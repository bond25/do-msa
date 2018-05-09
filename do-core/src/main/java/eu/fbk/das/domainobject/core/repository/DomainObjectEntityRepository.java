package eu.fbk.das.domainobject.core.repository;

import eu.fbk.das.domainobject.core.persistence.execution.DomainObjectEntity;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface DomainObjectEntityRepository extends Neo4jRepository<DomainObjectEntity, Long> {

    @Query("match (dom:DomainObjectModel)-[:MODEL]-(:DeploymentEntity)-[:DEPLOYMENT]-(doi:DomainObjectEntity {correlationId: {1}}) WHERE dom.name in {0} return doi")
    List<DomainObjectEntity> findAllByCorrelationId(String[] doNames, String correlationId);

}