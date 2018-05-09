package eu.fbk.das.domainobject.core.persistence;

import org.neo4j.ogm.annotation.*;

@RelationshipEntity(type="TRANSITION")
public class StateTransition {

    @Id
    @GeneratedValue
    private Long id;

    private String actionName;

    private String actionType;

    @StartNode
    private StateEntity from;

    @EndNode
    private StateEntity to;

    public StateTransition() {}

    public StateTransition(String actionName, String actionType, StateEntity from, StateEntity to) {
        this.actionName = actionName;
        this.actionType = actionType;
        this.from = from;
        this.to = to;
    }


}
