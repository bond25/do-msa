package eu.fbk.das.domainobject.core.persistence.execution;

import eu.fbk.das.domainobject.core.entity.jaxb.DomainProperty;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity
public class DomainPropertyEntity {

    @Id
    @GeneratedValue
    Long id;

    @Property(name = "name")
    String title;

    String type;

    String currentState;

    public DomainPropertyEntity() {}

    public String getName() {
        return title;
    }

    public DomainPropertyEntity setName(String name) {
        this.title = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public DomainPropertyEntity setType(String type) {
        this.type = type;
        return this;
    }

    public String getCurrentState() {
        return currentState;
    }

    public DomainPropertyEntity setCurrentState(String currentState) {
        this.currentState = currentState;
        return this;
    }

}
