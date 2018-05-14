package eu.fbk.das.composer;

import eu.fbk.das.domainobject.core.entity.ProcessDiagram;
import eu.fbk.das.domainobject.core.message.AdaptationProblem;
import eu.fbk.das.domainobject.core.persistence.StateEntity;
import eu.fbk.das.domainobject.core.persistence.execution.DomainPropertyEntity;
import eu.fbk.das.domainobject.core.persistence.model.DomainObjectModel;
import eu.fbk.das.domainobject.core.persistence.model.FragmentModel;

import java.util.List;
import java.util.Random;

public class FragmentSelectionStrategy implements CompositionStrategy {

    private Composer composerCtx;

    public FragmentSelectionStrategy(Composer composerCtx) {
        this.composerCtx = composerCtx;
    }

    public ProcessDiagram compose(String dpName, String state) {
        List<DomainObjectModel> doms = composerCtx.getRepositoryService().findRelevantDoModels(dpName);
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
        //runtime data
        List<DomainPropertyEntity> dps = composerCtx.getRepositoryService().getDomainPropertyInstance(dom.getId(), "c6702b13-73bc-4fe2-9d09-67d33923bbc0", dpName);
        List<StateEntity> initStates = composerCtx.getRepositoryService().findInitialStates(dom.getId(), dpName);
        Long[] states = new Long[initStates.size()];
        initStates.forEach(s -> states[states.length - 1] = s.getId());
        List<StateEntity> statesPath = composerCtx.getRepositoryService().findDomainPropertyPath(states, state);
        if (statesPath != null) {
            String event = composerCtx.getRepositoryService().findEvent(dom.getId(), statesPath.get(statesPath.size() - 1).getName());
            List<FragmentModel> fragments = composerCtx.getRepositoryService().findRelevantFragments(dom.getId(), event, dpName);
            if (fragments != null) {
                return composerCtx.convertToProcessDiagram(fragments.get(0).getId());
            }
        }
        return null;
    }


    @Override
    public ProcessDiagram compose(AdaptationProblem ap) {
        String dpName = ap.getGoal().getPoint().get(0).getDomainProperty().get(0).getDpName();
        String state = ap.getGoal().getPoint().get(0).getDomainProperty().get(0).getState().get(0);

        List<DomainObjectModel> doms = composerCtx.getRepositoryService().findRelevantDoModels(dpName);
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
        //runtime data
        List<DomainPropertyEntity> dps = composerCtx.getRepositoryService().getDomainPropertyInstance(dom.getId(), "c6702b13-73bc-4fe2-9d09-67d33923bbc0", dpName);
        List<StateEntity> initStates = composerCtx.getRepositoryService().findInitialStates(dom.getId(), dpName);
        Long[] states = new Long[initStates.size()];
        initStates.forEach(s -> states[states.length - 1] = s.getId());
        List<StateEntity> statesPath = composerCtx.getRepositoryService().findDomainPropertyPath(states, state);
        if (statesPath != null) {
            String event = composerCtx.getRepositoryService().findEvent(dom.getId(), statesPath.get(statesPath.size() - 1).getName());
            List<FragmentModel> fragments = composerCtx.getRepositoryService().findRelevantFragments(dom.getId(), event, dpName);
            if (fragments != null) {
                return composerCtx.convertToProcessDiagram(fragments.get(0).getId());
            }
        }
        return null;
    }

}
