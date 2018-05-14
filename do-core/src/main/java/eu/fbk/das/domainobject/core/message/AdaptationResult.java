package eu.fbk.das.domainobject.core.message;

import eu.fbk.das.domainobject.core.entity.ProcessDiagram;

public class AdaptationResult {

    String correlationId;

    ProcessDiagram refinement;

    String doDefName;

    String dpName;

    public AdaptationResult() {}

    public AdaptationResult(ProcessDiagram refinement) {
        this.refinement = refinement;
    }

    public AdaptationResult(String doDefName, String dpName) {
        this.doDefName = doDefName;
        this.dpName = dpName;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public AdaptationResult setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    public String getDoDefName() {
        return doDefName;
    }

    public String getDpName() {
        return dpName;
    }

    public ProcessDiagram getRefinement() {
        return refinement;
    }
}
