package com.aro.asistente_crohn.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Health {
    @ColumnInfo
    @PrimaryKey(autoGenerate=true)
    private Integer id;
    @ColumnInfo
    private Boolean crohnActive;
    @ColumnInfo
    private String relatedSymptoms;
    @ColumnInfo
    private Integer courage; //from 0-4, less to more happy
    @ColumnInfo
    Date detectionDate;
    @ColumnInfo
    Date limitDate;

    public Health() {
        this.id = null;
        this.crohnActive = false;
        this.relatedSymptoms = "";
        this.courage = 2;
        this.detectionDate = null;
        this.limitDate = null;
    }

    @Ignore
    public Health(Integer id, Boolean crohnActive, String relatedSymptoms, Integer courage, Date detectionDate, Date limitDate) {
        this.id = id;
        this.crohnActive = crohnActive;
        this.relatedSymptoms = relatedSymptoms;
        this.courage = courage;
        this.detectionDate = detectionDate;
        this.limitDate = limitDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getCrohnActive() {
        return crohnActive;
    }

    public void setCrohnActive(Boolean crohnActive) {
        this.crohnActive = crohnActive;
    }

    public String getRelatedSymptoms() {
        return relatedSymptoms;
    }

    public void setRelatedSymptoms(String relatedSymptoms) {
        this.relatedSymptoms = relatedSymptoms;
    }

    public Integer getCourage() {
        return courage;
    }

    public void setCourage(Integer courage) {
        this.courage = courage;
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
