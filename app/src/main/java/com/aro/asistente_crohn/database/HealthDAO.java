package com.aro.asistente_crohn.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.aro.asistente_crohn.model.Food;
import com.aro.asistente_crohn.model.Health;

import java.util.List;

@Dao
public interface HealthDAO {
    @Query("SELECT * FROM Health where id like :id")
    public Health getById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long insert(Health health);

    @Delete
    public void delete(Health health);

    @Query("SELECT * FROM Health WHERE detectionDate BETWEEN :before AND :after")
    LiveData<List<Health>> getTodayHealth(Long before, Long after);
}
