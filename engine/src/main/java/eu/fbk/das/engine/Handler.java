package eu.fbk.das.engine;

import eu.fbk.das.domainobject.core.entity.ProcessDiagram;
import eu.fbk.das.domainobject.core.entity.activity.ProcessActivity;

public interface Handler {

    public void handle(ProcessEngine pe, ProcessDiagram proc, ProcessActivity current);

    public boolean handlePrecondition(ProcessEngine pe, ProcessDiagram proc, ProcessActivity current);

    public void handleEffect(ProcessEngine pe, ProcessDiagram proc, ProcessActivity current);

}
