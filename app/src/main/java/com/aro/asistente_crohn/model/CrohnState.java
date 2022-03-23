package com.aro.asistente_crohn.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class CrohnState {
    @ColumnInfo
    @PrimaryKey(autoGenerate=true)
    private Integer id;
    @ColumnInfo
    private String username;
    @ColumnInfo
    private Boolean active;
    @ColumnInfo
    private String relatedSymptoms;
    @ColumnInfo
    Date detectionDate;
    @ColumnInfo
    Date limitDate;

    public CrohnState() {
        this.id = null;
        this.username = "";
        this.active = false;
        this.relatedSymptoms = "";
        this.detectionDate = null;
        this.limitDate = null;
    }

    @Ignore
    public CrohnState(Integer id, String username, Boolean active, String relatedSymptoms, Date detectionDate, Date limitDate) {
        this.id = id;
        this.username = username;
        this.active = active;
        this.relatedSymptoms = relatedSymptoms;
        this.detectionDate = detectionDate;
        this.limitDate = limitDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getRelatedSymptoms() {
        return relatedSymptoms;
    }

    public void setRelatedSymptoms(String relatedSymptoms) {
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
