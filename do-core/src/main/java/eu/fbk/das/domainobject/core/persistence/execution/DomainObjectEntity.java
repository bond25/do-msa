package eu.fbk.das.domainobject.core.persistence.execution;

import eu.fbk.das.domainobject.core.persistence.model.DomainObjectModel;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

@NodeEntity
public class DomainObjectEntity {

    @Id
    @GeneratedValue
    Long id;

    String correlationId;

    @Relationship(type = "DEPLOYMENT")
    DeploymentEntity deployment;

    @Relationship(type = "PROCESS_ACTIVITY")
    ProcessEntity process;

    @Relationship(type = "PROPERTY")
    List<DomainPropertyEntity> properties;

    public DomainObjectEntity() {}

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public void setProcess(ProcessEntity process) {
        this.process = process;
    }

    public ProcessEntity getProcess() {
        return process;
    }

    public void setDeployment(DeploymentEntity deployment) {
        this.deployment = deployment;
    }

    public void setProperties(List<DomainPropertyEntity> properties) {
        this.properties = properties;
    }
}
