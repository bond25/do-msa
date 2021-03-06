package eu.fbk.das.user.user.handlers;

import eu.fbk.das.domainobject.core.entity.ProcessDiagram;
import eu.fbk.das.domainobject.core.entity.activity.ProcessActivity;
import eu.fbk.das.engine.DelegateExecution;
import eu.fbk.das.engine.ProcessEngine;

public class UserEndInteraction implements DelegateExecution {

    @Override
    public void execute(ProcessEngine pe, ProcessDiagram proc, ProcessActivity pa) {
        System.out.println("Execute `UserEndInteraction` concrete activity");
        pa.setExecuted(true);
    }

}
