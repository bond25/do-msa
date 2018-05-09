package eu.fbk.das.domainobject.core.entity.activity;

import eu.fbk.das.domainobject.core.entity.ProcessDiagram;
import eu.fbk.das.domainobject.core.entity.jaxb.activity.EventHandlerType;

import java.util.List;

public class ScopeActivity extends ProcessActivity {

    private List<EventHandlerType> eventHandler;
    private ProcessDiagram branch; // a branch for a scope is just a list of
    // activities to monitor

    public ScopeActivity() {
    }

    public ScopeActivity(String name) {
        super(name);

    }

    public ScopeActivity(int SourceState, int TargetState, String name) {
        super(SourceState, TargetState, name, ProcessActivityType.SCOPE);
        this.name = name;
        this.setSource(SourceState);
        this.setTarget(TargetState);
        this.setScope(true);

    }

    @Override
    public ScopeActivity getCopyOfActivity() {
        return new ScopeActivity(this.name);
    }

    public void setEventHandler(List<EventHandlerType> eventHandler) {
        this.eventHandler = eventHandler;
    }

    public List<EventHandlerType> getEventHandler() {
        return eventHandler;
    }

    public void setBranch(ProcessDiagram branch) {
        this.branch = branch;

    }

    public ProcessDiagram getBranch() {
        return branch;
    }

    @Override
    public String toString() {
        return "[name=" + name + "]";
    }

}
