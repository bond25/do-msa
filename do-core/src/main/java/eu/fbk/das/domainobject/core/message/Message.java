package eu.fbk.das.domainobject.core.message;


import java.util.Date;
import java.util.UUID;

public class Message<T> {

    private String id = UUID.randomUUID().toString();
    private String deploymentId;
    private String sender;
    private Date timestamp = new Date();
    private String correlationId;

    private T payload;

    public Message() {
    }

    public Message(T payload) {
        this.payload = payload;
    }

    public Message(String sender, String deploymentId, T payload) {
        this.sender = sender;
        this.deploymentId = deploymentId;
        this.payload = payload;
    }


    public String getId() {
        return id;
    }

    public Message<T> setId(String id) {
        this.id = id;
        return this;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Message<T> setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public T getPayload() {
        return payload;
    }

    public Message<T> setPayload(T payload) {
        this.payload = payload;
        return this;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public Message<T> setDeploymentId(String traceId) {
        this.deploymentId = traceId;
        return this;
    }

    public String getSender() {
        return sender;
    }

    public Message<T> setSender(String sender) {
        this.sender = sender;
        return this;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public Message<T> setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    @Override
    public String toString() {
        return "Message [id=" + id + ", timestamp=" + timestamp + ", payload=" + payload + ", correlationId=" + correlationId + "]";
    }

}
