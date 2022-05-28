package com.aro.asistente_crohn.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.aro.asistente_crohn.model.FoodRepo;
import com.aro.asistente_crohn.model.Recommendation;

import java.util.List;

@Dao
public interface RecommendationDAO {
    @Query("SELECT * FROM Recommendation where id like :id")
    public Recommendation getById(int id);

    @Query("SELECT * FROM Recommendation")
    LiveData<List<Recommendation>> getRecommendations();
}
