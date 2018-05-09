package eu.fbk.das.composer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@ComponentScan("eu.fbk.das")
@EnableNeo4jRepositories("eu.fbk.das.domainobject.core")
@EntityScan("eu.fbk.das.domainobject.core")
public class ComposerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComposerApplication.class, args);
	}

}
