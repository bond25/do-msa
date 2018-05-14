package eu.fbk.das.domainobject.core.message;

import eu.fbk.das.domainobject.core.entity.activity.ProcessActivity;

public class ExecuteTask {

    private String deploymentId;
    private String fragmentName;
    private ProcessActivity activity;

    public ExecuteTask() {

    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getDeploymentId() {
        return this.deploymentId;
    }

    public void setFragmentName(String fragmentName) {
        this.fragmentName = fragmentName;
    }

    public String getFragmentName() {
        return fragmentName;
    }

    public void setActivity(ProcessActivity activity) {
        this.activity = activity;
    }

    public ProcessActivity getActivity() {
        return activity;
    }
}
