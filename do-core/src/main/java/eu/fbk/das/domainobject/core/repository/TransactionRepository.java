package eu.fbk.das.domainobject.core.repository;

import eu.fbk.das.domainobject.core.persistence.StateTransition;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface TransactionRepository extends Neo4jRepository<StateTransition, Long> {


}
