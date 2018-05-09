package eu.fbk.das.domainobject.core.persistence.model;

import eu.fbk.das.domainobject.core.persistence.StateEntity;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

import java.util.List;

@NodeEntity
public class DomainPropertyModel {

    @Id
    @GeneratedValue
    Long id;

    @Property(name = "name")
    String title;

    List<StateEntity> states;

    public DomainPropertyModel() {}

    public void setStates(List<StateEntity> states) {
        this.states = states;
    }

    public void setName(String name) {
        this.title = name;
    }

}
