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
    @Query("SELECT * FROM Symptom where id like :id")
    public Poop getById(int id);

    @Query("SELECT * FROM Symptom where name like :symptom")
    public LiveData<List<Poop>> getByPoop(String symptom);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long insertPoop(Poop symptom);

    @Delete
    public void deletePoop(Poop symptom);

    @Query("DELETE FROM Symptom")
    public void deleteAll();

    @Query("SELECT * FROM Symptom")
    LiveData<List<Poop>> getPoops();

    @Query("SELECT * FROM Symptom WHERE registeredDate BETWEEN :before AND :after")
    LiveData<List<Poop>> getTodayPoops(Long before, Long after);
}