package eu.fbk.das.domainobject.core.parser;

import eu.fbk.das.domainobject.core.entity.*;
import eu.fbk.das.domainobject.core.entity.activity.*;
import eu.fbk.das.domainobject.core.entity.jaxb.*;
import eu.fbk.das.domainobject.core.entity.jaxb.Process;
import eu.fbk.das.domainobject.core.entity.jaxb.activity.*;
import eu.fbk.das.domainobject.core.entity.jaxb.Fragment.Action;
import eu.fbk.das.domainobject.core.entity.jaxb.Fragment.State;
import eu.fbk.das.domainobject.core.entity.jaxb.Fragment.Transition;
import eu.fbk.das.domainobject.core.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Parser {

    private static final int DEFAULT_PID = 1;

    private static final Logger LOG = LoggerFactory.getLogger(Parser.class);

    public static DomainObject parseDomainObject(File f) {
        DomainObject jaxbDO = null;
        try {
            JAXBContext jaxbContext = JAXBContext
                    .newInstance(DomainObject.class);
            jaxbDO = (DomainObject) jaxbContext.createUnmarshaller().unmarshal(f);
        } catch (JAXBException e) {
            LOG.error("Error while parsing Domain Object {}", e);
        }
        return jaxbDO;
    }

    public static Process parseProcess(File f) {
        Process p = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Process.class);
            p = (Process) jaxbContext.createUnmarshaller().unmarshal(f);
        } catch (JAXBException e) {
            LOG.error("Error while parsing Domain Object Process {}", e);
        }
        LOG.debug("Parse process {}", p.getActivity());
        return p;
    }

    public static Fragment parseFragment(File f) {
        Fragment frag = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Fragment.class);
            frag = (Fragment) jaxbContext.createUnmarshaller().unmarshal(f);
        } catch (JAXBException e) {
            LOG.error("Error while parsing Domain Object Fragments {}", e);
        }
        return frag;
    }


    public static DomainProperty parseDomainProperty(File f) {
        DomainProperty dp = null;
        try {
            JAXBContext jaxbContext = JAXBContext
                    .newInstance(DomainProperty.class);
            dp = (DomainProperty) jaxbContext.createUnmarshaller().unmarshal(f);
        } catch (JAXBException e) {
            LOG.error("Error while parsing Domain Object Property {}", e);
        }
        return dp;
    }

    public static DomainObjectInstance convertToDomainObjectInstance(DomainObjectDefinition dod) {
        String type = dod.getDomainObject().getName();
        DomainObjectInstance doi = new DomainObjectInstance();
        ProcessDiagram process = null;
        List<ServiceDiagram> services = null;
        List<ObjectDiagram> domainProperties = null;
        try {
            process = convertToProcessDiagram(dod);
            services = convertToServiceDiagram(dod);
            domainProperties = convertInternalToObjectDiagrams(dod);
        } catch (InvalidFlowInitialStateException e) {
            e.printStackTrace();
        } catch (InvalidFlowActivityException e) {
            e.printStackTrace();
        } catch (FlowDuplicateActivityException e) {
            e.printStackTrace();
        } catch (InvalidServiceTransitionException e) {
            e.printStackTrace();
        } catch (ServiceDuplicateActionException e) {
            e.printStackTrace();
        } catch (InvalidServiceInitialStateException e) {
            e.printStackTrace();
        }
        doi.setType(type);
        doi.setFragments(services);
        doi.setProcess(process);
        doi.setInternalKnowledge(domainProperties);
        doi.setRole(dod.isRole());
        doi.setSingleton(dod.getDomainObject().isSingleton());
        return doi;
    }


    public static ProcessDiagram convertToProcessDiagram(DomainObjectDefinition dod) throws InvalidFlowInitialStateException,
            InvalidFlowActivityException, FlowDuplicateActivityException {
        List<ActivityType> activities = dod.getProcess().getActivity();
        ProcessDiagram pd = new ProcessDiagram();
        pd.addAllActivity(parseActivities(activities));
        pd.setName(dod.getProcess().getName());
        return pd;
    }

    public static List<ServiceDiagram> convertToServiceDiagram(DomainObjectDefinition dod) throws InvalidServiceInitialStateException,
            InvalidServiceTransitionException, ServiceDuplicateActionException {
        List<Fragment> fragments = dod.getFragments();
        if (fragments == null) {
            return null;
        }
        List<ServiceDiagram> response = new ArrayList<ServiceDiagram>();
        for (Fragment f : fragments) {
            Set<String> initialStates = new HashSet<String>();
            for (State s : f.getState()) {
                if (s.isIsInitial() != null && s.isIsInitial()) {
                    initialStates.add(s.getName());
                }
            }
            Set<String> inputs = new HashSet<String>();
            Set<String> outputs = new HashSet<String>();
            Set<String> abstracts = new HashSet<String>();
            Set<String> concretes = new HashSet<String>();
            for (Action act : f.getAction()) {
                if (act.getActionType() == ActionTypeValues.INPUT) {
                    inputs.add(act.getName());
                }
                if (act.getActionType() == ActionTypeValues.OUTPUT) {
                    outputs.add(act.getName());
                }
                if (act.getActionType() == ActionTypeValues.CONCRETE) {
                    concretes.add(act.getName());
                    outputs.add(act.getName());
                }
                if (act.getActionType() == ActionTypeValues.ABSTRACT) {
                    abstracts.add(act.getName());
                    inputs.add(act.getName());
                }
            }
            Set<ServiceTransition> transitions = new HashSet<ServiceTransition>();
            for (Transition t : f.getTransition()) {
                ServiceTransition st = new ServiceTransition(t
                        .getInitialState().getValue(), t.getFinalState()
                        .getValue(), t.getAction().getName(),
                        getServiceDiagramActionType(t.getAction().getName(),
                                f.getAction()));
                transitions.add(st);
            }
            Set<String> states = new HashSet<String>();
            for (State s : f.getState()) {
                states.add(s.getName());
            }

            ServiceDiagram service = new ServiceDiagram(f.getId(), null,
                    f.getConsumerEntityType(), states, initialStates, inputs,
                    outputs, abstracts, concretes, transitions, false);

            response.add(service);
        }
        return response;
    }

    public static List<ObjectDiagram> convertInternalToObjectDiagrams(DomainObjectDefinition dod) {
        List<DomainProperty> internalDp = dod.getProperties();
        List<ObjectDiagram> result = new ArrayList<>();
        if (internalDp == null) {
            return null;
        }
        internalDp.forEach(dp -> {
            try {
                result.add(convertToObjectDiagram(dp));
            } catch (InvalidObjectInitialStateException
                    | InvalidObjectTransitionException
                    | InvalidObjectCurrentStateException e) {
                LOG.error("Error while convert Domain Property {}", e);
            }
        });
        return result;
    }

    private static ObjectDiagram convertToObjectDiagram(DomainProperty dp)
            throws InvalidObjectInitialStateException,
            InvalidObjectTransitionException,
            InvalidObjectCurrentStateException {

        String initial = "";
        Set<String> initialStates = new HashSet<String>();
        Set<String> states = new HashSet<String>();
        for (DomainProperty.State st : dp.getState()) {
            states.add(st.getValue());
            if (st != null && st.isIsInitial() != null && st.isIsInitial()) {
                initialStates.add(st.getValue());
                initial = st.getValue();
            }
        }
        Set<String> events = new HashSet<String>();
        for (DomainPropertyEventType e : dp.getEvent()) {
            events.add(e.getValue());
        }
        Set<ObjectTransition> transitions = new HashSet<ObjectTransition>();
        for (DomainProperty.Transition t : dp.getTransition()) {
            ObjectTransition ot = new ObjectTransition(t.getFrom().getValue(),
                    t.getTo().getValue(), t.getEvent().getValue());
            transitions.add(ot);
        }
        ObjectDiagram od = new ObjectDiagram(dp.getId(), dp.getId(), states,
                initialStates, events, transitions);
        od.setCurrentState(initial);
        return od;
    }

    public static List<ProcessActivity> parseActivities(List<ActivityType> activities) throws InvalidFlowInitialStateException,
            InvalidFlowActivityException, FlowDuplicateActivityException {
        List<ProcessActivity> result = new ArrayList<ProcessActivity>();
        int a = 0;
        for (ActivityType processActivity : activities) {
            Set<Integer> states = new HashSet<Integer>();
            Integer initialState = 0;
            states.add(initialState);
            if (isReceiveType(processActivity)) {
                ReplyActivity act = new ReplyActivity(
                        a, a + 1, processActivity.getName());
                act.setReceive(true);
                act.setEffect(processActivity.getEffect());
                act.setPrecondition(processActivity.getPrecondition());
                result.add(act);
            }
            if (isInvokeType(processActivity)) {
                InvokeActivty act = new InvokeActivty(
                        a, a + 1, processActivity.getName());
                act.setSend(true);
                act.setEffect(processActivity.getEffect());
                act.setPrecondition(processActivity.getPrecondition());
                result.add(act);
            }
            //TODO: Default pid, why?
            if (isSwitchType(processActivity)) {
                ProcessActivity act = parseSwitchActivity(processActivity,
                        a, states, DEFAULT_PID);
                act.setSwitch(true);
                result.add(act);
            }
            if (isConcreteType(processActivity)) {
                ConcreteActivity act = (ConcreteActivity) parseSimpleActivity(
                        processActivity, a, states);
                act.setPrecondition(processActivity.getPrecondition());
                act.setEffect(processActivity.getEffect());
                result.add(act);
            }
            if (isPickType(processActivity)) {
                ProcessActivity act = parsePickActivity(processActivity, a,
                        states, DEFAULT_PID);
                result.add(act);
            }
            if (isAbstractType(processActivity)) {
                ProcessActivity act = parseSimpleActivity(processActivity, a,
                        states);
                AbstractActivity absact = (AbstractActivity) act;
                absact.setEffect(processActivity.getEffect());
                absact.setPrecondition(processActivity.getPrecondition());
                result.add(act);
            }
            if (isWhileType(processActivity)) {
                ProcessActivity act = parseWhileActivity(processActivity, a,
                        states, DEFAULT_PID);
                act.setPrecondition(processActivity.getPrecondition());
                act.setEffect(processActivity.getEffect());
                result.add(act);
            }
            if (isScopeType(processActivity)) {
                ProcessActivity act = parseScopeActivity(processActivity, a,
                        states, DEFAULT_PID);
                act.setPrecondition(processActivity.getPrecondition());
                act.setEffect(processActivity.getEffect());
                result.add(act);
            }
            a++;
        }
        return result;
    }

    private static boolean isWhileType(ActivityType processActivity) {
        return processActivity.getClass().getSimpleName()
                .equals("WhileType");
    }

    public static boolean isAbstractType(Object processActivity) {
        return processActivity.getClass().getSimpleName()
                .equals("AbstractType");
    }

    public static boolean isPickType(Object processActivity) {
        return processActivity.getClass().getSimpleName()
                .equals("PickType");
    }

    public static boolean isConcreteType(Object processActivity) {
        return processActivity.getClass().getSimpleName()
                .equals("ConcreteType");
    }

    public static boolean isSwitchType(Object processActivity) {
        return processActivity.getClass().getSimpleName()
                .equals("SwitchType");
    }

    public static boolean isInvokeType(Object processActivity) {
        return processActivity.getClass().getSimpleName()
                .equals("InvokeType");
    }

    public static boolean isReceiveType(Object processActivity) {
        return processActivity.getClass().getSimpleName()
                .equals("ReceiveType");
    }

    private static boolean isScopeType(ActivityType processActivity) {
        return processActivity.getClass().getSimpleName()
                .equals("ScopeType");
    }

    private static ServiceDiagramActionType getServiceDiagramActionType(String name,
                                                                        List<Action> action) {
        for (Action act : action) {
            if (act.getName().equals(name)) {
                switch (act.getActionType()) {
                    case ABSTRACT:
                        return ServiceDiagramActionType.IN;
                    case INPUT:
                        return ServiceDiagramActionType.IN;
                    case OUTPUT:
                        return ServiceDiagramActionType.OUT;
                    case CONCRETE:
                        return ServiceDiagramActionType.OUT;
                    default:
                        return ServiceDiagramActionType.IN;
                }
            }
        }
        return ServiceDiagramActionType.IN;
    }

    public static ProcessActivity parseSwitchActivity(java.lang.Object object,
                                                      int sourcest, Set<Integer> states, int ProcessID)
            throws InvalidFlowInitialStateException,
            InvalidFlowActivityException, FlowDuplicateActivityException {

        List<IFActivity> IFs = new ArrayList<IFActivity>();
        DefaultActivity def = new DefaultActivity();

        // Default branch
        SwitchType.Default d = ((SwitchType) object).getDefault();
        List<ActivityType> seqActs = d.getBranch().getActivity();
        int sourcedef = 0;
        int DefInitialState = sourcedef;
        Set<Integer> DefStates = new HashSet<Integer>();
        DefStates.add(DefInitialState);

        List<ProcessActivity> DefbranchActs = new ArrayList<ProcessActivity>();
        for (java.lang.Object act : seqActs) {
            if (isSwitchType(act)) {
                ProcessActivity br = parseSwitchActivity(act, sourcedef,
                        DefStates, ProcessID);
                DefbranchActs.add(br);

            } else if (isPickType(act)) {
                ProcessActivity br = parsePickActivity(act, sourcedef,
                        DefStates, ProcessID);
                DefbranchActs.add(br);

            } else {
                ProcessActivity br = parseSimpleActivity(act, sourcedef,
                        DefStates);
                DefbranchActs.add(br);
            }
            sourcedef++;
            DefStates.add(sourcedef);

        }
        ProcessDiagram Defbranch = new ProcessDiagram(ProcessID, DefStates,
                DefInitialState, DefbranchActs);
        def.setDefaultBranch(Defbranch);

        // IFs management

        for (int j = 0; j < ((SwitchType) object).getIf().size(); j++) {
            List<SwitchType.If> ListofIFs = ((SwitchType) object).getIf();

            for (SwitchType.If currentIF : ListofIFs) {
                // inside the IF

                int sourceif = 0;
                int IFInitialState = sourceif;
                Set<Integer> ifStates = new HashSet<Integer>();
                ifStates.add(IFInitialState);
                IFActivity ifAct = new IFActivity();

                SwitchType.If.Branch branch = currentIF.getBranch();

                List<PreconditionType> conditions = new ArrayList<PreconditionType>();
                List<SwitchType.If.VarCondition> varConditions = new ArrayList<SwitchType.If.VarCondition>();
                for (java.lang.Object cond : currentIF
                        .getContextConditionOrVarCondition()) {
                    if (cond.getClass()
                            .getSimpleName()
                            .equals("PreconditionType")) {
                        conditions.add((PreconditionType) cond);
                    } else if (cond
                            .getClass()
                            .getSimpleName()
                            .startsWith("SwitchType")) {
                        varConditions.add((SwitchType.If.VarCondition) cond);
                    }
                }

                List<ProcessActivity> IfBranchActivities = new ArrayList<ProcessActivity>();
                List<ActivityType> activs = branch.getActivity();
                // inside to a single branch
                for (java.lang.Object act : activs) {
                    if (act.getClass()
                            .getSimpleName()
                            .equals("SwitchType")) {
                        ProcessActivity a = parseSwitchActivity(act,
                                sourceif, ifStates, ProcessID);
                        IfBranchActivities.add(a);

                    } else {
                        ProcessActivity a = parseSimpleActivity(act,
                                sourceif, ifStates);
                        IfBranchActivities.add(a);
                    }
                    sourceif++;
                    ifStates.add(sourceif);
                }

                ProcessDiagram branchAct = new ProcessDiagram(ProcessID,
                        ifStates, IFInitialState, IfBranchActivities);
                ifAct.setBranch(branchAct);
                ifAct.setConditions(conditions);
                ifAct.setVarConditions(varConditions);
                IFs.add(ifAct);
            }

        }
        SwitchActivity sw = new SwitchActivity(sourcest, sourcest + 1,
                "SWITCH", IFs, def);
        states.add(sourcest + 1);
        return sw;
    }

    public static ProcessActivity parsePickActivity(java.lang.Object object,
                                                    int sourcest, Set<Integer> states, int ProcessID)
            throws InvalidFlowInitialStateException,
            InvalidFlowActivityException, FlowDuplicateActivityException {
        List<OnMessageActivity> OnMsgs = new ArrayList<OnMessageActivity>();

        // OnMsgss management
        List<PickType.OnMessage> ListofOnMsgs = ((PickType) object)
                .getOnMessage();

        for (PickType.OnMessage currentOnMsg : ListofOnMsgs) {
            // inside the onMessage

            int sourceif = 0;
            int OMInitialState = sourceif;
            Set<Integer> OMStates = new HashSet<Integer>();
            OMStates.add(OMInitialState);
            OnMessageActivity OMAct = new OnMessageActivity();
            OMAct.setOnMessage(true);
            OMAct.setName(currentOnMsg.getName());

            List<ProcessActivity> OMBranchActivities = new ArrayList<ProcessActivity>();
            List<ActivityType> activs = currentOnMsg.getActivity();
            // inside to a single branch
            for (java.lang.Object act : activs) {
                if (act.getClass()
                        .getSimpleName()
                        .equals("SwitchType")) {
                    ProcessActivity a = parseSwitchActivity(act, sourceif,
                            OMStates, ProcessID);
                    OMBranchActivities.add(a);

                } else if (act.getClass().getSimpleName()
                        .equals("PickType")) {
                    ProcessActivity a = parsePickActivity(act, sourceif,
                            OMStates, ProcessID);
                    OMBranchActivities.add(a);

                } else if (act.getClass().getSimpleName()
                        .equals("WhileType")) {
                    ProcessActivity a = parseWhileActivity(
                            (ActivityType) act, sourceif, OMStates, ProcessID);
                    OMBranchActivities.add(a);

                } else if (act.getClass().getSimpleName()
                        .equals("ScopeType")) {
                    ProcessActivity a = parseScopeActivity(
                            (ActivityType) act, sourceif, OMStates, ProcessID);
                    OMBranchActivities.add(a);
                } else {
                    ProcessActivity a = parseSimpleActivity(act, sourceif,
                            OMStates);
                    a.setEffect(((ActivityType) act).getEffect());
                    a.setPrecondition(((ActivityType) act).getPrecondition());
                    OMBranchActivities.add(a);
                }
                sourceif++;
                OMStates.add(sourceif);
            }

            ProcessDiagram branchAct = new ProcessDiagram(ProcessID, OMStates,
                    OMInitialState, OMBranchActivities);
            OMAct.setBranch(branchAct);
            OnMsgs.add(OMAct);
        }

        PickActivity pick = new PickActivity(sourcest, sourcest + 1, "PICK",
                OnMsgs);
        pick.setPick(true);
        states.add(sourcest + 1);
        return pick;
    }

    public static ProcessActivity parseSimpleActivity(Object object, int sourcest,
                                                      Set<Integer> states) {

        ProcessActivity result = new ProcessActivity();
        if (isReceiveType(object)) {
            String name = ((ReceiveType) object).getName();
            ReplyActivity act = new ReplyActivity(sourcest, sourcest + 1, name);
            sourcest++;
            act.setReceive(true);
            states.add(sourcest);
            result = act;
        }

        if (isAbstractType(object)) {
            String name = ((AbstractType) object).getName();
            GoalType goal = ((AbstractType) object).getGoal();
            AbstractActivity act = new AbstractActivity(sourcest, sourcest + 1,
                    name, goal);
            if (((AbstractType) object).getType() != null) {
                act.setAbstractType(((AbstractType) object).getType());
                act.setReceiveVar(((AbstractType) object).getReceiveVar());
            }
            act.setAbstract(true);
            sourcest++;
            states.add(sourcest);
            result = act;
        }
        if (isConcreteType(object)) {
            String name = ((ConcreteType) object).getName();
            ConcreteActivity act = new ConcreteActivity(sourcest, sourcest + 1,
                    name, ((ConcreteType) object).getReturnsTo());
            act.setConcrete(true);
            result = act;
        }
        if (isInvokeType(object)) {
            String name = ((InvokeType) object).getName();
            InvokeActivty act = new InvokeActivty(sourcest, sourcest + 1, name);
            sourcest++;
            act.setSend(true);
            states.add(sourcest);
            result = act;

        }
        return result;
    }

    private static ProcessActivity parseWhileActivity(ActivityType processActivity,
                                                      int sourcest, Set<Integer> states, int ProcessID)
            throws InvalidFlowInitialStateException,
            InvalidFlowActivityException, FlowDuplicateActivityException {
        WhileType wa = (WhileType) processActivity;
        ProcessDiagram defbranch = null;
        if (wa.getActivity() != null) {

            int sourcedef = 0;
            int DefInitialState = sourcedef;
            Set<Integer> DefStates = new HashSet<Integer>();
            DefStates.add(DefInitialState);

            int lastSourcedef = 0;
            List<ProcessActivity> DefbranchActs = new ArrayList<ProcessActivity>();
            for (ActivityType act : wa.getActivity()) {
                ProcessActivity br = null;
                if (isSwitchType(act)) {
                    br = parseSwitchActivity(act, sourcedef, DefStates,
                            ProcessID);
                } else if (isPickType(act)) {
                    br = parsePickActivity(act, sourcedef, DefStates,
                            ProcessID);
                } else if (isWhileType(act)) {
                    br = parseWhileActivity(act, sourcedef, DefStates,
                            ProcessID);
                    br.setSource(sourcedef);
                    br.setTarget(sourcedef + 1);
                } else if (isScopeType(act)) {
                    br = parseScopeActivity(act, sourcedef, DefStates,
                            ProcessID);
                    br.setSource(sourcedef);
                    br.setTarget(sourcedef + 1);
                } else {
                    br = parseSimpleActivity(act, sourcedef, DefStates);
                }
                // common to all activities
                br.setPrecondition(act.getPrecondition());
                br.setEffect(act.getEffect());
                DefbranchActs.add(br);

                lastSourcedef = sourcedef;
                sourcedef++;
                DefStates.add(sourcedef);
            }
            defbranch = new ProcessDiagram(ProcessID, DefStates,
                    DefInitialState, DefbranchActs);

        }
        WhileActivity result = new WhileActivity(sourcest, sourcest + 1,
                wa.getName());
        if (wa.getVarCondition() != null) {
            result.setCondition(wa.getVarCondition());
        }
        if (wa.getContextCondition() != null) {
            result.setContextCondition(wa.getContextCondition());
        }
        result.addDefaultBranch(defbranch);

        return result;
    }

    private static ProcessActivity parseScopeActivity(ActivityType processActivity, int sourcest, Set<Integer> states, int ProcessID)
            throws InvalidFlowInitialStateException,
            InvalidFlowActivityException, FlowDuplicateActivityException {
        ScopeType sa = (ScopeType) processActivity;
        List<ProcessActivity> activities = new ArrayList<ProcessActivity>();

        ScopeActivity result = new ScopeActivity(sourcest, sourcest + 1,
                sa.getName());

        if (sa.getActivity() != null) {
            int sourcedef = 0;
            int DefInitialState = sourcedef;
            Set<Integer> DefStates = new HashSet<Integer>();
            DefStates.add(DefInitialState);
            int lastSourcedef = 0;

            for (ActivityType act : sa.getActivity()) {
                ProcessActivity br = null;
                if (isSwitchType(act)) {
                    br = parseSwitchActivity(act, sourcedef, DefStates,
                            ProcessID);
                } else if (isPickType(act)) {
                    br = parsePickActivity(act, sourcedef, DefStates,
                            ProcessID);
                } else if (isWhileType(act)) {
                    br = parseWhileActivity(act, sourcedef, DefStates,
                            ProcessID);
                    br.setSource(sourcedef);
                    br.setTarget(sourcedef + 1);

                } else if (isScopeType(act)) {
                    br = parseScopeActivity(act, sourcedef, DefStates,
                            ProcessID);
                    br.setSource(sourcedef);
                    br.setTarget(sourcedef + 1);
                } else {
                    br = parseSimpleActivity(act, sourcedef, DefStates);
                }
                // common to all activities
                br.setPrecondition(act.getPrecondition());
                br.setEffect(act.getEffect());

                lastSourcedef = sourcedef;
                sourcedef++;
                DefStates.add(sourcedef);
                activities.add(br);
            }
            ProcessDiagram branch = new ProcessDiagram(ProcessID, DefStates,
                    DefInitialState, activities);
            result.setBranch(branch);
        }

        result.setEventHandler(sa.getEventHandler());
        return result;
    }

}
