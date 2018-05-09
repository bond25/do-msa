package eu.fbk.das.blablacar.handlers;

import eu.fbk.das.domainobject.core.entity.ProcessDiagram;
import eu.fbk.das.domainobject.core.entity.activity.ProcessActivity;
import eu.fbk.das.engine.DelegateExecution;
import eu.fbk.das.engine.ProcessEngine;

public class BBCServiceCall implements DelegateExecution {


    @Override
    public void execute(ProcessEngine pe, ProcessDiagram proc, ProcessActivity pa) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("BBCServiceCall executed");
        pa.setExecuted(true);
    }

}
