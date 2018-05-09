package eu.fbk.das.domainobject.core.persistence.model;


import eu.fbk.das.domainobject.core.persistence.EffectEntity;
import eu.fbk.das.domainobject.core.persistence.GoalEntity;
import eu.fbk.das.domainobject.core.persistence.PreconditionEntity;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

@NodeEntity
public class FragmentActionModel {

    @Id
    @GeneratedValue
    Long id;

    String name;

    String type;

    @Relationship(type = "NEXT_ACTION", direction = Relationship.OUTGOING)
    FragmentActionModel nextAction;

    @Relationship(type = "GOAL", direction = Relationship.OUTGOING)
    List<GoalEntity> goals;

    @Relationship(type = "PRECONDITION", direction = Relationship.OUTGOING)
    List<PreconditionEntity> preconditions;

    @Relationship(type = "EFFECT", direction = Relationship.OUTGOING)
    List<EffectEntity> effects;

    public FragmentActionModel() {}

    public FragmentActionModel(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public FragmentActionModel setName(String name) {
        this.name = name;
        return this;
    }

    public FragmentActionModel setType(String type) {
        this.type = type;
        return this;
    }

    public FragmentActionModel setGoals(List<GoalEntity> goals) {
        this.goals = goals;
        return this;
    }

    public FragmentActionModel setPreconditions(List<PreconditionEntity> preconditions) {
        this.preconditions = preconditions;
        return this;
    }

    public FragmentActionModel setEffects(List<EffectEntity> effects) {
        this.effects = effects;
        return this;
    }

    public FragmentActionModel getNextAction() {
        return nextAction;
    }

    public FragmentActionModel setNextAction(FragmentActionModel nextAction) {
        this.nextAction = nextAction;
        return this;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public List<EffectEntity> getEffects() {
        return effects;
    }

    public List<GoalEntity> getGoals() {
        return goals;
    }

    public List<PreconditionEntity> getPreconditions() {
        return preconditions;
    }
}
