package eu.fbk.das.domainobject.core.entity.activity;

import eu.fbk.das.domainobject.core.entity.ProcessDiagram;
import eu.fbk.das.domainobject.core.entity.jaxb.activity.PreconditionType;
import eu.fbk.das.domainobject.core.entity.jaxb.activity.SwitchType.If.VarCondition;

import java.util.List;

public class IFActivity extends ProcessActivity {

    private List<PreconditionType> conditions;
    private List<VarCondition> varConditions;

    public void setVarConditions(
            List<VarCondition> varConditions) {
        this.varConditions = varConditions;
    }

    public List<VarCondition> getVarConditions() {
        return varConditions;
    }

    private ProcessDiagram branch;

    public IFActivity() {
        super();
        this.setIF(true);
    }

    public IFActivity(
            int SourceState,
            int TargetState,
            List<PreconditionType> contextConditions,
            List<VarCondition> varConds,
            ProcessDiagram branch) {

        this.setSource(SourceState);
        this.setTarget(TargetState);
        this.conditions = contextConditions;
        this.varConditions = varConds;
        this.branch = branch;

    }

    public ProcessDiagram getBranch() {
        return branch;
    }

    public void setBranch(ProcessDiagram branch) {
        this.branch = branch;
    }

    public List<PreconditionType> getConditions() {
        return conditions;
    }

    public void setConditions(
            List<PreconditionType> conditions) {
        this.conditions = conditions;
    }

}
