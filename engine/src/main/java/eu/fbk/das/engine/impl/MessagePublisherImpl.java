package eu.fbk.das.engine.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.fbk.das.domainobject.core.message.AdaptationProblem;
import eu.fbk.das.domainobject.core.message.Message;
import eu.fbk.das.domainobject.core.message.TaskExecuted;
import eu.fbk.das.engine.message.MessageAdapter;
import eu.fbk.das.engine.message.MessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(MessageAdapter.class)
public class MessagePublisherImpl implements MessagePublisher {

    @Autowired
    MessageChannel execution;

    @Autowired
    MessageChannel adaptation;

    public void sendAdaptationProblem(Message<AdaptationProblem> m) {
        try {
            String jsonMessage = new ObjectMapper().writeValueAsString(m);
            adaptation.send(MessageBuilder.withPayload(jsonMessage).setHeader("eventType", "AdaptationProblem").build());
        } catch (Exception e) {
            throw new RuntimeException("Could not tranform and send message due to: "+ e.getMessage(), e);
        }
    }

    public void sendTaskExecuted(Message<TaskExecuted> m) {
        try {
            String jsonMessage = new ObjectMapper().writeValueAsString(m);
            execution.send(MessageBuilder.withPayload(jsonMessage).setHeader("eventType", "TaskExecuted").build());
        } catch (Exception e) {
            throw new RuntimeException("Could not tranform and send message due to: "+ e.getMessage(), e);
        }
    }
}
