package eu.fbk.das.domainobject.core.message;

import eu.fbk.das.domainobject.core.entity.jaxb.GoalType;

public class AdaptationProblem {

    GoalType goal;

    public AdaptationProblem(){}

    public AdaptationProblem(GoalType goal) {
        this.goal = goal;
    }

    public GoalType getGoal() {
        return goal;
    }

    public void setGoal(GoalType goal) {
        this.goal = goal;
    }
}
