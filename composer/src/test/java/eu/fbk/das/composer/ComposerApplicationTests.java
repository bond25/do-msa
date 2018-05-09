package eu.fbk.das.composer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ComposerApplicationTests.class)
@EnableNeo4jRepositories("eu.fbk.das.domainobject.core")
@EntityScan("eu.fbk.das.domainobject.core")
@ComponentScan("eu.fbk.das.composer")
public class ComposerApplicationTests {

	@Autowired
	Composer composer;


	@Test
	public void buildCompositionProblem() {

		System.out.println("Some text");

	}

}
