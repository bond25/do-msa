package eu.fbk.das.composer;

import eu.fbk.das.domainobject.core.entity.ProcessDiagram;
import eu.fbk.das.domainobject.core.message.AdaptationProblem;
import eu.fbk.das.domainobject.core.message.Message;
import eu.fbk.das.domainobject.core.persistence.model.FragmentActionModel;
import eu.fbk.das.domainobject.core.service.RepositoryService;

import java.util.List;

public interface Composer {

    ProcessDiagram submitProblem(AdaptationProblem ap);

    ProcessDiagram convertToProcessDiagram(List<FragmentActionModel> actions);

    CompositionStrategy getCompositionStrategy(String type);

    RepositoryService getRepositoryService();

}
