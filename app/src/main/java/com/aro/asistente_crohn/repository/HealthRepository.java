package com.aro.asistente_crohn.repository;

import android.app.Application;

import com.aro.asistente_crohn.database.CrohnsAssistDatabase;
import com.aro.asistente_crohn.database.HealthDAO;
import com.aro.asistente_crohn.model.Health;

public class HealthRepository {
    private HealthDAO healthDAO;
    private Health health;

    public HealthRepository(Application application) {
        CrohnsAssistDatabase db = CrohnsAssistDatabase.getDatabase(application);
        healthDAO = db.healthDAO();
        health = getHealth();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public Health getHealth() {
        return health;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(Health health) {
        CrohnsAssistDatabase.databaseWriteExecutor.execute(() -> healthDAO.insert(health));
    }
}
