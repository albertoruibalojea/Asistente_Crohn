package com.aro.asistente_crohn.data;

import java.util.ArrayList;
import java.util.Date;

public class User {
    private String name;
    private ArrayList<Symptom> symptoms;
    private ArrayList<CrohnState> states;
    private ArrayList<Food> foods;
    private ArrayList<Food> forbidden;

    public User() {
    }

    public User(String name) {
        this.name = name;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Symptom> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(ArrayList<Symptom> symptoms) {
        this.symptoms = symptoms;
    }

    public ArrayList<CrohnState> getStates() {
        return states;
    }

    public void setStates(ArrayList<CrohnState> states) {
        this.states = states;
    }

    public ArrayList<Food> getFoods() {
        return foods;
    }

    public void setFoods(ArrayList<Food> foods) {
        this.foods = foods;
    }

    public ArrayList<Food> getForbidden() {
        return forbidden;
    }

    public void setForbidden(ArrayList<Food> forbidden) {
        this.forbidden = forbidden;
    }
}
