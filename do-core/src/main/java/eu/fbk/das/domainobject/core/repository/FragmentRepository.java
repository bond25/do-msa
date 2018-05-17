package eu.fbk.das.domainobject.core.repository;

import eu.fbk.das.domainobject.core.persistence.model.FragmentActionModel;
import eu.fbk.das.domainobject.core.persistence.model.FragmentModel;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface FragmentRepository extends Neo4jRepository<FragmentModel, Long> {

    FragmentModel findByTitle(String title);

    @Query("MATCH (dom:DomainObjectModel)-[:INTERNAL_PROPERTY]->(dp:DomainPropertyModel)-[:STATES]->(:StateEntity)-[t:TRANSITION]->(finalState:StateEntity)\n" +
            "WHERE ID(dom) = {0}\n" +
            "AND dp.name = {1}\n" +
            "AND finalState.name = {2}\n" +
            "WITH t.event as event, dom\n" +
            "MATCH (dom)-[:FRAGMENT]->(fragment:FragmentModel)-[:START_ACTIVITY]->(startActivity:FragmentActionModel)-[:NEXT_ACTION *0..]->(finalActivity:FragmentActionModel)-[:EFFECT]->(effect:EffectEntity)\n" +
            "WHERE effect.domainPropertyName = {1}\n" +
            "AND effect.event = event\n" +
            "WITH fragment, startActivity, finalActivity\n" +
            "MATCH path=((startActivity)-[:NEXT_ACTION *0..]-(finalActivity))\n" +
            "RETURN path")
    List<FragmentActionModel> findFragmentActionFlow(Long domId, String domainProperty, String finalState);

    @Query("MATCH (f:FragmentModel)-[START_ACTION]->(:FragmentActionModel)-[NEXT_ACTION *0..]->(a:FragmentActionModel) where ID(f) = {0} return a")
    List<FragmentActionModel> findActionFlowByFragmentId(Long id);

    @Query("MATCH (dom:DomainObjectModel)-[:FRAGMENT]->(f:FragmentModel)-e where ID(dom) = {0} return f")
    List<FragmentModel> findRelevantFragments(Long domId, String event, String dpName);
}
