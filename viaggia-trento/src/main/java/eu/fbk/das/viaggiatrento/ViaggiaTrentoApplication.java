package eu.fbk.das.viaggiatrento;

import eu.fbk.das.engine.ProcessEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@ComponentScan("eu.fbk.das")
@EnableNeo4jRepositories("eu.fbk.das.domainobject.core")
@EntityScan("eu.fbk.das.domainobject.core")
public class ViaggiaTrentoApplication {

	private static Logger LOG = LoggerFactory.getLogger(ViaggiaTrentoApplication.class);

	@Autowired
	ProcessEngine engine;

	public static void main(String[] args) {
		SpringApplication.run(ViaggiaTrentoApplication.class, args);
	}

	@Bean
	public String start() {
		LOG.debug("Deploy Process Engine with deploymentId {}", engine.getDeploymentId());
		engine.deploy(this.getClass().getPackageName());
//        for (int i = 0; i < 100; i++) {
//            engine.startInstanceByCorrelationId(ids[i]);
//        }
		return new String("start");
	}
}
