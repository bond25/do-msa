package eu.fbk.das.composer;

import eu.fbk.das.domainobject.core.entity.ProcessDiagram;
import eu.fbk.das.domainobject.core.entity.jaxb.DomainProperty;
import eu.fbk.das.domainobject.core.entity.jaxb.Fragment;
import eu.fbk.das.domainobject.core.persistence.DomainPropertyTransition;
import eu.fbk.das.domainobject.core.persistence.EffectEntity;
import eu.fbk.das.domainobject.core.persistence.StateEntity;
import eu.fbk.das.domainobject.core.persistence.StateTransition;
import eu.fbk.das.domainobject.core.persistence.model.DomainObjectModel;
import eu.fbk.das.domainobject.core.persistence.model.DomainPropertyModel;
import eu.fbk.das.domainobject.core.persistence.model.FragmentActionModel;
import eu.fbk.das.domainobject.core.persistence.model.FragmentModel;
import eu.fbk.das.domainobject.core.repository.DomainObjectModelRepository;
import eu.fbk.das.domainobject.core.repository.DpTransitionRepositry;
import eu.fbk.das.domainobject.core.service.RepositoryService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan("eu.fbk.das")
@EnableNeo4jRepositories("eu.fbk.das.domainobject.core")
@EntityScan("eu.fbk.das.domainobject.core")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreateDom {

    private static Logger LOG = LoggerFactory.getLogger(CreateDom.class);

    @Autowired
    Composer composer;

    @Autowired
    RepositoryService repo;

    @Autowired
    DpTransitionRepositry dpTranRepo;

    @Autowired
    DomainObjectModelRepository domRepo;

    private static Map<String, Map<String, String>> repos = new HashMap<>();
    private static List<String> domNames = new ArrayList<>();

    private static List<Long> results = new ArrayList<>();

    @Test
    public void testB() {
        domNames.forEach(dom -> {
            Map<String, String> data = repos.get(dom);
            String domainProperty = data.get("domainProperty");
            String finalState = data.get("finalState");
            String event = data.get("event");
            Long avgTime = Long.valueOf(0);
            for (int i = 0; i < 10; i++) {
                Long start = System.currentTimeMillis();
                ProcessDiagram result = composer.getCompositionStrategy("fragmentSelection").compose(domainProperty, finalState);
                Long end = System.currentTimeMillis();
                avgTime += end - start;
                Assert.assertNotNull(result);
            }
            results.add(avgTime / 10);
            System.out.println(results);
        });
    }

    @After
    public void printTestResults() {
        LOG.debug("Test resulsts {}", results);
    }

    @Test
    public void testA() {

        for (int i = 3; i < 100; i++) {
            String name = UUID.randomUUID().toString();
            DomainObjectModel dom = new DomainObjectModel();
            dom.setName(name);
            domNames.add(name);
            repos.put(name, new HashMap<>());

            String dpName = UUID.randomUUID().toString();
            DomainPropertyModel dp = new DomainPropertyModel();
            dp.setName(dpName);
            repos.get(name).put("domainProperty", dpName);

            List<StateEntity> states = new ArrayList<>();
            for (int s = 0; s < i; s++) {
                String sname = RandomStringUtils.randomAlphabetic(10);
                if (s == 0) {
                    StateEntity state = new StateEntity("INITIAL");
                    state.setIsInitial(true);
                    states.add(state);
                }
                if (s == i - 1) {
                    repos.get(name).put("finalState", sname);
                    StateEntity state = new StateEntity(sname);
                    states.add(state);
                } else {
                    StateEntity state = new StateEntity(sname);
                    states.add(state);
                }
            }

            dp.setStates(states);
            List<DomainPropertyModel> dps = new ArrayList<>();
            dps.add(dp);
            dom.setProperties(dps);

            List<DomainPropertyTransition> transitions = new ArrayList<>();

            for (int s = 0; s < states.size() - 1; s ++) {
                String aname = RandomStringUtils.randomAlphabetic(15);
                DomainPropertyTransition tran = new DomainPropertyTransition(aname, states.get(s), states.get(s + 1));
                transitions.add(tran);
                if (s == states.size() - 2) {
                    repos.get(name).put("event", aname);
                }
            }

            List<FragmentModel> fragments = new ArrayList<>();
            FragmentModel frag = new FragmentModel();
            fragments.add(frag);

            String startact = RandomStringUtils.randomAlphabetic(10);
            FragmentActionModel start = new FragmentActionModel(startact, "concrete");
            frag.setStartActivity(start);

            FragmentActionModel current = start;
            for (int k = 0; k < i; k++) {
                String faname = RandomStringUtils.randomAlphabetic(10);
                FragmentActionModel tmp = new FragmentActionModel(faname, "concrete");
                current.setNextAction(tmp);
                current = tmp;
            }

            String endact = RandomStringUtils.randomAlphabetic(10);
            FragmentActionModel end = new FragmentActionModel(endact, "concrete");
            current.setNextAction(end);

            EffectEntity effect = new EffectEntity().setDomainPropertyName(dpName).setEvent(repos.get(name).get("event"));
            List<EffectEntity> effects = new ArrayList<>();
            effects.add(effect);
            end.setEffects(effects);

            dom.setFragmentDefEntities(fragments);

            domRepo.save(dom);
            dpTranRepo.saveAll(transitions);
        }

    }

}
