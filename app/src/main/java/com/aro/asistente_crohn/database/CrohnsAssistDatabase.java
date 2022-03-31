package com.aro.asistente_crohn.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.aro.asistente_crohn.model.CrohnState;
import com.aro.asistente_crohn.model.Food;
import com.aro.asistente_crohn.model.Recommendation;
import com.aro.asistente_crohn.model.Symptom;

// bump version number if the schema changes
@Database(entities={CrohnState.class, Food.class, Recommendation.class, Symptom.class}, version=2)
@TypeConverters({DateConverter.class})
public abstract class CrohnsAssistDatabase extends RoomDatabase {

    // Declaring data access objects as abstract
    public abstract CrohnStateDAO CrohnStateDao();

    public abstract FoodDAO FoodDao();

    public abstract RecommendationDAO recommendationDAO();

    public abstract SymptomDAO symptomDAO();

    // Database name to be used
    public static final String NAME = "CrohnsAssistDatabase";
}
