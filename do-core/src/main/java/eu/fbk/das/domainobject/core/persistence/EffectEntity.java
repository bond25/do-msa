package eu.fbk.das.domainobject.core.persistence;


import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class EffectEntity {

    @Id
    @GeneratedValue
    Long id;

    String domainPropertyName;

    String event;

    public EffectEntity() {

    }

    public String getDomainPropertyName() {
        return domainPropertyName;
    }

    public String getEvent() {
        return event;
    }

    public EffectEntity setDomainPropertyName(String domainPropertyName) {
        this.domainPropertyName = domainPropertyName;
        return this;
    }

    public EffectEntity setEvent(String event) {
        this.event = event;
        return this;
    }
}
