package eu.fbk.das.domainobject.core.message;

public class TaskExecuted {

    private TaskExecutedStatus status;

    private String deploymentId;

    private String correlationId;

    public TaskExecuted() {

    }

    public TaskExecutedStatus getStatus() {
        return status;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public TaskExecuted setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
        return this;
    }

    public TaskExecuted setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public TaskExecuted setStatus(TaskExecutedStatus status) {
        this.status = status;
        return this;
    }
}
