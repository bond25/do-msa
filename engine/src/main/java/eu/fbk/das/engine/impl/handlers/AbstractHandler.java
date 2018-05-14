package eu.fbk.das.engine.impl.handlers;

import eu.fbk.das.engine.Handler;
import eu.fbk.das.engine.ProcessEngine;
import eu.fbk.das.domainobject.core.entity.ProcessDiagram;
import eu.fbk.das.domainobject.core.entity.activity.ProcessActivity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractHandler implements Handler {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractHandler.class);

    public boolean handlePrecondition(ProcessEngine pe, ProcessDiagram proc,
                                      ProcessActivity current) {
        if (!current.isSwitch() && !current.isPick()) {
            if (current.getPrecondition() != null) {
                return pe.checkPrecondition(proc, current);
            }
        }
        return true;
    }

    public void handleEffect(ProcessEngine pe, ProcessDiagram proc,
                             ProcessActivity current) {
        if (current.getEffect() != null
                && current.getEffect().getEvent() != null) {
            LOG.debug("[" + proc.getCorrelationId() + "] applicazione effetti");
            pe.applyEffect(proc, current.getEffect());
        }
    }
}
