package eu.fbk.das.domainobject.core.repository;

import eu.fbk.das.domainobject.core.persistence.model.FragmentActionModel;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface FragmentActionRepository extends Neo4jRepository<FragmentActionModel, Long> {
}
