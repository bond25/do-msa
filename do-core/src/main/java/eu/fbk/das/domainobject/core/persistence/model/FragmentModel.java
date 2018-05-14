package eu.fbk.das.domainobject.core.persistence.model;


import eu.fbk.das.domainobject.core.entity.jaxb.Fragment;
import eu.fbk.das.domainobject.core.persistence.StateEntity;
import org.neo4j.ogm.annotation.*;

import java.util.List;

@NodeEntity
public class FragmentModel {

    @Id
    @GeneratedValue
    Long id;

    @Property(name = "name")
    String title;

    @Relationship(type = "STATE", direction = Relationship.OUTGOING)
    List<StateEntity> states;

    @Relationship(type = "START_ACTIVITY", direction = Relationship.OUTGOING)
    FragmentActionModel startActivity;

    public FragmentModel() {}

    public FragmentModel setName(String name) {
        this.title = name;
        return this;
    }

    public FragmentModel setStartActivity(FragmentActionModel startActivity) {
        this.startActivity = startActivity;
        return this;
    }

    public FragmentActionModel getStartActivity() {
        return startActivity;
    }

    public FragmentModel setStates(List<StateEntity> states) {
        this.states = states;
        return this;
    }

    public String getName() {
        return title;
    }

    public List<StateEntity> getStates() {
        return states;
    }

    public Long getId() {
        return id;
    }
}
