package com.aro.asistente_crohn.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.aro.asistente_crohn.model.Poop;

import java.util.List;

@Dao
public interface PoopDAO {
    @Query("SELECT * FROM Poop where id like :id")
    public Poop getById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long insertPoop(Poop poop);

    @Delete
    public void deletePoop(Poop poop);

    @Query("DELETE FROM Poop")
    public void deleteAll();

    @Query("SELECT * FROM Poop")
    LiveData<List<Poop>> getPoops();

    @Query("SELECT * FROM Poop WHERE registeredDate BETWEEN :before AND :after")
    LiveData<List<Poop>> getTodayPoops(Long before, Long after);
}