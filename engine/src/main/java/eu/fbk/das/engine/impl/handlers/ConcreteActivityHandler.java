package eu.fbk.das.engine.impl.handlers;

import eu.fbk.das.engine.ProcessEngine;
import eu.fbk.das.domainobject.core.entity.ProcessDiagram;
import eu.fbk.das.domainobject.core.entity.activity.ConcreteActivity;
import eu.fbk.das.domainobject.core.entity.activity.ProcessActivity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//TODO: replace getExecutionHandler method
public class ConcreteActivityHandler extends AbstractHandler {

    private static Logger LOG = LoggerFactory.getLogger(ConcreteActivity.class);

    public void handle(ProcessEngine pe, ProcessDiagram proc, ProcessActivity current) {
        boolean prec = handlePrecondition(pe, proc, current);
        if (!prec) {
            LOG.debug("[" + proc.getCorrelationId()
                    + "] Precondition not satisfied");
            return;
        }
        LOG.debug("[" + proc.getCorrelationId() + "] Try to execute ConcreteActivity");
        ConcreteActivity concrete = (ConcreteActivity) current;
        if (pe.getDelegateHandler(concrete.getName()) != null) {
            LOG.debug("Using executable handler for concrete activity: "
                    + concrete.getName());

            pe.getDelegateHandler(concrete.getName()).execute(pe, proc, concrete);
            if (concrete.isExecuted()) {
                handleEffect(pe, proc, current);
            }
        } else {
            current.setExecuted(true);
            handleEffect(pe, proc, current);
        }
    }
}
