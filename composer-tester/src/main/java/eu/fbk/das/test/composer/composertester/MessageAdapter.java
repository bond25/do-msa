package eu.fbk.das.test.composer.composertester;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MessageAdapter {

    @Output("adaptation")
    MessageChannel adaptation();

    @Output("execution")
    MessageChannel execution();

}

