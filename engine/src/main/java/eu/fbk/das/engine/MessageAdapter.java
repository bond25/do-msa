package eu.fbk.das.engine;


import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MessageAdapter {

    @Output("adaptation")
    MessageChannel adaptation();

    @Output("execution")
    MessageChannel execution();

}
