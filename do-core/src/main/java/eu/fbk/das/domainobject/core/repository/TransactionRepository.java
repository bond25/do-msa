package eu.fbk.das.domainobject.core.repository;

import eu.fbk.das.domainobject.core.persistence.StateEntity;
import eu.fbk.das.domainobject.core.persistence.StateTransition;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface TransactionRepository extends Neo4jRepository<StateTransition, Long> {

    @Query("match path=shortestPath((istate:StateEntity)-[:TRANSITION *0..]-(fstate:StateEntity)) WHERE istate.isInitial = true and fstate.name = \"ASSISTANCE_COMPLETE\" return path")
    public List<StateEntity> findDp();

}
