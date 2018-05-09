package eu.fbk.das.domainobject.core.entity.activity;


import eu.fbk.das.domainobject.core.entity.ProcessDiagram;
import eu.fbk.das.domainobject.core.entity.jaxb.activity.ClauseType;
import eu.fbk.das.domainobject.core.entity.jaxb.activity.WhileType.VarCondition;

public class WhileActivity extends ProcessActivity {

    private VarCondition condition;
    private ProcessDiagram defaultBranch;
    private ClauseType contexCondition;

    public WhileActivity() {
    }

    public WhileActivity(String name) {
        super(name);

    }

    public WhileActivity(int SourceState, int TargetState, String name) {
        super(SourceState, TargetState, name, ProcessActivityType.WHILE);
        this.name = name;
        this.setSource(SourceState);
        this.setTarget(TargetState);
        this.setWhile(true);

    }

    @Override
    public WhileActivity getCopyOfActivity() {
        return new WhileActivity(this.name);
    }

    public void setCondition(VarCondition cond) {
        this.condition = cond;
    }

    public VarCondition getCondition() {
        return condition;
    }

    public void addDefaultBranch(ProcessDiagram defbranch) {
        this.defaultBranch = defbranch;

    }

    public ProcessDiagram getDefaultBranch() {
        return defaultBranch;
    }

    public void setContextCondition(ClauseType contextCondition) {
        this.contexCondition = contextCondition;
    }

    public ClauseType getContextCondition() {
        return contexCondition;
    }

    @Override
    public String toString() {
        return "[name=" + name + "]";
    }

}
