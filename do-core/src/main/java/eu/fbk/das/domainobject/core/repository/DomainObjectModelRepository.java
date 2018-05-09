package eu.fbk.das.domainobject.core.repository;

import eu.fbk.das.domainobject.core.persistence.model.DomainObjectModel;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;


public interface DomainObjectModelRepository extends Neo4jRepository<DomainObjectModel, Long> {

    DomainObjectModel findByTitle(String title);

    @Query("MATCH (dod: DomainObjectModel {deploymentId: {0}}) RETURN dod")
    DomainObjectModel findByDeploymentId(String deploymentId);

    @Query("MATCH (dod: DomainObjectModel)-[INTERNAL_PROPERTY]->(dp: DomainPropertyModel {name: {0}}) return dod")
    List<DomainObjectModel> findRelevantDoModels(String name);

    @Query("MATCH (:StateEntity)-[t:TRANSITION]->(s:StateEntity {name: {0}}) return t.event")
    String findEvent(String state);

}
