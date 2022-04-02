package com.aro.asistente_crohn.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.aro.asistente_crohn.model.Health;

@Dao
public interface HealthDAO {
    @Query("SELECT * FROM Health where id like :id")
    public Health getById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long insert(Health health);

    @Delete
    public void delete(Health health);
}
