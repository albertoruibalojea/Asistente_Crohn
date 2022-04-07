package com.aro.asistente_crohn.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.aro.asistente_crohn.model.Symptom;

import java.util.Date;
import java.util.List;

@Dao
public interface SymptomDAO {
    @Query("SELECT * FROM Symptom where id like :id")
    public Symptom getById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long insertSymptom(Symptom symptom);

    @Delete
    public void deleteSymptom(Symptom symptom);

    @Query("SELECT * FROM Symptom")
    LiveData<List<Symptom>> getSymptoms();
}
