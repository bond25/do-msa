package eu.fbk.das.engine.impl;

import eu.fbk.das.domainobject.core.entity.*;
import eu.fbk.das.domainobject.core.entity.activity.AbstractActivity;
import eu.fbk.das.domainobject.core.entity.jaxb.activity.ClauseType;
import eu.fbk.das.domainobject.core.exceptions.InvalidObjectCurrentStateException;
import eu.fbk.das.domainobject.core.exceptions.InvalidObjectEventException;
import eu.fbk.das.domainobject.core.exceptions.InvalidServiceActionException;
import eu.fbk.das.domainobject.core.exceptions.InvalidServiceCurrentStateException;
import eu.fbk.das.domainobject.core.message.*;
import eu.fbk.das.domainobject.core.persistence.execution.DeploymentEntity;
import eu.fbk.das.domainobject.core.persistence.model.DomainObjectModel;
import eu.fbk.das.domainobject.core.service.RepositoryService;
import eu.fbk.das.engine.DelegateExecution;
import eu.fbk.das.engine.Handler;
import eu.fbk.das.engine.InstanceManager;
import eu.fbk.das.engine.ProcessEngine;
import eu.fbk.das.engine.impl.handlers.*;
import eu.fbk.das.domainobject.core.entity.activity.ProcessActivity;
import eu.fbk.das.domainobject.core.entity.activity.ProcessActivityType;
import eu.fbk.das.domainobject.core.entity.jaxb.Fragment;
import eu.fbk.das.domainobject.core.entity.jaxb.activity.ActivityType;
import eu.fbk.das.domainobject.core.entity.jaxb.activity.EffectType;
import eu.fbk.das.domainobject.core.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.dsl.channel.QueueChannelSpec;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Component
public class ProcessEngineImpl implements ProcessEngine {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessEngineImpl.class);

    private InstanceManager instanceManager;

    ConcurrentHashMap<String, Queue<TaskExecuted>> messages = new ConcurrentHashMap<>();

    ConcurrentMap<String, DomainObjectInstance> instances = new ConcurrentHashMap<>();

    ConcurrentHashMap<String, DomainObjectInstance> waitingInstances = new ConcurrentHashMap<>();

    Set<String> correlationIds = new HashSet<>();

    protected ConcurrentHashMap<ProcessActivityType, Handler> handlers = new ConcurrentHashMap<ProcessActivityType, Handler>();

    protected ConcurrentMap<String, DelegateExecution> delegateHandlers = new ConcurrentHashMap<String, DelegateExecution>();

    protected ConcurrentMap<String, ConcurrentHashMap<String, ProcessActivity>> fragmentsActivities = new ConcurrentHashMap<>();

    DomainObjectDefinition domainObjectDefinition;

    DeploymentEntity deploymentEntity;

    String deploymentId;

    DeployService deployService;

    @Autowired
    MessagePublisherImpl messagePublisher;

    @Autowired
    RepositoryService repositoryService;

    public ProcessEngineImpl() {
        if (deploymentId == null) {
            deploymentId = UUID.randomUUID().toString();
        }
        deployService = new DeployService(this);
        instanceManager = new InstanceManagerImpl(this);
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
        boolean result = false;
        DomainObjectInstance doi = instanceManager.getInstance(proc);

        if (current.getPrecondition() != null) {
            for (ClauseType.Point point : current.getPrecondition().getPoint()) {
                for (ClauseType.Point.DomainProperty dp : point.getDomainProperty()) {
                    for (ObjectDiagram od : doi.getInternalKnowledge()) {
                        if (od.getOid().equals(dp.getDpName())) {
                            result = dp.getState()
                                    .stream()
                                    .anyMatch(s -> od.getCurrentState().equals(s));
                        }
                    }
                }
            }
        }

        return result;
    }

    @Override
    public void applyEffect(ProcessDiagram proc, EffectType effect) {
        boolean result = false;
        DomainObjectInstance doi = instanceManager.getInstance(proc);

        if (effect != null && effect.getEvent() != null && !effect.getEvent().isEmpty()) {
            result = effect.getEvent().stream()
                    .allMatch(e -> {
                        boolean flag = false;
                        for (ObjectDiagram od : doi.getInternalKnowledge()) {
                            if (od.getOid().equals(e.getDpName())) {
                                try {
                                    od.publishEvent(e.getEventName());
                                    flag = true;
                                } catch (InvalidObjectEventException |
                                        InvalidObjectCurrentStateException e1) {
                                    LOG.error("Effect error {}", e1);
                                }
                            }
                        }
                        return flag;
                    });
        }

        if (result) {
            LOG.debug("All effects was applied");
        } else {
            LOG.debug("Error while applying effects");
        }
    }

    public Handler getHandler(ProcessActivityType type) {
        return handlers.get(type);
    }

    public DelegateExecution getDelegateHandler(String name) {
        return delegateHandlers.get(name);
    }

    @Override
    public MessagePublisherImpl getMessagePublisher() {
        return messagePublisher;
    }

    @Override
    public String getDeploymentId() {
        return deploymentId;
    }

    @Override
    public String getDomainObjectName() {
        return domainObjectDefinition.getDomainObject().getName();
    }

    public void createDeployment() {
        DomainObjectModel dom = repositoryService.createDoModel(domainObjectDefinition);
        repositoryService.createDeployment(dom, this.deploymentId);
    }

    public void deploy(String path) {
        this.domainObjectDefinition = deployService.deploy();
        this.delegateHandlers =  getDelegateHandlers(domainObjectDefinition, String.format("%s.handlers", path));
        getTaskSubscriptions(domainObjectDefinition);
        DomainObjectModel dom = repositoryService.createDoModel(domainObjectDefinition);
        DeploymentEntity de = repositoryService.createDeployment(dom, this.deploymentId);
        this.deploymentEntity = de;
    }

    public ConcurrentMap<String, ProcessActivity> getTaskSubscriptions(DomainObjectDefinition dod) {
        ConcurrentHashMap<String, ProcessActivity> result = new ConcurrentHashMap<>();
        List<ActivityType> procActions = dod.getProcess().getActivity().stream().peek(a -> a.getClass().getSimpleName()).collect(Collectors.toList());
        List<String> concreteActions = procActions.stream().
                filter(a -> a.getClass().getSimpleName().equals("ConcreteType")).
                map(a -> a.getName()).
                collect(Collectors.toList());
        for (Fragment f : dod.getFragments()) {
            List<String> concreteNames = f.getAction().stream().
                    filter(a -> !a.getActionType().value().equals("output")).
                    map(a -> a.getName()).collect(Collectors.toList());
        }
        return result;
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
            } catch (InstantiationException
                    | InvocationTargetException
                    | IllegalAccessException
                    | NoSuchMethodException
                    | ClassNotFoundException e) {
                LOG.error("Error while parsing Delegate Handlers {}", e);
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
        process.setCorrelationId(correlationId);
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
        LOG.debug("Started new instance with correlationId {}", correlationId);
        return correlationId;
    }

    @Override
    public String startInstanceByCorrelationId(String correlationId) {
        DomainObjectInstance doi = Parser.convertToDomainObjectInstance(domainObjectDefinition);
        doi.setId(correlationId);
        ProcessDiagram process = doi.getProcess();
        process.setCorrelationId(correlationId);
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
        instanceManager.registerInstance(doi);
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
        LOG.debug("Started new instance by correlationId {}", correlationId);
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
        return instanceManager.getInstance(proc);
    }

    public void addToWaitingList(ProcessDiagram proc) {
        DomainObjectInstance doi = getDoi(proc);
        waitingInstances.put(doi.getId(), doi);
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public boolean canHandleInstance(String correlationId) {
        return correlationIds.contains(correlationId);
    }

    public boolean canHandle(String deploymentId) {
        return deploymentId.equals(this.deploymentId);
    }

    public InstanceManager getInstanceManager() {
        return this.instanceManager;
    }

    public DeployService getDeployService() {
        return this.deployService;
    }

    public void handleAbstractActivity(ProcessDiagram proc, AbstractActivity activity) {
        AdaptationProblem ap = new AdaptationProblem(activity.getGoal());
        Message<AdaptationProblem> m = new Message<>(deploymentId, proc.getCorrelationId(), ap);
        messagePublisher.sendAdaptationProblem(m);
    }

    public void injectAdaptationResult(AdaptationResult ar) {
        DomainObjectInstance doi = instanceManager.getInstance(ar.getCorrelationId());
        ProcessDiagram proc = doi.getProcess();
        ProcessDiagram refinement = ar.getRefinement();
        refinement.setCorrelationId(ar.getCorrelationId());
        //pe.registerProcess(refinement, proc);
        // we can remove the refinement from the awaiting list
        refinement.setFather(proc);
        if (refinement.getStates() != null
                && refinement.getStates().size() > 0) {
            proc.getCurrentActivity().setRunning(false);
            proc.setRunning(false);
            refinement.setCurrentActivity(refinement.getFirstActivity());
            refinement.setRunning(true);
            //pe.addRunningRefinements(proc, refinement);
        } else {
            LOG.warn("[" + proc.getCorrelationId() + "] Refinement is empty");
            proc.getCurrentActivity().setRunning(false);
            proc.getCurrentActivity().setExecuted(true);
        }
    }

    public void suspendProcess(ProcessDiagram proc) {

    }

    public TaskExecuted executeFragmentActivity(Message<ExecuteTask> m) {
        DomainObjectInstance doi = instanceManager.getInstance(m.getCorrelationId());
        TaskExecuted result = new TaskExecuted();
        ExecuteTask cmd = m.getPayload();
        ConcurrentHashMap<String, ProcessActivity> activities = fragmentsActivities.get(cmd.getFragmentName());
        ProcessActivity act = activities.get(cmd.getActivity().getName());
        for (ServiceDiagram service : doi.getFragments()) {
            if (service.getSid().equals(cmd.getFragmentName())) {
                boolean isExecuted = false;
                try {
                    isExecuted = service.executeAction(cmd.getActivity().getName());
                } catch (InvalidServiceActionException |
                        InvalidServiceCurrentStateException e) {
                    LOG.error("Error while executing the fragment action {}", cmd.getActivity().getName());
                }
                if (isExecuted) {
                    result.setStatus(TaskExecutedStatus.COMPLETED);
                    return result;
                }
            }
        }
        result.setStatus(TaskExecutedStatus.FAILED);
        return result;
    }

    public void correlateMessage(TaskExecuted payload) {
        DomainObjectInstance doi = instanceManager.getInstance(payload.getCorrelationId());
        Queue<TaskExecuted> msgQueue = messages.get(payload.getCorrelationId());
        if (msgQueue.isEmpty()) {
        }

    }

    public void stepAll() {

    }

}
