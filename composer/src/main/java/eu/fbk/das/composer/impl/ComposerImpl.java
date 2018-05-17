package eu.fbk.das.composer.impl;

import eu.fbk.das.composer.Composer;
import eu.fbk.das.composer.CompositionStrategy;
import eu.fbk.das.composer.FragmentSelectionStrategy;
import eu.fbk.das.composer.message.MessageService;
import eu.fbk.das.domainobject.core.entity.ProcessDiagram;
import eu.fbk.das.domainobject.core.entity.activity.*;
import eu.fbk.das.domainobject.core.entity.jaxb.GoalType;
import eu.fbk.das.domainobject.core.entity.jaxb.activity.ClauseType;
import eu.fbk.das.domainobject.core.entity.jaxb.activity.EffectType;
import eu.fbk.das.domainobject.core.entity.jaxb.activity.PreconditionType;
import eu.fbk.das.domainobject.core.message.AdaptationProblem;
import eu.fbk.das.domainobject.core.persistence.model.DomainObjectModel;
import eu.fbk.das.domainobject.core.persistence.model.FragmentActionModel;
import eu.fbk.das.domainobject.core.service.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class ComposerImpl implements Composer {

    private static final Logger LOG = LoggerFactory.getLogger(ComposerImpl.class);

    private static final String  WINE_PATH = "/opt/local/bin/wine";

    private static final String WSYNTH_PATH = "/opt/local/bin/wsynth.exe";

    private ConcurrentHashMap<String, CompositionStrategy> strategies = new ConcurrentHashMap<>();

    public ComposerImpl() {
        strategies.put("fragmentSelection", new FragmentSelectionStrategy(this));
    }

    @Autowired
    MessageService messageService;

    @Autowired
    RepositoryService repositoryService;

    public ProcessDiagram submitProblem(AdaptationProblem ap) {
        //choose strategy
        CompositionStrategy strategy = getCompositionStrategy("fragmentSelection");
        return strategy.compose(ap);
    }

    public List<DomainObjectModel> findDomainObjectDefinitionWithGoalRelation(AdaptationProblem ap) {
        GoalType goal = ap.getGoal();
        List<DomainObjectModel> dods = new ArrayList<>();
        goal.getPoint().stream().forEach(p -> {
            p.getDomainProperty().stream().forEach(dp -> {
                String dpName = dp.getDpName();
                dods.addAll(repositoryService.findRelevantDoModels(dpName));
            });
        });
        return dods;
    }

    public ProcessDiagram convertToProcessDiagram(List<FragmentActionModel> actions) {
        List<ProcessActivity> pacts = new ArrayList<>();
        actions.forEach(a -> {
            FragmentActionModel fa = repositoryService.findFragmentActionById(a.getId());
            if (fa.getType().equals("input")) {
                InvokeActivty act = new InvokeActivty();
                act.setSend(true);
                act.setType(ProcessActivityType.INVOKE);
                act.setName(fa.getName());
                if (fa.getEffects() != null) {
                    EffectType et = new EffectType();
                    fa.getEffects().forEach(e -> {
                        EffectType.Event ev = new EffectType.Event();
                        ev.setDpName(e.getDomainPropertyName());
                        ev.setEventName(e.getEvent());
                        et.getEvent().add(ev);
                    });
                    act.setEffect(et);
                }
                if (fa.getPreconditions() != null) {
                    PreconditionType pt = new PreconditionType();
                    fa.getPreconditions().forEach(p -> {
                        ClauseType.Point.DomainProperty dp = new ClauseType.Point.DomainProperty();
                        dp.setDpName(p.getDomainPropertyName());
                        dp.getState().add(p.getState());
                        ClauseType.Point point = new ClauseType.Point();
                        point.getDomainProperty().add(dp);
                        pt.getPoint().add(point);
                    });
                    act.setPrecondition(pt);
                }
                pacts.add(act);
            } else if (fa.getType().equals("output")) {
                ReplyActivity act = new ReplyActivity();
                act.setReceive(true);
                act.setType(ProcessActivityType.REPLY);
                act.setName(fa.getName());
                if (fa.getEffects() != null) {
                    EffectType et = new EffectType();
                    fa.getEffects().forEach(e -> {
                        EffectType.Event ev = new EffectType.Event();
                        ev.setDpName(e.getDomainPropertyName());
                        ev.setEventName(e.getEvent());
                        et.getEvent().add(ev);
                    });
                    act.setEffect(et);
                }
                if (fa.getPreconditions() != null) {
                    PreconditionType pt = new PreconditionType();
                    fa.getPreconditions().forEach(p -> {
                        ClauseType.Point.DomainProperty dp = new ClauseType.Point.DomainProperty();
                        dp.setDpName(p.getDomainPropertyName());
                        dp.getState().add(p.getState());
                        ClauseType.Point point = new ClauseType.Point();
                        point.getDomainProperty().add(dp);
                        pt.getPoint().add(point);
                    });
                    act.setPrecondition(pt);
                }
                pacts.add(act);
            } else if (fa.getType().equals("concrete")) {
                ConcreteActivity act = new ConcreteActivity();
                act.setConcrete(true);
                act.setType(ProcessActivityType.CONCRETE);
                act.setName(fa.getName());
                if (fa.getEffects() != null) {
                    EffectType et = new EffectType();
                    fa.getEffects().forEach(e -> {
                        EffectType.Event ev = new EffectType.Event();
                        ev.setDpName(e.getDomainPropertyName());
                        ev.setEventName(e.getEvent());
                        et.getEvent().add(ev);
                    });
                    act.setEffect(et);
                }
                if (fa.getPreconditions() != null) {
                    PreconditionType pt = new PreconditionType();
                    fa.getPreconditions().forEach(p -> {
                        ClauseType.Point.DomainProperty dp = new ClauseType.Point.DomainProperty();
                        dp.setDpName(p.getDomainPropertyName());
                        dp.getState().add(p.getState());
                        ClauseType.Point point = new ClauseType.Point();
                        point.getDomainProperty().add(dp);
                        pt.getPoint().add(point);
                    });
                    act.setPrecondition(pt);
                }
                pacts.add(act);
            } else if (fa.getType().equals("abstract")) {
                AbstractActivity act = new AbstractActivity();
                act.setAbstract(true);
                act.setType(ProcessActivityType.ABSTRACT);
                act.setName(fa.getName());
                if (fa.getGoals() != null) {
                    GoalType goal = new GoalType();
                    fa.getGoals().forEach(g -> {
                        ClauseType.Point.DomainProperty dp = new ClauseType.Point.DomainProperty();
                        dp.setDpName(g.getDomainPropertyName());
                        dp.getState().add(g.getState());
                        ClauseType.Point point = new ClauseType.Point();
                        point.getDomainProperty().add(dp);
                        goal.getPoint().add(point);
                    });
                    act.setGoal(goal);
                }
                pacts.add(act);
            }
        });
        ProcessDiagram pd = new ProcessDiagram(pacts);
        return pd;
    }

    public CompositionStrategy getCompositionStrategy(String type) {
        return strategies.get(type);
    }

    @Override
    public RepositoryService getRepositoryService() {
        return repositoryService;
    }

}
