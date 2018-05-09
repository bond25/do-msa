package eu.fbk.das.domainobject.core.repository;

import eu.fbk.das.domainobject.core.persistence.execution.DomainObjectEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ExecutionRepository extends Neo4jRepository<DomainObjectEntity, Long> {
}
