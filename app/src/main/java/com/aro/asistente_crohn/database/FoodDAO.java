package com.aro.asistente_crohn.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.aro.asistente_crohn.model.Food;

@Dao
public interface FoodDAO {
    @Query("SELECT * FROM Food where id like :id")
    public Food getById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long insertFood(Food food);

    @Delete
    public void deleteFood(Food food);
}
