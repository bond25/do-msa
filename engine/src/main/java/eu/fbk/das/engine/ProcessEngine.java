package eu.fbk.das.engine;

import eu.fbk.das.domainobject.core.entity.DomainObjectInstance;
import eu.fbk.das.domainobject.core.entity.ProcessDiagram;
import eu.fbk.das.domainobject.core.entity.activity.ProcessActivity;
import eu.fbk.das.domainobject.core.entity.jaxb.activity.EffectType;
import eu.fbk.das.domainobject.core.service.RepositoryService;
import org.apache.tomcat.jni.Proc;

public interface ProcessEngine {

    boolean checkPrecondition(ProcessDiagram proc, ProcessActivity current);

    void applyEffect(ProcessDiagram proc, EffectType effect);

    //TODO: getExecutionHandler method in ATLAS pay attention
    DelegateExecution getDelegateHandler(String name);

    MessageService getMessageService();

    RepositoryService getRepositoryService();

    String getDeploymentId();

    String getDomainObjectName();

    void deploy(String path);

    void setDeploymentId(String deploymentId);

    String startInstance();

    String startInstanceByCorrelationId(String correlationId);

    void step(String correlationId);

    DomainObjectInstance getDoi(ProcessDiagram proc);

    void addToWaitingList(ProcessDiagram proc);

    boolean canHandleInstance(String correlationId);

    public void createDeployment();

}
