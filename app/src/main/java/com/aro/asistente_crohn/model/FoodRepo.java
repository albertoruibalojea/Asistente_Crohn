package com.aro.asistente_crohn.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FoodRepo {
    @ColumnInfo
    @PrimaryKey(autoGenerate=true)
    private Integer id;
    @ColumnInfo
    @NonNull
    private String name;

    public FoodRepo() {
        this.id = null;
        this.name = "";
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
}
