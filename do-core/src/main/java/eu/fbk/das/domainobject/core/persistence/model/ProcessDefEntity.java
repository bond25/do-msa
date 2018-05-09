package eu.fbk.das.domainobject.core.persistence.model;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;
import java.util.UUID;

public class ProcessDefEntity {

    @Id
    @GeneratedValue
    Long id;

    String uuid;

    @Property(name="name")
    String title;

    @Relationship(type = "ACTIVITY", direction = Relationship.OUTGOING)
    List<ProcessAction> activities;

    public ProcessDefEntity() {
        this.uuid = UUID.randomUUID().toString();
    }

}
