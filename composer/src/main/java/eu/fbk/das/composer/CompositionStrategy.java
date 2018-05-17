package eu.fbk.das.composer;

import eu.fbk.das.domainobject.core.entity.ProcessDiagram;
import eu.fbk.das.domainobject.core.message.AdaptationProblem;

public interface CompositionStrategy {

    ProcessDiagram compose(AdaptationProblem ap);

    ProcessDiagram compose(String domainProperty, String finalState);
}
