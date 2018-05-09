package eu.fbk.das.composer.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.fbk.das.domainobject.core.message.AdaptationResult;
import eu.fbk.das.domainobject.core.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(Source.class)
public class MessageService {

    @Autowired
    MessageChannel output;

    public void sendAdaptationResult(Message<AdaptationResult> m) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonMessage = mapper.writeValueAsString(m);
            output.send(
                    MessageBuilder.withPayload(jsonMessage).setHeader("eventType", "AdaptationResult").build());
        } catch (Exception e) {
            throw new RuntimeException("Could not tranform and send message due to: "+ e.getMessage(), e);
        }
    }

//    public void sendToAdaptation(Message<?> m) {
//        try {
//            // avoid too much magic and transform ourselves
//            ObjectMapper mapper = new ObjectMapper();
//            String jsonMessage = mapper.writeValueAsString(m);
//            // wrap into a proper message for the transport (Kafka/Rabbit) and send it
//            adaptationOut.send(
//                    MessageBuilder.withPayload(jsonMessage).build());
//        } catch (Exception e) {
//            throw new RuntimeException("Could not tranform and send message due to: "+ e.getMessage(), e);
//        }
//    }
//
//    public void sendToSystem(Message<?> m) {
//        try {
//            // avoid too much magic and transform ourselves
//            ObjectMapper mapper = new ObjectMapper();
//            String jsonMessage = mapper.writeValueAsString(m);
//            // wrap into a proper message for the transport (Kafka/Rabbit) and send it
//            adaptationOut.send(
//                    MessageBuilder.withPayload(jsonMessage).build());
//        } catch (Exception e) {
//            throw new RuntimeException("Could not tranform and send message due to: "+ e.getMessage(), e);
//        }
//    }

}
