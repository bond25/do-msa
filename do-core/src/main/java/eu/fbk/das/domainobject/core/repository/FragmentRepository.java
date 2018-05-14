package eu.fbk.das.domainobject.core.repository;

import eu.fbk.das.domainobject.core.persistence.model.FragmentActionModel;
import eu.fbk.das.domainobject.core.persistence.model.FragmentModel;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface FragmentRepository extends Neo4jRepository<FragmentModel, Long> {

    FragmentModel findByTitle(String title);

    @Query("match (f:FragmentModel)-[:START_ACTIVITY]-(sAct:FragmentActionModel)-[:NEXT_ACTION *0..]-(fAct:FragmentActionModel)-[:EFFECT]-(e:EffectEntity)\n" +
            "where f.name = {0} and e.event = {1} \n" +
            "with sAct, fAct\n" +
            "match path=shortestPath((:FragmentActionModel {name: sAct.name})-[:NEXT_ACTION *0..]-(:FragmentActionModel {name: fAct.name})) return path")
    List<FragmentActionModel> findFragmentActionFlow(String fragmentName, String event);

    @Query("MATCH (f:FragmentModel)-[START_ACTION]->(:FragmentActionModel)-[NEXT_ACTION *0..]->(a:FragmentActionModel) where ID(f) = {0} return a")
    List<FragmentActionModel> findActionFlowByFragmentId(Long id);

    @Query("MATCH (dom:DomainObjectModel)-[:FRAGMENT]->(f:FragmentModel)-[:START_ACTIVITY]->(:FragmentActionModel)-[:NEXT_ACTION *0..]->(act:FragmentActionModel)-[:EFFECT]->(:EffectEntity {event: {1}, domainPropertyName: {2}}) where ID(dom) = {0} return f")
    List<FragmentModel> findRelevantFragments(Long domId, String event, String dpName);
}
