package eu.fbk.das.domainobject.core.persistence.execution;

import eu.fbk.das.domainobject.core.persistence.model.DomainObjectModel;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class DeploymentEntity {

    @Id
    @GeneratedValue
    Long id;

    String deploymentId;

    @Relationship(type = "MODEL")
    DomainObjectModel model;

    public DeploymentEntity() {}

    public DeploymentEntity setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
        return this;
    }

    public DeploymentEntity setDoModel(DomainObjectModel dom) {
        this.model = dom;
        return this;
    }

}
