package eu.fbk.das.domainobject.core.persistence;


import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity
public class StateEntity {

    @Id
    @GeneratedValue
    Long id;

    @Property(name = "name")
    String title;

    boolean isInitial = false;

    public StateEntity() {

    }

    public StateEntity(String name) {
        this.title = name;
    }

    public String getName() {
        return title;
    }

    public void setIsInitial(boolean isInitial) {
        this.isInitial = isInitial;
    }

    public boolean isInitial() {
        return isInitial;
    }
}
