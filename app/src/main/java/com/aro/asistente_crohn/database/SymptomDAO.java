package com.aro.asistente_crohn.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.aro.asistente_crohn.model.Symptom;

import java.util.List;

@Dao
public interface SymptomDAO {
    @Query("SELECT * FROM Symptom where id like :id")
    public Symptom getById(int id);

    @Query("SELECT * FROM Symptom where name like :symptom")
    public LiveData<List<Symptom>> getBySymptom(String symptom);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long insertSymptom(Symptom symptom);

    @Delete
    public void deleteSymptom(Symptom symptom);

    @Query("SELECT * FROM Symptom")
    LiveData<List<Symptom>> getSymptoms();

    @Query("SELECT * FROM Symptom WHERE registeredDate BETWEEN :before AND :after")
    LiveData<List<Symptom>> getTodaySymptoms(Long before, Long after);
}
