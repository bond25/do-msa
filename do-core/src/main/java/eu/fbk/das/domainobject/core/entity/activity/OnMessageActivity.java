package eu.fbk.das.domainobject.core.entity.activity;

import eu.fbk.das.domainobject.core.entity.ProcessDiagram;

import javax.xml.bind.annotation.XmlElement;

public class OnMessageActivity extends ProcessActivity{



    @XmlElement(name = "branch")
    private ProcessDiagram branch;


    public OnMessageActivity()
    {
        super();
        this.setOnMessage(true);
    }


    public OnMessageActivity(int SourceState,int TargetState, ProcessDiagram branch)
    {

        this.setSource(SourceState);
        this.setTarget(TargetState);
        this.branch=branch;
        this.setOnMessage(true);

    }




    public ProcessDiagram getBranch() {
        return branch;
    }


    public void setBranch(ProcessDiagram branch) {
        this.branch = branch;
    }



}
