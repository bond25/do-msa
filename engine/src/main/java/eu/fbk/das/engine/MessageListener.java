package eu.fbk.das.engine;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.fbk.das.domainobject.core.message.AdaptationResult;
import eu.fbk.das.domainobject.core.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;

@Component
@EnableBinding(Sink.class)
public class MessageListener {

    private static Logger LOG = LoggerFactory.getLogger(MessageListener.class);

    @Autowired
    MessageService messageService;

    @Autowired
    ProcessEngine processEngine;

    @StreamListener(target = "input", condition = "headers['eventType']=='AdaptationResult'")
    @Transactional
    public void getMsg(String messageJson) throws IOException {
        Message<AdaptationResult> m = new ObjectMapper().readValue(messageJson, new TypeReference<Message<AdaptationResult>>(){});
        if (processEngine.canHandleInstance(m.getCorrelationId())) {
            LOG.debug("Receive message {}", messageJson);
        }
    }

    @StreamListener(target = "input", condition = "headers['eventType']=='ExecuteTask'")
    @Transactional
    public void handleExecuteTaskCmd(String messageJson) {
        LOG.debug("Handle `ExecuteTaskCmd` {}", messageJson);
    }

    protected boolean canHandle() {
        return true;
    }

    @StreamListener(target = "input", condition = "headers['eventType']=='command'")
    @Transactional
    public void handleCommand(String messageJson) throws IOException {
        String[] command = messageJson.split(":");
        if (command[0].equals("createInstances")) {
            int num = Integer.parseInt(command[1]);
            for (int i = 0; i < num; i++) {
                processEngine.startInstanceByCorrelationId(UUID.randomUUID().toString());
            }
        }
        if (command[0].equals("deploy")) {
            int num = Integer.parseInt(command[1]);
            for (int i = 0; i < num; i++) {
                processEngine.createDeployment();
            }
        }
        LOG.debug("Command {} executed", messageJson);
    }
}