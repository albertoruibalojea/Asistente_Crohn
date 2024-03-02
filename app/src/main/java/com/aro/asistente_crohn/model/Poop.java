package com.aro.asistente_crohn.model;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "Poop")
public class Poop {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @Nullable
    private Integer id;

    @NonNull
    @ColumnInfo(name = "registeredDate")
    private Date registeredDate;

    @NonNull
    @ColumnInfo(name = "limitDate")
    private Date limitDate;

    @NonNull
    @ColumnInfo(name = "color")
    private String color;

    @ColumnInfo(name = "notes")
    private String notes;

    @NonNull
    @ColumnInfo(name = "urgency")
    private Boolean urgency;

    @NonNull
    @ColumnInfo(name = "weight")
    private Integer weight;

    @NonNull
    @ColumnInfo(name = "type")
    private Integer type;

    @NonNull
    @ColumnInfo(name = "painful")
    private Boolean painful;

    @NonNull
    @ColumnInfo(name = "blood")
    private Boolean blood;

    //create constructores, one empty and the other with all attributes, using Symptom.class as reference
    public Poop() {
        this.id = null;
        this.registeredDate = Calendar.getInstance().getTime();
        this.type = 0;
        this.weight = 0;
        this.color = "brown";
        this.urgency = false;
        this.painful = false;
        this.blood = false;
        this.notes = "";

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(registeredDate);
        calendar.add(Calendar.DAY_OF_YEAR, 90);
        this.limitDate = calendar.getTime();
    }

    public Poop(Integer id, Date registeredDate, Integer type, Integer weight, String color, Boolean urgency, Boolean painful, Boolean blood, String notes) {
        this.id = id;
        this.registeredDate = registeredDate;
        this.type = type;
        this.weight = weight;
        this.color = color;
        this.urgency = urgency;
        this.painful = painful;
        this.blood = blood;
        this.notes = notes;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(registeredDate);
        calendar.add(Calendar.DAY_OF_YEAR, 90);
        this.limitDate = calendar.getTime();
    }

    //create getters and setters for all attributes

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    @NonNull
    public Date getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(@NonNull Date registeredDate) {
        this.registeredDate = registeredDate;
    }

    @NonNull
    public Date getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(@NonNull Date limitDate) {
        this.limitDate = limitDate;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @NonNull
    public String getColor() {
        return color;
    }

    public void setColor(@NonNull String color) {
        this.color = color;
    }

    public Boolean isUrgency() {
        return urgency;
    }

    public void setUrgency(Boolean urgency) {
        this.urgency = urgency;
    }

    public Boolean isPainful() {
        return painful;
    }

    public void setPainful(Boolean painful) {
        this.painful = painful;
    }

    public Boolean isBlood() {
        return blood;
    }

    public void setBlood(Boolean blood) {
        this.blood = blood;
    }

    @NonNull
    public String getNotes() {
        return notes;
    }

    public void setNotes(@NonNull String notes) {
        this.notes = notes;
    }
}