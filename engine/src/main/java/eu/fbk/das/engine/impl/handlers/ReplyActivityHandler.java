package eu.fbk.das.engine.impl.handlers;

import eu.fbk.das.engine.ProcessEngine;
import eu.fbk.das.domainobject.core.entity.ProcessDiagram;
import eu.fbk.das.domainobject.core.entity.activity.ProcessActivity;

public class ReplyActivityHandler extends AbstractHandler {

    public void handle(ProcessEngine pe, ProcessDiagram proc, ProcessActivity current) {
        proc.setRunning(false);

/*        Message<ExecuteTask> m = new Message<ExecuteTask>();
        pe.getMessageService().send();*/
    }

}
