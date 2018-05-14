package eu.fbk.das.engine.message;

import eu.fbk.das.domainobject.core.message.AdaptationProblem;
import eu.fbk.das.domainobject.core.message.Message;

public interface MessagePublisher {

    void sendAdaptationProblem(Message<AdaptationProblem> m);

}
