package eu.fbk.das.composer;

import eu.fbk.das.domainobject.core.entity.ProcessDiagram;
import eu.fbk.das.domainobject.core.message.AdaptationProblem;
import eu.fbk.das.domainobject.core.message.Message;

public interface Composer {

    ProcessDiagram submitProblem(AdaptationProblem ap,  String correlationId);

}
