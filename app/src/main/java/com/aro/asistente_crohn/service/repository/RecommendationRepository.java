package com.aro.asistente_crohn.service.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.aro.asistente_crohn.database.CrohnsAssistDatabase;
import com.aro.asistente_crohn.model.Recommendation;
import java.util.List;

public class RecommendationRepository {
    private LiveData<List<Recommendation>> allRecommendations;

    public RecommendationRepository(Application application) {
        CrohnsAssistDatabase db = CrohnsAssistDatabase.getDatabase(application);
        allRecommendations = db.recommendationDAO().getRecommendations();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Recommendation>> getAllRecommendations() {
        return allRecommendations;
    }
}