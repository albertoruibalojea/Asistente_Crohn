package com.aro.asistente_crohn.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.aro.asistente_crohn.model.Recommendation;

@Dao
public interface RecommendationDAO {
    @Query("SELECT * FROM Recommendation where id like :id")
    public Recommendation getById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long insertRecommendation(Recommendation recommendation);

    @Delete
    public void deleteRecommendation(Recommendation recommendation);
}
