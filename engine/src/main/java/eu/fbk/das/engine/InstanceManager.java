package eu.fbk.das.engine;

import eu.fbk.das.domainobject.core.entity.DomainObjectInstance;
import eu.fbk.das.domainobject.core.entity.ProcessDiagram;

public interface InstanceManager {

    void registerInstance(DomainObjectInstance doi);

    void registerProcess(ProcessDiagram proc);

    DomainObjectInstance getInstance(ProcessDiagram proc);

    DomainObjectInstance getInstance(String correlationId);

}
