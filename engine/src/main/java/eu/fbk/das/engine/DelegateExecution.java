package eu.fbk.das.engine;

import eu.fbk.das.domainobject.core.entity.ProcessDiagram;
import eu.fbk.das.domainobject.core.entity.activity.ProcessActivity;

public interface DelegateExecution {

    void execute(ProcessEngine pe, ProcessDiagram proc, ProcessActivity pa);


}
