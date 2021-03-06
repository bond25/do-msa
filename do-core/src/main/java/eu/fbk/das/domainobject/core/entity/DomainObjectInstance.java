package eu.fbk.das.domainobject.core.entity;

import java.util.ArrayList;
import java.util.List;

public class DomainObjectInstance {

    private ProcessDiagram process;
    private String id;
    private List<ServiceDiagram> serviceDiagrams;
    // il nome del modello del domainObject dal quale questa istanza e' generata
    private String type;
    private List<ObjectDiagram> internal = new ArrayList<ObjectDiagram>();
    private List<ObjectDiagram> external = new ArrayList<ObjectDiagram>();
    private String lon;
    private String lat;
    private Integer onTurn;
    private boolean role;
    private boolean singleton;
    private String ensemble;
    private String correlations;
    private String selectedRoute;
    private Integer pickupPoint;
    private String routes;

    public void setProcess(ProcessDiagram process) {
        this.process = process;
    }

    public ProcessDiagram getProcess() {
        return process;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

//    public void updateKnowledge(Dp updated)
//            throws InvalidObjectCurrentStateException {
//        if (internal == null) {
//            internal = new ArrayList<ObjectDiagram>();
//        }
//        for (ObjectDiagram od : internal) {
//            if (od.getOid().equals(updated.getName())) {
//                od.setCurrentState(updated.getCurrentState());
//            }
//        }
//        // TODO: also external, for first storyboard
//        for (ObjectDiagram od : external) {
//            if (od.getOid().equals(updated.getName())) {
//                od.setCurrentState(updated.getCurrentState());
//            }
//        }
//
//    }

    public void setFragments(List<ServiceDiagram> serviceDiagrams) {
        this.serviceDiagrams = serviceDiagrams;
    }

    public List<ServiceDiagram> getFragments() {
        return serviceDiagrams;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setInternalKnowledge(List<ObjectDiagram> internal) {
        this.internal = internal;
    }

    public void setExternalKnowledge(List<ObjectDiagram> external) {
        this.external = external;
    }

    public List<ObjectDiagram> getInternalKnowledge() {
        return internal;
    }

    public List<ObjectDiagram> getExternalKnowledge() {
        return external;
    }

    /**
     * @return true if know a domainproperty
     */
    public boolean know(String domainProperty) {
        for (ObjectDiagram i : internal) {
            if (i.getType().equals(domainProperty)) {
                return true;
            }
        }
        for (ObjectDiagram i : external) {
            if (i.getType().equals(domainProperty)) {
                return true;
            }
        }
        return false;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLon(String lon) {
        this.lon = lon;

    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public void setOnTurn(Integer onTurn) {
        this.onTurn = onTurn;
    }

    public Integer getOnTurn() {
        return onTurn;
    }

    public boolean isRole() {
        return role;
    }

    public void setRole(boolean role) {
        this.role = role;
    }

    public boolean isSingleton() {
        return singleton;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    @Override
    public String toString() {
        return "[id=" + id + "]";
    }

    public void setEnsemble(String ensemble) {
        this.ensemble = ensemble;
    }

    public String getEnsemble() {
        return ensemble;
    }

    public void setCorrelations(String correlations) {
        this.correlations = correlations;
    }

    public String getCorrelations() {
        return correlations;
    }

    public void setSelectedRoute(String selectedRoute) {
        this.selectedRoute = selectedRoute;
    }

    public String getSelectedRoute() {
        return selectedRoute;
    }

    public Integer getPickupPoint() {
        return pickupPoint;
    }

    public void setPickupPoint(Integer pickupPoint) {
        this.pickupPoint = pickupPoint;
    }

    public void setRoutes(String routes) {
        this.routes = routes;
    }

    public String getRoutes() {
        return routes;
    }
}

