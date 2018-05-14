package eu.fbk.das.composer;

import eu.fbk.das.domainobject.core.entity.ProcessDiagram;
import eu.fbk.das.domainobject.core.message.AdaptationProblem;
import eu.fbk.das.domainobject.core.message.Message;
import eu.fbk.das.domainobject.core.service.RepositoryService;

public interface Composer {

    ProcessDiagram submitProblem(AdaptationProblem ap);

    ProcessDiagram convertToProcessDiagram(Long fragId);

    CompositionStrategy getCompositionStrategy(String type);

    RepositoryService getRepositoryService();

}
