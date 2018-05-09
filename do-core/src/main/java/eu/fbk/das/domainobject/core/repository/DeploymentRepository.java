package eu.fbk.das.domainobject.core.repository;

import eu.fbk.das.domainobject.core.persistence.execution.DeploymentEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface DeploymentRepository extends Neo4jRepository<DeploymentEntity, Long> {
}
