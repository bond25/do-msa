package eu.fbk.das.engine.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.fbk.das.domainobject.core.message.AdaptationResult;
import eu.fbk.das.domainobject.core.message.ExecuteTask;
import eu.fbk.das.domainobject.core.message.Message;
import eu.fbk.das.domainobject.core.message.TaskExecuted;
import eu.fbk.das.engine.ProcessEngine;
import eu.fbk.das.engine.message.MessageSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@EnableBinding(Sink.class)
public class MessageSubscriberImpl implements MessageSubscriber {

    private static Logger LOG = LoggerFactory.getLogger(MessageSubscriberImpl.class);

    @Autowired
    MessagePublisherImpl messageServiceImpl;

    @Autowired
    ProcessEngine engine;

    @StreamListener(target = "input", condition = "headers['eventType']=='AdaptationResult'")
    @Transactional
    public void onAdaptationResult(String messageJson) throws IOException {
        Message<AdaptationResult> m = new ObjectMapper().readValue(messageJson, new TypeReference<Message<AdaptationResult>>(){});
        if (engine.canHandle(m.getDeploymentId())) {
            LOG.debug("Handle AdaptationResult {}", messageJson);
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //engine.injectAdaptationResult(m.getPayload());
        }
    }

    @StreamListener(target = "input", condition = "headers['eventType']=='ExecuteTask'")
    @Transactional
    public void onExecuteTask(String messageJson) throws IOException {
        Message<ExecuteTask> m = new ObjectMapper().readValue(messageJson, new TypeReference<Message<ExecuteTask>>(){});
        if (engine.canHandle(m.getPayload().getDeploymentId())) {
            LOG.debug("Handle ExecuteTask {}", messageJson);
            TaskExecuted message = engine.executeFragmentActivity(m);
        }
    }

    @StreamListener(target = "input", condition = "headers['eventType']=='TaskExecuted'")
    @Transactional
    public void onTaskEexecuted(String messageJson) throws IOException {
        Message<TaskExecuted> m = new ObjectMapper().readValue(messageJson, new TypeReference<Message<TaskExecuted>>(){});
        engine.correlateMessage(m.getPayload());
    }

    protected boolean canHandle() {
        return true;
    }

    @StreamListener(target = "input", condition = "headers['eventType']=='command'")
    @Transactional
    public void handleCommand(String messageJson) throws IOException {
        int num;
        List<String> command = new ArrayList<>();
        if (messageJson.contains(":")) {
            command = Arrays.stream(messageJson.split(":")).collect(Collectors.toList());
        } else {
            command.add(messageJson);
        }
        switch (command.get(0)) {
            case "createInstance":
                engine.startInstance();
                break;
            case "createInstances":
                num = Integer.parseInt(command.get(1));
                for (int i = 0; i < num; i++) {
                    engine.startInstanceByCorrelationId(UUID.randomUUID().toString());
                }
                break;
            case "deploy":
                num = Integer.parseInt(command.get(1));
                for (int i = 0; i < num; i++) {
                    engine.createDeployment();
                }
                break;
            case "step":
                if (command.size() == 2) {
                    if (engine.canHandle(command.get(1))) {
                        engine.stepAll();
                    }
                } else {
                    if (engine.canHandle(command.get(1)) && engine.canHandleInstance(command.get(2))) {
                        engine.step(command.get(2));
                    }
                }
                break;
        }
        LOG.debug("Command {} executed", messageJson);
    }
}