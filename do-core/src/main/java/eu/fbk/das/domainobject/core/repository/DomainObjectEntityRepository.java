package eu.fbk.das.domainobject.core.repository;

import eu.fbk.das.domainobject.core.persistence.execution.DomainObjectEntity;
import eu.fbk.das.domainobject.core.persistence.execution.DomainPropertyEntity;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface DomainObjectEntityRepository extends Neo4jRepository<DomainObjectEntity, Long> {

    @Query("match (dom:DomainObjectModel)-[:MODEL]-(:DeploymentEntity)-[:DEPLOYMENT]-(doi:DomainObjectEntity {correlationId: {1}}) WHERE dom.name in {0} return doi")
    List<DomainObjectEntity> findAllByCorrelationId(String[] doNames, String correlationId);

    @Query("match (dom:DomainObjectModel)-[:MODEL]-(:DeploymentEntity)-[:DEPLOYMENT]-(doi:DomainObjectEntity)-[:PROPERTY]-(dp:DomainPropertyEntity) where ID(dom)={0} and doi.correlationId = {1} and dp.name = {2} return dp")
    List<DomainPropertyEntity> getDomainPropertyInstance(Long domId, String correlationId, String dpName);

    @Query("MATCH (doe:DomainObjectEntity)-[:PROPERTY]->(dp:DomainPropertyEntity)\n" +
            "WHERE doe.correlationId = {0}\n" +
            "AND dp.name = {1}\n" +
            "RETURN dp.currentState")
    String getRuntimeState(String correlationId, String domainProperty);

}
