package eu.fbk.das.composer.message;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.fbk.das.composer.Composer;
import eu.fbk.das.domainobject.core.entity.ProcessDiagram;
import eu.fbk.das.domainobject.core.message.AdaptationProblem;
import eu.fbk.das.domainobject.core.message.AdaptationResult;
import eu.fbk.das.domainobject.core.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Component
@EnableBinding(Sink.class)
public class MessageListener {

    @Autowired
    Composer composer;

    @Autowired
    MessageService messageService;

    private static Logger LOG = LoggerFactory.getLogger(MessageListener.class);

    @StreamListener(target = "input", condition = "headers['eventType']=='AdaptationProblem'")
    @Transactional
    public void handleAdaptationProblem(String messageJson) throws IOException {
        Message<AdaptationProblem> m = new ObjectMapper().readValue(messageJson, new TypeReference<Message<AdaptationProblem>>(){});
        long startTime = System.currentTimeMillis();
        ProcessDiagram result = composer.submitProblem(m.getPayload());
        long endTime = System.currentTimeMillis();
        LOG.info("Handle AP {} in {} ms", m.getId(), (endTime - startTime));
        //messageService.sendAdaptationResult(new Message<AdaptationResult>(m.getDeploymentId(), m.getCorrelationId(), new AdaptationResult(result).setCorrelationId(m.getCorrelationId())));
    }

}