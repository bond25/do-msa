package eu.fbk.das.domainobject.core.service;

import eu.fbk.das.domainobject.core.entity.DomainObjectDefinition;
import eu.fbk.das.domainobject.core.entity.DomainObjectInstance;
import eu.fbk.das.domainobject.core.persistence.*;
import eu.fbk.das.domainobject.core.persistence.execution.DeploymentEntity;
import eu.fbk.das.domainobject.core.persistence.model.DomainObjectModel;
import eu.fbk.das.domainobject.core.persistence.model.DomainPropertyModel;
import eu.fbk.das.domainobject.core.persistence.model.FragmentActionModel;
import eu.fbk.das.domainobject.core.persistence.model.FragmentModel;
import eu.fbk.das.domainobject.core.persistence.execution.DomainObjectEntity;
import eu.fbk.das.domainobject.core.persistence.execution.DomainPropertyEntity;
import eu.fbk.das.domainobject.core.persistence.execution.ProcessEntity;
import eu.fbk.das.domainobject.core.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RepositoryService {

    private static Logger LOG = LoggerFactory.getLogger(RepositoryService.class);

    @Autowired
    DomainObjectModelRepository domRepo;

    @Autowired
    TransactionRepository tranRepo;

    @Autowired
    DpTransitionRepositry dpTranRepo;

    @Autowired
    ExecutionRepository execRepo;

    @Autowired
    FragmentRepository fragmentRepo;

    @Autowired
    FragmentActionRepository fragmentActionRepo;

    @Autowired
    DeploymentRepository deploymentRepo;

    @Autowired
    DomainObjectEntityRepository runtimeRepo;

    @Transactional
    public List<DomainObjectEntity> getRuntimeData(String[] doNames, String correlationId) {
        return runtimeRepo.findAllByCorrelationId(doNames, correlationId);
    }

    @Transactional
    public DomainObjectModel save(DomainObjectModel dod) {
        return domRepo.save(dod);
    }

    @Transactional
    public DomainObjectModel findByName(String name) {
        return domRepo.findByTitle(name);
    }

    @Transactional
    public DomainObjectModel findByDeploymentId(String deploymentId) {
        DomainObjectModel dod = domRepo.findByDeploymentId(deploymentId);
        return domRepo.findById(dod.getId()).orElse(null);
    }

    @Transactional
    public String findEvent(String state) {
        return domRepo.findEvent(state);
    }

    @Transactional
    public FragmentModel findFragmentByName(String name) {
        return fragmentRepo.findByTitle(name);
    }

    @Transactional
    public List<FragmentModel> findRelevantFragments(String dodName, String event, String dpName) {
        return fragmentRepo.findRelevantFragments(dodName, event, dpName);
    }

    @Transactional
    public List<FragmentActionModel> findActionFlowByFragmentName(String name) {
        return fragmentRepo.findActionFlowByFragmentName(name);
    }

    @Transactional
    public FragmentActionModel findFragmentActionById(Long id) {
        return fragmentActionRepo.findById(id).orElse(null);
    }

    @Transactional
    public DomainObjectEntity createInstance(DeploymentEntity de, String correlationId, DomainObjectInstance doi) {
        DomainObjectEntity doe = new DomainObjectEntity();
        List<DomainPropertyEntity> properties = new ArrayList<>();
        ProcessEntity p = new ProcessEntity();
        doi.getInternalKnowledge().forEach(dp -> {
            DomainPropertyEntity d = new DomainPropertyEntity();
            d.setName(dp.getOid())
                    .setType(dp.getType())
                    .setCurrentState(dp.getCurrentState());
            properties.add(d);
        });
        p.setName(doi.getProcess().getName())
                .setCurrentActivity(doi.getProcess().getCurrentActivity().getName())
                .setIsEnded(doi.getProcess().isEnded());
        doe.setCorrelationId(correlationId);
        doe.setDeployment(de);
        doe.setProcess(p);
        doe.setProperties(properties);
        execRepo.save(doe);
        return doe;
    }

    @Transactional
    public List<DomainObjectModel> findRelevantDoModels(String name) {
        return domRepo.findRelevantDoModels(name);
    }

    //TODO: maybe decompose method
    @Transactional
    public DomainObjectModel createDoModel(DomainObjectDefinition dod) {
        LOG.debug("Create db model for {}", dod);
        DomainObjectModel dode = new DomainObjectModel();
        List<FragmentModel> fragments = new ArrayList<>();
        List<StateTransition> transitions = new ArrayList<>();
        dod.getFragments().forEach(f -> {
            List<StateEntity> fStates = new ArrayList<>();
            FragmentModel fdb = new FragmentModel();
            Map<String, StateEntity> states = f.getState().stream().map(s -> {
                StateEntity se = new StateEntity(s.getName());
                if (s.isIsInitial() != null) {
                    se.setIsInitial(true);
                }
                fStates.add(se);
                return se;
            }).collect(Collectors.toMap(s -> s.getName(), s -> s));
            transitions.addAll(f.getTransition().stream().map(t -> {
                StateTransition tran = new StateTransition(t.getAction().getName(), t.getAction().getType(), states.get(t.getInitialState().getValue()), states.get(t.getFinalState().getValue()));
                return tran;
            }).collect(Collectors.toList()));
            List<FragmentActionModel> acts = f.getAction().stream().map(a -> {
                FragmentActionModel adef = new FragmentActionModel();
                adef.setName(a.getName())
                        .setType(a.getActionType().value());
                if (a.getGoal() != null) {
                    List<GoalEntity> gl = new ArrayList<>();
                    a.getGoal().getPoint().forEach(p -> {
                                List<GoalEntity> tmp = p.getDomainProperty().stream()
                                        .map(dp -> {
                                            return new GoalEntity()
                                                    .setDomainPropertyName(dp.getDpName())
                                                    .setState(dp.getState().get(0));
                                        }).collect(Collectors.toList());
                                gl.addAll(tmp);
                            });
                    adef.setGoals(gl);
                }
                if (a.getPrecondition() != null) {
                    List<PreconditionEntity> pl = new ArrayList<>();
                            a.getPrecondition().getPoint().forEach(p -> {
                                List<PreconditionEntity> tmp = p.getDomainProperty().stream().
                                        map(dp -> {
                                            return new PreconditionEntity().setDomainPropertyName(dp.getDpName())
                                                    .setState(dp.getState().get(0));}).collect(Collectors.toList());
                                pl.addAll(tmp);
                            });
                    adef.setPreconditions(pl);
                }
                if (a.getEffect() != null) {
                    List<EffectEntity> el = a.getEffect().getEvent().stream()
                            .map(e -> {
                                return new EffectEntity()
                                        .setDomainPropertyName(e.getDpName())
                                        .setEvent(e.getEventName());
                            }).collect(Collectors.toList());
                    adef.setEffects(el);
                }
                return adef;
            }).collect(Collectors.toList());
            for (int i = 0; i < acts.size() - 1; i++) {
                acts.get(i).setNextAction(acts.get(i + 1));
            }
            fragments.add(fdb.setStates(fStates).setName(f.getId()).setStartActivity(acts.get(0)));
        });

        List<DomainPropertyModel> dpdefs = new ArrayList<>();
        List<DomainPropertyTransition> trans = new ArrayList<>();
        dod.getProperties().forEach(dp -> {
            List<StateEntity> fStates = new ArrayList<>();
            Map<String, StateEntity> states = dp.getState().stream().map(s -> {
                StateEntity se = new StateEntity(s.getValue());
                if (s.isIsInitial() != null) {
                    se.setIsInitial(true);
                }
                fStates.add(se);
                return  se;
            }).collect(Collectors.toMap(s -> s.getName(), s -> s));
            dp.getTransition().forEach(t -> trans.add(new DomainPropertyTransition(t.getEvent().getValue(), states.get(t.getFrom().getValue()), states.get(t.getTo().getValue()))));
            DomainPropertyModel dpe = new DomainPropertyModel();
            dpe.setName(dp.getId());
            dpe.setStates(fStates);
            dpdefs.add(dpe);
        });
        dode.setProperties(dpdefs);
        dode.setFragmentDefEntities(fragments);
        dode.setName(dod.getDomainObject().getName());
        DomainObjectModel createdDod = domRepo.save(dode);
        LOG.debug("Created Domain Object Definition {}", createdDod.getId());
        tranRepo.saveAll(transitions);
        dpTranRepo.saveAll(trans);
        return createdDod;
    }

    @Transactional
    public DeploymentEntity createDeployment(DomainObjectModel dom, String deploymentId) {
        DeploymentEntity deployment = new DeploymentEntity();
        deployment.setDeploymentId(deploymentId)
                .setDoModel(dom);
        deploymentRepo.save(deployment);
        return deployment;
    }
}