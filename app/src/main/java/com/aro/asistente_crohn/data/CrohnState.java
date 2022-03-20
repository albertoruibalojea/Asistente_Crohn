package com.aro.asistente_crohn.data;

import java.util.ArrayList;
import java.util.Date;

public class CrohnState {
    private String id;
    private Boolean active;
    private ArrayList<Symptom> relatedSymptoms;
    Date detectionDate;
    Date limitDate;

    public CrohnState() {
    }

    public CrohnState(String id, Boolean active, ArrayList<Symptom> relatedSymptoms, Date detectionDate, Date limitDate) {
        this.id = id;
        this.active = active;
        this.relatedSymptoms = relatedSymptoms;
        this.detectionDate = detectionDate;
        this.limitDate = limitDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ArrayList<Symptom> getRelatedSymptoms() {
        return relatedSymptoms;
    }

    public void setRelatedSymptoms(ArrayList<Symptom> relatedSymptoms) {
        this.relatedSymptoms = relatedSymptoms;
    }

    public Date getDetectionDate() {
        return detectionDate;
    }

    public void setDetectionDate(Date detectionDate) {
        this.detectionDate = detectionDate;
    }

    public Date getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(Date limitDate) {
        this.limitDate = limitDate;
    }
}
