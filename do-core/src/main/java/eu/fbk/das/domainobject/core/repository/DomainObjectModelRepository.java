package eu.fbk.das.domainobject.core.repository;

import eu.fbk.das.domainobject.core.persistence.StateEntity;
import eu.fbk.das.domainobject.core.persistence.model.DomainObjectModel;
import org.neo4j.driver.internal.InternalRelationship;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Collection;
import java.util.List;


public interface DomainObjectModelRepository extends Neo4jRepository<DomainObjectModel, Long> {

    DomainObjectModel findByTitle(String title);

    @Query("MATCH (dod: DomainObjectModel {deploymentId: {0}}) RETURN dod")
    DomainObjectModel findByDeploymentId(String deploymentId);

    @Query("MATCH (dod:DomainObjectModel)-[INTERNAL_PROPERTY]->(dp:DomainPropertyModel {name: {0}}) return dod")
    List<DomainObjectModel> findRelevantDoModels(String name);

    @Query("match (dom:DomainObjectModel)-[:INTERNAL_PROPERTY]-(dp:DomainPropertyModel)-[:STATES]-(:StateEntity)-[t:TRANSITION]->(se:StateEntity)\n" +
            "where ID(dom) = {0} and se.name = {1}\n" +
            "return t.event")
    String findEvent(Long domId, String finalState);

    @Query("match (dom:DomainObjectModel)-[:INTERNAL_PROPERTY]-(dp:DomainPropertyModel)-[:STATES]-(s:StateEntity) where ID(dom) = {0} and dp.name = {1} and s.isInitial = true return s")
    List<StateEntity> findInitialStates(Long domId, String dpName);

    @Query("match path=shortestPath((istate:StateEntity)-[:TRANSITION *0..]-(fstate:StateEntity)) WHERE istate.isInitial = true and ID(istate) IN {0} and fstate.name = {1} return path")
    List<StateEntity> findDomainPropertyPath(Long[] initStates, String finalState);

}
