package com.aro.asistente_crohn.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.aro.asistente_crohn.database.CrohnsAssistDatabase;
import com.aro.asistente_crohn.database.HealthDAO;
import com.aro.asistente_crohn.model.Health;
import com.aro.asistente_crohn.model.Symptom;

import java.util.Calendar;
import java.util.List;

public class HealthRepository {
    private HealthDAO healthDAO;
    private Health health;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
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
        CrohnsAssistDatabase.databaseWriteExecutor.execute(() -> {
            healthDAO.insert(health);
        });
    }
}
