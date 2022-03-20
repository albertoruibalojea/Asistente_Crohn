package com.aro.asistente_crohn.data;

import java.util.Date;

public class Food {
    private String id;
    private String name;
    Boolean forbidden;
    Date eatenDate;
    Date limitDate;

    public Food() {
    }

    public Food(String id, String name, Boolean forbidden, Date eatenDate, Date limitDate) {
        this.id = id;
        this.name = name;
        this.forbidden = forbidden;
        this.eatenDate = eatenDate;
        this.limitDate = limitDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
