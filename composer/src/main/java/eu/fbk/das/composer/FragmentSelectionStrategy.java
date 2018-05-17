package eu.fbk.das.composer;

import eu.fbk.das.domainobject.core.entity.ProcessDiagram;
import eu.fbk.das.domainobject.core.message.AdaptationProblem;
import eu.fbk.das.domainobject.core.persistence.StateEntity;
import eu.fbk.das.domainobject.core.persistence.execution.DomainPropertyEntity;
import eu.fbk.das.domainobject.core.persistence.model.DomainObjectModel;
import eu.fbk.das.domainobject.core.persistence.model.FragmentActionModel;
import eu.fbk.das.domainobject.core.persistence.model.FragmentModel;

import java.util.List;
import java.util.Random;

public class FragmentSelectionStrategy implements CompositionStrategy {

    private Composer composerCtx;

    public FragmentSelectionStrategy(Composer composerCtx) {
        this.composerCtx = composerCtx;
    }

    public ProcessDiagram compose(String domainProperty, String finalState) {
        //runtime data
        String initState = composerCtx.getRepositoryService().getRuntimeState("none", domainProperty);

        List<DomainObjectModel> doms = null;

        if (finalState.equals("LEG_REFINED")) {
            doms = composerCtx.getRepositoryService().findRelevantDomFromState(domainProperty, finalState, "JOURNEY_PLANNED");
        } else {
            doms = composerCtx.getRepositoryService().findRelevantDomFromInitState(domainProperty, finalState);
        }

        if (doms.isEmpty()) {
            return null;
        }
        //in future can include some metrics (e.g. QoS)
        DomainObjectModel dom = null;
        if (doms.size() > 1) {
            Random r = new Random();
            int i = r.ints(0, doms.size()).findFirst().getAsInt();
            dom = doms.get(i);
        } else {
            dom = doms.get(0);
        }
        List<FragmentActionModel> activities = composerCtx.getRepositoryService().findFragmentActionFlow(dom.getId(), domainProperty, finalState);
        return composerCtx.convertToProcessDiagram(activities);
    }


    @Override
    public ProcessDiagram compose(AdaptationProblem ap) {
        String domainProperty = ap.getGoal().getPoint().get(0).getDomainProperty().get(0).getDpName();
        String finalState = ap.getGoal().getPoint().get(0).getDomainProperty().get(0).getState().get(0);

        //runtime data
        String initState = composerCtx.getRepositoryService().getRuntimeState("none", domainProperty);

        List<DomainObjectModel> doms = composerCtx.getRepositoryService().findRelevantDomFromInitState(domainProperty, finalState);
        if (doms.isEmpty()) {
            return null;
        }
        //in future can include some metrics (e.g. QoS)
        DomainObjectModel dom = null;
        if (doms.size() > 1) {
            Random r = new Random();
            int i = r.ints(0, doms.size()).findFirst().getAsInt();
            dom = doms.get(i);
        } else {
            dom = doms.get(0);
        }
        List<FragmentActionModel> activities = composerCtx.getRepositoryService().findFragmentActionFlow(dom.getId(), domainProperty, finalState);
        return composerCtx.convertToProcessDiagram(activities);
    }

}
