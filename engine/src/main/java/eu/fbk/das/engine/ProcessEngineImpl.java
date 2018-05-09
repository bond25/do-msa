package eu.fbk.das.engine;

import eu.fbk.das.domainobject.core.message.AdaptationResult;
import eu.fbk.das.domainobject.core.persistence.execution.DeploymentEntity;
import eu.fbk.das.domainobject.core.persistence.model.DomainObjectModel;
import eu.fbk.das.domainobject.core.service.RepositoryService;
import eu.fbk.das.engine.handlers.*;
import eu.fbk.das.domainobject.core.entity.DomainObjectDefinition;
import eu.fbk.das.domainobject.core.entity.DomainObjectInstance;
import eu.fbk.das.domainobject.core.entity.ProcessDiagram;
import eu.fbk.das.domainobject.core.entity.activity.ProcessActivity;
import eu.fbk.das.domainobject.core.entity.activity.ProcessActivityType;
import eu.fbk.das.domainobject.core.entity.jaxb.Fragment;
import eu.fbk.das.domainobject.core.entity.jaxb.activity.ActivityType;
import eu.fbk.das.domainobject.core.entity.jaxb.activity.EffectType;
import eu.fbk.das.domainobject.core.parser.Parser;
import eu.fbk.das.domainobject.core.repository.DomainObjectModelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Component
public class ProcessEngineImpl implements ProcessEngine {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessEngineImpl.class);

    ConcurrentMap<String, DomainObjectInstance> instances = new ConcurrentHashMap<>();

    ConcurrentHashMap<String, DomainObjectInstance> waitingInstances = new ConcurrentHashMap<>();

    ConcurrentHashMap<ProcessDiagram, DomainObjectInstance> procToDoi = new ConcurrentHashMap<>();

    Set<String> correlationIds = new HashSet<>();

    protected ConcurrentHashMap<ProcessActivityType, Handler> handlers = new ConcurrentHashMap<ProcessActivityType, Handler>();

    protected ConcurrentMap<String, DelegateExecution> delegateHandlers = new ConcurrentHashMap<String, DelegateExecution>();

    DomainObjectDefinition domainObjectDefinition;

    DeploymentEntity deploymentEntity;

    String deploymentId;

    DeployService deployService;

    @Autowired
    MessageService messageService;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    DomainObjectModelRepository dodRepo;

    public ProcessEngineImpl() {
        if (deploymentId == null) {
            deploymentId = UUID.randomUUID().toString();
        }
        deployService = new DeployService();
        handlers.put(ProcessActivityType.ABSTRACT, new AbstractActivityHandler());
        handlers.put(ProcessActivityType.CONCRETE, new ConcreteActivityHandler());
        handlers.put(ProcessActivityType.INVOKE, new InvokeActivityHandler());
        handlers.put(ProcessActivityType.REPLY, new ReplyActivityHandler());
        handlers.put(ProcessActivityType.PICK, new PickActivityHandler());
        handlers.put(ProcessActivityType.SCOPE, new ScopeActivityHandler());
        handlers.put(ProcessActivityType.SWITCH, new SwitchActivityHandler());
        handlers.put(ProcessActivityType.WHILE, new WhileActivityHandler());
    }

    public void setDomainObjectDefinition(DomainObjectDefinition domainObjectDefinition) {
        this.domainObjectDefinition = domainObjectDefinition;
    }

    public DomainObjectDefinition getDomainObjectDefinition(){
        return this.domainObjectDefinition;
    }

    @Override
    public boolean checkPrecondition(ProcessDiagram proc, ProcessActivity current) {
        return false;
    }

    @Override
    public void applyEffect(ProcessDiagram proc, EffectType effect) {

    }

    public Handler getHandler(ProcessActivityType type) {
        return handlers.get(type);
    }

    public DelegateExecution getDelegateHandler(String name) {
        return delegateHandlers.get(name);
    }

    @Override
    public MessageService getMessageService() {
        return messageService;
    }

    @Override
    public String getDeploymentId() {
        return deploymentId;
    }

    @Override
    public String getDomainObjectName() {
        return domainObjectDefinition.getDomainObject().getName();
    }

    public void deploy(String path) {
        this.domainObjectDefinition = deployService.deploy();
        this.delegateHandlers =  getDelegateHandlers(domainObjectDefinition, String.format("%s.handlers", path));
        DomainObjectModel dom = repositoryService.createDoModel(domainObjectDefinition);
        DeploymentEntity de = repositoryService.createDeployment(dom, this.deploymentId);
        this.deploymentEntity = de;
    }

    public ConcurrentMap<String, DelegateExecution> getDelegateHandlers(DomainObjectDefinition dod, String rootPath) {
        ConcurrentHashMap<String, DelegateExecution> result = new ConcurrentHashMap<>();
        List<ActivityType> procActions = dod.getProcess().getActivity().stream().peek(a -> a.getClass().getSimpleName()).collect(Collectors.toList());
        List<String> concreteActions = procActions.stream().
                filter(a -> a.getClass().getSimpleName().equals("ConcreteType")).
                map(a -> a.getName()).
                collect(Collectors.toList());
        for (Fragment f : dod.getFragments()) {
            List<String> concreteNames = f.getAction().stream().
                    filter(a -> a.getActionType().value().equals("concrete")).
                    map(a -> a.getName()).collect(Collectors.toList());
            concreteActions.addAll(concreteNames);
        }
        concreteActions.forEach(act -> {
            try {
                DelegateExecution handler = (DelegateExecution) ClassLoader.getSystemClassLoader().loadClass(String.format("%s.%s", rootPath, act)).getConstructor().newInstance();
                result.put(act, handler);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        LOG.debug("Concrete handlers {}", result);
        return result;
    }



    public String startInstance() {
        DomainObjectInstance doi = Parser.convertToDomainObjectInstance(domainObjectDefinition);
        String correlationId = UUID.randomUUID().toString();
        doi.setId(correlationId);
        ProcessDiagram process = doi.getProcess();
        process.setPid(1);
        process.setRunning(true);
        process.setEnded(false);
        process.setTerminated(false);
        process.setCurrentActivity(process.getActivities().get(0));
        for (ProcessActivity act : process.getActivities()) {
            act.setExecuted(false);
            act.setRunning(false);
            act.setStopped(false);
        }
        instances.put(correlationId, doi);
        repositoryService.createInstance(deploymentEntity, correlationId, doi);
        correlationIds.add(correlationId);
        procToDoi.put(doi.getProcess(), doi);
        LOG.debug("Started new instance with correlationId {}", correlationId);
        return correlationId;
    }

    @Override
    public String startInstanceByCorrelationId(String correlationId) {
        DomainObjectInstance doi = Parser.convertToDomainObjectInstance(domainObjectDefinition);
        doi.setId(correlationId);
        ProcessDiagram process = doi.getProcess();
        process.setPid(1);
        process.setRunning(true);
        process.setEnded(false);
        process.setTerminated(false);
        process.setCurrentActivity(process.getActivities().get(0));
        for (ProcessActivity act : process.getActivities()) {
            act.setExecuted(false);
            act.setRunning(false);
            act.setStopped(false);
        }
        instances.put(correlationId, doi);
        repositoryService.createInstance(deploymentEntity, correlationId, doi);
        correlationIds.add(correlationId);
        procToDoi.put(doi.getProcess(), doi);
        LOG.debug("Started new instance with correlationId {}", correlationId);
        return correlationId;
    }

    public void startInstanceByCorrelationId(String correlationId, AdaptationResult ar) {
        DomainObjectInstance doi = Parser.convertToDomainObjectInstance(domainObjectDefinition);
        doi.setId(correlationId);
        //TODO: how to handle such situation
        //inject refinement process diagram
        ProcessDiagram refinement = ar.getRefinement();
        refinement.setFather(doi.getProcess());

        instances.put(correlationId, doi);
        LOG.debug("Started new instance with by correlationId {}", correlationId);
    }

    public void step(String correlationId) {
        DomainObjectInstance doi = instances.get(correlationId);
        ProcessDiagram proc = doi.getProcess();
        if (!proc.isRunning()) {
            //TODO: remove from execution
        }
        if (proc.getCurrentActivity().isExecuted()) {
            // logica per passare alla prossima attivita'
            if (!proc.getNextActivity().isEmpty()
                    && proc.getCurrentActivity().isExecuted()) {
                proc.setCurrentActivity(proc.getNextActivity().get(0));
            } else if (proc.getNextActivity().isEmpty()
                    && proc.getCurrentActivity().isExecuted()) {
                proc.setEnded(true);
            }
        }
        Handler handler = handlers.get(proc.getCurrentActivity().getType());
        if (handler != null) {
            handler.handle(this, proc, proc.getCurrentActivity());
        } else {
        }
        // logica per passare alla prossima attivita'
        if (!proc.getNextActivity().isEmpty()
                && proc.getCurrentActivity().isExecuted()) {
            proc.setCurrentActivity(proc.getNextActivity().get(0));
        } else if (proc.getNextActivity().isEmpty()
                && proc.getCurrentActivity().isExecuted()) {
            proc.setEnded(true);
        }
    }

    public RepositoryService getRepositoryService() {
        return repositoryService;
    }

    public DomainObjectInstance getDoi(ProcessDiagram proc) {
        return procToDoi.get(proc);
    }

    public void addToWaitingList(ProcessDiagram proc) {
        DomainObjectInstance doi = getDoi(proc);
        waitingInstances.put(doi.getId(), doi);
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public boolean canHandleInstance(String correlationId) {
        return correlationIds.contains(correlationId) ? true : false;
    }

}
