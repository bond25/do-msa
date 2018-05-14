package eu.fbk.das.composer;

import eu.fbk.das.domainobject.core.entity.ProcessDiagram;
import eu.fbk.das.domainobject.core.service.RepositoryService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class ComposerApplicationTests {

    private static Logger LOG = LoggerFactory.getLogger(ComposerApplicationTests.class);

    private static List<Long> results = new ArrayList<>();

	@Autowired
	Composer composer;

	@Autowired
    RepositoryService repo;

    @Test
    public void testTravelAssistant() {
        Long avgTime = Long.valueOf(0);
        for (int i = 0; i < 10; i++) {
            Long start = System.currentTimeMillis();
            ProcessDiagram res = composer.getCompositionStrategy("fragmentSelection").compose(goals[0][0], goals[0][1]);
            Long end = System.currentTimeMillis();
            avgTime += end - start;
            Assert.assertNotNull(res);
        }
        results.add(avgTime / 10);
        LOG.debug("testTravelAssistant avgTime = {} ms", avgTime / 10);
    }

    @Test
    public void testLocalPlanner() {
        Long avgTime = Long.valueOf(0);
        for (int i = 0; i < 10; i++) {
            Long start = System.currentTimeMillis();
            ProcessDiagram res = composer.getCompositionStrategy("fragmentSelection").compose(goals[1][0], goals[1][1]);
            Long end = System.currentTimeMillis();
            avgTime += end - start;
            Assert.assertNotNull(res);
        }
        results.add(avgTime / 10);
        LOG.debug("testLocalPlanner avgTime = {} ms", avgTime / 10);
    }

    @Test
    public void testGlobalPlanner() {
        Long avgTime = Long.valueOf(0);
        for (int i = 0; i < 10; i++) {
            Long start = System.currentTimeMillis();
            ProcessDiagram res = composer.getCompositionStrategy("fragmentSelection").compose(goals[2][0], goals[2][1]);
            Long end = System.currentTimeMillis();
            avgTime += end - start;
            Assert.assertNotNull(res);
        }
        results.add(avgTime / 10);
        LOG.debug("testGlobalPlanner avgTime = {} ms", avgTime / 10);
    }

    @Test
    public void testDataViewer() {
        Long avgTime = Long.valueOf(0);
        for (int i = 0; i < 10; i++) {
            Long start = System.currentTimeMillis();
            ProcessDiagram res = composer.getCompositionStrategy("fragmentSelection").compose(goals[3][0], goals[3][1]);
            Long end = System.currentTimeMillis();
            avgTime += end - start;
            Assert.assertNotNull(res);
        }
        results.add(avgTime / 10);
        LOG.debug("testDataViewer avgTime = {} ms", avgTime / 10);
    }

    @Test
    public void testRideSharing() {
        Long avgTime = Long.valueOf(0);
        for (int i = 0; i < 10; i++) {
            Long start = System.currentTimeMillis();
            ProcessDiagram res = composer.getCompositionStrategy("fragmentSelection").compose(goals[4][0], goals[4][1]);
            Long end = System.currentTimeMillis();
            avgTime += end - start;
            Assert.assertNotNull(res);
        }
        results.add(avgTime / 10);
        LOG.debug("testRideSharing avgTime = {} ms", avgTime / 10);
    }

    @Test
    public void testSpecificJourney() {
        Long avgTime = Long.valueOf(0);
        for (int i = 0; i < 10; i++) {
            Long start = System.currentTimeMillis();
            ProcessDiagram res = composer.getCompositionStrategy("fragmentSelection").compose(goals[5][0], goals[5][1]);
            Long end = System.currentTimeMillis();
            avgTime += end - start;
            Assert.assertNotNull(res);
        }
        results.add(avgTime / 10);
        LOG.debug("testSpecificJourneyDiscard avgTime = {} ms", avgTime / 10);
    }

    @Test
    public void testLegRefine() {
        Long avgTime = Long.valueOf(0);
        for (int i = 0; i < 10; i++) {
            Long start = System.currentTimeMillis();
            ProcessDiagram res = composer.getCompositionStrategy("fragmentSelection").compose(goals[6][0], goals[6][1]);
            Long end = System.currentTimeMillis();
            avgTime += end - start;
            Assert.assertNotNull(res);
        }
        results.add(avgTime / 10);
        LOG.debug("testLegRefine avgTime = {} ms", avgTime / 10);
    }

    @Test
    public void testLegRefineMock() {
        Long avgTime = Long.valueOf(0);
        for (int i = 0; i < 10; i++) {
            Long start = System.currentTimeMillis();
            ProcessDiagram res = composer.getCompositionStrategy("fragmentSelection").compose(goals[6][0], goals[6][1]);
            Long end = System.currentTimeMillis();
            avgTime += end - start;
            Assert.assertNotNull(res);
        }
        results.add(avgTime / 10);
        LOG.debug("testLegRefineMock avgTime = {} ms", avgTime / 10);
    }

    @After
    public void printTestResults() {
        LOG.debug("Test resulsts {}", results);
    }

    private final String[][] goals = {
            {"TravelAssistant", "ASSISTANCE_COMPLETE"},
            {"LocalPlanner", "LOCAL_ALTERNATIVES_SENT"},
            {"GlobalPlanner", "ALTERNATIVES_SENT"},
            {"DataViewer", "VIEWER_PATTERN_DEFINED"},
            {"RideSharing", "AVAILABLE_PASSAGES_SENT"},
            {"SpecificJourney", "AVAILABLE_ROUTES_SENT"},
            {"TravelAssistant", "LEG_REFINED"}
    };

}

