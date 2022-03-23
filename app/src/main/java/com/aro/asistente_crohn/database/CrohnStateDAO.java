package com.aro.asistente_crohn.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.aro.asistente_crohn.model.CrohnState;

@Dao
public interface CrohnStateDAO {
    @Query("SELECT * FROM CrohnState where id like :id")
    public CrohnState getById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long insertState(CrohnState state);

    @Delete
    public void deleteState(CrohnState state);
}
