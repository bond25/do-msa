package eu.fbk.das.domainobject.core.persistence;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class PreconditionEntity {

    @Id
    @GeneratedValue
    Long id;

    String domainPropertyName;

    String state;

    public PreconditionEntity() {

    }

    public String getDomainPropertyName() {
        return domainPropertyName;
    }

    public String getState() {
        return state;
    }

    public PreconditionEntity setDomainPropertyName(String domainPropertyName) {
        this.domainPropertyName = domainPropertyName;
        return this;
    }

    public PreconditionEntity setState(String state) {
        this.state = state;
        return this;
    }

}
