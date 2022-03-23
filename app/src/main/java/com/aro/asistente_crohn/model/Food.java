package com.aro.asistente_crohn.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Food {
    @ColumnInfo
    @PrimaryKey(autoGenerate=true)
    private Integer id;
    @ColumnInfo
    private String name;
    @ColumnInfo
    Boolean forbidden;
    @ColumnInfo
    Date eatenDate;
    @ColumnInfo
    Date limitDate;

    public Food() {
        this.id = null;
        this.name = "";
        this.forbidden = false;
        this.eatenDate = null;
        this.limitDate = null;
    }

    @Ignore
    public Food(Integer id, String name, Boolean forbidden, Date eatenDate, Date limitDate) {
        this.id = id;
        this.name = name;
        this.forbidden = forbidden;
        this.eatenDate = eatenDate;
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

    public Boolean getForbidden() {
        return forbidden;
    }

    public void setForbidden(Boolean forbidden) {
        this.forbidden = forbidden;
    }

    public Date getEatenDate() {
        return eatenDate;
    }

    public void setEatenDate(Date eatenDate) {
        this.eatenDate = eatenDate;
    }

    public Date getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(Date limitDate) {
        this.limitDate = limitDate;
    }
}
