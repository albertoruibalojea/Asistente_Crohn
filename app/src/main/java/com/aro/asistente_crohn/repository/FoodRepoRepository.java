package com.aro.asistente_crohn.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.aro.asistente_crohn.database.CrohnsAssistDatabase;
import com.aro.asistente_crohn.database.FoodRepoDAO;
import com.aro.asistente_crohn.model.FoodRepo;

import java.util.List;

public class FoodRepoRepository {
    private FoodRepoDAO foodRepoDAO;
    private LiveData<List<FoodRepo>> allFoods;


    public FoodRepoRepository(Application application) {
        CrohnsAssistDatabase db = CrohnsAssistDatabase.getDatabase(application);
        foodRepoDAO = db.foodRepoDAO();
        allFoods = foodRepoDAO.getFoods();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<FoodRepo>> getAllFoods() {
        return allFoods;
    }
}
