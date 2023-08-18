package com.aro.asistente_crohn.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.aro.asistente_crohn.model.Food;
import com.aro.asistente_crohn.model.FoodRepo;
import com.aro.asistente_crohn.model.Symptom;

import java.util.List;

@Dao
public interface FoodRepoDAO {
    @Query("SELECT * FROM FoodRepo")
    LiveData<List<FoodRepo>> getFoods();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long insertFood(FoodRepo food);
}
