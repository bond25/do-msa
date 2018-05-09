package eu.fbk.das.domainobject.core.repository;

import eu.fbk.das.domainobject.core.persistence.model.FragmentActionModel;
import eu.fbk.das.domainobject.core.persistence.model.FragmentModel;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface FragmentRepository extends Neo4jRepository<FragmentModel, Long> {

    FragmentModel findByTitle(String title);

    @Query("MATCH (:FragmentModel {name: {0}})-[START_ACTION]->(:FragmentActionModel)-[NEXT_ACTION *0..]->(a:FragmentActionModel) return a")
    List<FragmentActionModel> findActionFlowByFragmentName(String name);

    @Query("MATCH (:DomainObjectModel {name: {0}})-[:FRAGMENT]->(f:FragmentModel)-[:START_ACTIVITY]->(:FragmentActionModel)-[:NEXT_ACTION *0..]->(act:FragmentActionModel)-[:EFFECT]->(:EffectEntity {event: {1}, domainPropertyName: {2}}) return f")
    List<FragmentModel> findRelevantFragments(String dodName, String event, String dpName);
}
