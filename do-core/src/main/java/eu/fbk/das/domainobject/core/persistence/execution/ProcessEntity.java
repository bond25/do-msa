package eu.fbk.das.domainobject.core.persistence.execution;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity
public class ProcessEntity {

    @Id
    @GeneratedValue
    Long id;

    @Property(name = "name")
    String title;

    String currentActivity;

    boolean isEnded;

    public ProcessEntity() {}

    public String getName() {
        return title;
    }

    public ProcessEntity setName(String name) {
        this.title = name;
        return this;
    }

    public String getCurrentActivity() {
        return this.currentActivity;
    }

    public ProcessEntity setCurrentActivity(String currentActivity) {
        this.currentActivity = currentActivity;
        return this;
    }

    public boolean isEnded() {
        return this.isEnded;
    }

    public ProcessEntity setIsEnded(boolean isEnded) {
        this.isEnded = isEnded;
        return this;
    }
}
