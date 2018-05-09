package eu.fbk.das.domainobject.core.persistence.model;


import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity
public class VariableDefEntity {

    @Id
    @GeneratedValue
    Long id;

    @Property(name="name")
    String title;

    String type;

    public VariableDefEntity() {

    }

    public VariableDefEntity(String name, String type, String value) {
        this.title = name;
        this.type = type;
    }

}
