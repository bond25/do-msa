package eu.fbk.das.domainobject.core.persistence;

import org.neo4j.ogm.annotation.*;

@RelationshipEntity(type = "TRANSITION")
public class DomainPropertyTransition {

    @Id
    @GeneratedValue
    private Long id;

    private String event;

    @StartNode
    private StateEntity from;

    @EndNode
    private StateEntity to;

    public DomainPropertyTransition() {}

    public DomainPropertyTransition(String event, StateEntity from, StateEntity to) {
        this.event = event;
        this.from = from;
        this.to = to;
    }



}
