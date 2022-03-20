package com.aro.asistente_crohn.data;

import android.graphics.Picture;

import java.util.Date;

public class Symptom {
    private String id;
    String name;
    Picture picture;
    Date registeredDate;
    Date limitDate;

    public Symptom() {
    }

    public Symptom(String id, String name, Picture picture, Date registeredDate, Date limitDate) {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.registeredDate = registeredDate;
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

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
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
