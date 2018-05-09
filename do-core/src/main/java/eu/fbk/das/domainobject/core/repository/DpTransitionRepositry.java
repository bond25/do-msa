package eu.fbk.das.domainobject.core.repository;

import eu.fbk.das.domainobject.core.persistence.DomainPropertyTransition;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface DpTransitionRepositry extends Neo4jRepository<DomainPropertyTransition, Long> {
}
