package com.aro.asistente_crohn.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.aro.asistente_crohn.model.Food;

import java.util.List;

@Dao
public interface FoodDAO {
    @Query("SELECT * FROM Food where id like :id")
    public LiveData<Food> getById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long insertFood(Food food);

    @Delete
    public void deleteFood(Food food);

    @Update
    public void updateFood(Food food);

    @Query("SELECT * FROM Food")
    LiveData<List<Food>> getFoods();

    @Query("SELECT * FROM Food WHERE forbidden==1")
    LiveData<List<Food>> getForbiddenFoods();

    @Query("SELECT * FROM Food WHERE eatenDate BETWEEN :before AND :after")
    LiveData<List<Food>> getTodayFoods(Long before, Long after);
}
