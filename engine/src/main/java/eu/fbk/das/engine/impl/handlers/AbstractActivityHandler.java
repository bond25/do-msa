package eu.fbk.das.engine.impl.handlers;

import eu.fbk.das.engine.DelegateExecution;
import eu.fbk.das.engine.ProcessEngine;
import eu.fbk.das.domainobject.core.entity.ProcessDiagram;
import eu.fbk.das.domainobject.core.entity.activity.AbstractActivity;
import eu.fbk.das.domainobject.core.entity.activity.ProcessActivity;
import eu.fbk.das.domainobject.core.message.AdaptationProblem;
import eu.fbk.das.domainobject.core.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractActivityHandler extends AbstractHandler {

    private static String HOAA = "HOAA";
    private static Logger LOG = LoggerFactory.getLogger(AbstractActivityHandler.class);

    public void handle(ProcessEngine pe, ProcessDiagram proc, ProcessActivity current) {
        AbstractActivity currentAbstract = (AbstractActivity) current;
        boolean prec = handlePrecondition(pe, proc, currentAbstract);
        if (!prec) {
            LOG.debug("[" + proc.getCorrelationId()
                    + "] Precondition not satisfied");
            return;
        }
        LOG.debug("[" + proc.getCorrelationId() + "] Abstract Activity - "
                + currentAbstract.getName());
        if (currentAbstract.getAbstractType() != null
                && currentAbstract.getAbstractType().equals(HOAA)) {
            LOG.debug("Abstract Activity - HOAA with receiveVar "
                    + currentAbstract.getReceiveVar());
            DelegateExecution handler = pe.getDelegateHandler(currentAbstract.getName()); //delete process pid
            if (handler != null) {
                handler.execute(pe, proc, currentAbstract);
            } else {
                LOG.warn("Hoaa without proper handler "
                        + currentAbstract.getName());
            }
            currentAbstract.setExecuted(true);
        } else {
//            if (currentAbstract.getProblem() != null
//                    && currentAbstract.getProblem().getProblemId() != null) {
//                id = currentAbstract.getProblem().getProblemId();
//            }
//            if (!pe.isWaiting(proc.getpid(), id)) {
//                currentAbstract.setRunning(true);
//                if (currentAbstract.getGoal() != null
//                        && !currentAbstract.getGoal().getPoint().isEmpty()) {
//                    LOG.debug("Abstract activity with goal");
//                } else {
//                    LOG.debug("Abstract activity with no goal, check for receiveGoal");
//                    GoalType goal = buildGoal(pe, proc, currentAbstract);
//                    if (goal != null) {
//                        currentAbstract.setGoal(goal);
//                    }
//                }
//
//            }
            pe.suspendProcess(proc);
            pe.handleAbstractActivity(proc, currentAbstract);
        }
    }

    //TODO: what is receiveGoal, do we need it?
//    private GoalType buildGoal(ProcessEngine pe, ProcessDiagram proc,
//                               AbstractActivity current) {
//        if (current == null) {
//            LOG.error("Not possible to build goal with null activity");
//            return null;
//        }
//        if (current.getReceiveGoal() == null) {
//            LOG.warn("Receive goal is null");
//            return null;
//        }
//        // build goal from receive goal, for now in format DP=value
//        String dpName = current.getReceiveGoal();
//        // TODO: fix for variables
//        ProcVar procVar = pe.getVariablesFor(proc, current.getReceiveGoal());
//        // if (procVar == null) {
//        // List<Integer> correlated = pe.getCorrelated(proc.getpid());
//        // for (Integer cpid : correlated) {
//        // procVar = pe.getVariablesFor(pe.find(cpid),
//        // current.getReceiveGoal());
//        // if (procVar != null) {
//        // break;
//        // }
//        // }
//        // }
//        String dpState = procVar.getValue();
//
//        GoalType response = new GoalType();
//        List<Point> points = response.getPoint();
//        Point p = new Point();
//        List<DomainProperty> dps = p.getDomainProperty();
//        DomainProperty dp = new DomainProperty();
//        dp.setDpName(dpName);
//        dp.getState().add(dpState);
//        dps.add(dp);
//        points.add(p);
//        return response;
//    }

}