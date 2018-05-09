package eu.fbk.das.domainobject.core.persistence.model;

import org.neo4j.ogm.annotation.*;

import java.util.List;

@NodeEntity
public class DomainObjectModel {

    @Id
    @GeneratedValue
    Long id;

    String deploymentId;

    @Property(name="name")
    String title;

    public DomainObjectModel() {
    }

    @Relationship(type = "VARIABLE")
    List<VariableDefEntity> variableDefEntities;

    @Relationship(type = "PROCESS")
    ProcessDefEntity processDefEntity;

    @Relationship(type = "INTERNAL_PROPERTY")
    List<DomainPropertyModel> properties;

    @Relationship(type = "FRAGMENT")
    List<FragmentModel> fragmentDefEntities;



    public DomainObjectModel(String deploymentId, String title, List<VariableDefEntity> variableDefEntities, ProcessDefEntity processDefEntity, List<DomainPropertyModel> properties, List<FragmentModel> fragmentDefEntities) {
        this.deploymentId = deploymentId;
        this.title = title;
        this.variableDefEntities = variableDefEntities;
        this.processDefEntity = processDefEntity;
        this.properties = properties;
        this.fragmentDefEntities = fragmentDefEntities;
    }

    public String getName() {
        return title;
    }

    public Long getId() {
        return id;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public void setName(String name) {
        this.title = name;
    }

    public List<VariableDefEntity> getVariableDefEntities() {
        return this.variableDefEntities;
    }

    public void setVariableDefEntities(List<VariableDefEntity> variableDefEntities) {
        this.variableDefEntities = variableDefEntities;
    }

    public List<DomainPropertyModel> getProperties() {
        return properties;
    }

    public void setProperties(List<DomainPropertyModel> properties) {
        this.properties = properties;
    }

    public ProcessDefEntity getProcessDefEntity() {
        return this.processDefEntity;
    }

    public void setProcessDefEntity(ProcessDefEntity processDefEntity) {
        this.processDefEntity = processDefEntity;
    }

    public List<FragmentModel> getFragmentDefEntities() {
        return fragmentDefEntities;
    }

    public void setFragmentDefEntities(List<FragmentModel> fragmentDefEntities) {
        this.fragmentDefEntities = fragmentDefEntities;
    }
}
