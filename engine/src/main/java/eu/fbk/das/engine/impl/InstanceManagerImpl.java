package eu.fbk.das.engine.impl;

import eu.fbk.das.domainobject.core.entity.DomainObjectInstance;
import eu.fbk.das.domainobject.core.entity.ProcessDiagram;
import eu.fbk.das.engine.InstanceManager;
import eu.fbk.das.engine.ProcessEngine;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InstanceManagerImpl implements InstanceManager {

    private ProcessEngine engine;

    ConcurrentMap<String, DomainObjectInstance> instances = new ConcurrentHashMap<>();

    ConcurrentMap<String, ProcessDiagram> processes = new ConcurrentHashMap<>();


    public InstanceManagerImpl(ProcessEngine engine) {
        this.engine = engine;
    }

    public void registerInstance(DomainObjectInstance doi) {
        instances.put(doi.getId(), doi);
    }

    public void registerProcess(ProcessDiagram proc) {
        processes.put(proc.getCorrelationId(), proc);
    }

    public DomainObjectInstance getInstance(ProcessDiagram proc) {
        return instances.get(proc.getCorrelationId());
    }

    public DomainObjectInstance getInstance(String correlationId) {
        return instances.get(correlationId);
    }

}
