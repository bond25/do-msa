package eu.fbk.das.test.composer.composertester;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.fbk.das.domainobject.core.message.AdaptationProblem;
import eu.fbk.das.domainobject.core.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(MessageAdapter.class)
public class MessageService {

    @Autowired
    MessageChannel execution;

    @Autowired
    MessageChannel adaptation;

    public void sendAdaptationProblem(Message<AdaptationProblem> m) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonMessage = mapper.writeValueAsString(m);
            adaptation.send(MessageBuilder.withPayload(jsonMessage).setHeader("eventType", "AdaptationProblem").build());
        } catch (Exception e) {
            throw new RuntimeException("Could not tranform and send message due to: " + e.getMessage(), e);
        }
    }
}

