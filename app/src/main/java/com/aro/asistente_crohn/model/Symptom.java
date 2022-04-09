package com.aro.asistente_crohn.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Symptom {
    @ColumnInfo
    @PrimaryKey(autoGenerate=true)
    private Integer id;
    @ColumnInfo
    String name;
    @ColumnInfo
    Date registeredDate;
    @ColumnInfo
    Date limitDate;

    public Symptom() {
        this.id = null;
        this.name = "";
        this.registeredDate = null;
        this.limitDate = null;
    }

    @Ignore
    public Symptom(String name, Date registeredDate, Date limitDate) {
        this.id = null;
        this.name = name;
        this.registeredDate = registeredDate;
        this.limitDate = limitDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Date registeredDate) {
        this.registeredDate = registeredDate;
    }

    public Date getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(Date limitDate) {
        this.limitDate = limitDate;
    }
}
