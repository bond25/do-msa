package eu.fbk.das.engine;

import eu.fbk.das.domainobject.core.entity.DomainObjectInstance;
import eu.fbk.das.domainobject.core.entity.ProcessDiagram;
import eu.fbk.das.domainobject.core.entity.activity.AbstractActivity;
import eu.fbk.das.domainobject.core.entity.activity.ProcessActivity;
import eu.fbk.das.domainobject.core.entity.jaxb.activity.EffectType;
import eu.fbk.das.domainobject.core.message.AdaptationResult;
import eu.fbk.das.domainobject.core.message.ExecuteTask;
import eu.fbk.das.domainobject.core.message.Message;
import eu.fbk.das.domainobject.core.message.TaskExecuted;
import eu.fbk.das.domainobject.core.service.RepositoryService;
import eu.fbk.das.engine.impl.DeployService;
import eu.fbk.das.engine.impl.MessagePublisherImpl;

public interface ProcessEngine {

    boolean checkPrecondition(ProcessDiagram proc, ProcessActivity current);

    void applyEffect(ProcessDiagram proc, EffectType effect);

    //TODO: getExecutionHandler method in ATLAS pay attention
    DelegateExecution getDelegateHandler(String name);

    MessagePublisherImpl getMessagePublisher();

    RepositoryService getRepositoryService();

    String getDeploymentId();

    String getDomainObjectName();

    void deploy(String path);

    void setDeploymentId(String deploymentId);

    String startInstance();

    String startInstanceByCorrelationId(String correlationId);

    void step(String correlationId);

    TaskExecuted executeFragmentActivity(Message<ExecuteTask> m);

    void correlateMessage(TaskExecuted message);

    DomainObjectInstance getDoi(ProcessDiagram proc);

    void addToWaitingList(ProcessDiagram proc);

    boolean canHandleInstance(String correlationId);

    boolean canHandle(String deploymentId);

    void createDeployment();

    //getters for services
    InstanceManager getInstanceManager();

    DeployService getDeployService();

    void handleAbstractActivity(ProcessDiagram proc, AbstractActivity activity);

    void injectAdaptationResult(AdaptationResult ar);

    void suspendProcess(ProcessDiagram proc);

    void stepAll();

}
