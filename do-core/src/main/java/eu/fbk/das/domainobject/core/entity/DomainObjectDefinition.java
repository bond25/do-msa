package eu.fbk.das.domainobject.core.entity;

import eu.fbk.das.domainobject.core.entity.jaxb.DomainObject;
import eu.fbk.das.domainobject.core.entity.jaxb.DomainProperty;
import eu.fbk.das.domainobject.core.entity.jaxb.Fragment;
import eu.fbk.das.domainobject.core.entity.jaxb.Process;

import java.util.List;


/**
 * Define an entity (Domain Object) and collect all information about it in one
 * place
 *
 * {@link DomainObject}, {@link Process}, {@link Fragment},
 * {@link DomainProperty}
 */
public class DomainObjectDefinition {

    private DomainObject domainObject;
    private List<Fragment> fragments;

    private List<DomainProperty> properties;
    private Process process;
    private boolean isRole;

    public DomainObjectDefinition(DomainObject domainObject, Process process, List<Fragment> fragments, List<DomainProperty> properties) {
        this.domainObject = domainObject;
        this.process = process;
        this.fragments = fragments;
        this.properties = properties;
    }


    public void setDomainObject(DomainObject e) {
        this.domainObject = e;
    }

    public DomainObject getDomainObject() {
        return domainObject;
    }

    public List<Fragment> getFragments() {
        return fragments;
    }

    public void setFragments(List<Fragment> fragments) {
        this.fragments = fragments;
    }

    public List<DomainProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<DomainProperty> properties) {
        this.properties = properties;
    }

    public void setProcess(Process process) {
        this.process = process;

    }

    public Process getProcess() {
        return process;
    }

    public boolean isRole() {
        return isRole;
    }

    public void setRole(boolean isRole) {
        this.isRole = isRole;
    }

    @Override
    public String toString() {
        return "[name=" + getProcess().getName() + "]";
    }

}

