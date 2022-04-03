package com.aro.asistente_crohn.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.aro.asistente_crohn.database.CrohnsAssistDatabase;
import com.aro.asistente_crohn.database.SymptomDAO;
import com.aro.asistente_crohn.model.Symptom;

import java.util.Calendar;
import java.util.List;

public class SymptomRepository {
    private SymptomDAO symptomDao;
    private LiveData<List<Symptom>> allSymptoms;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public SymptomRepository(Application application) {
        CrohnsAssistDatabase db = CrohnsAssistDatabase.getDatabase(application);
        symptomDao = db.symptomDAO();
        allSymptoms = symptomDao.getSymptoms(Calendar.getInstance().getTime());
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Symptom>> getAllSymptoms() {
        return allSymptoms;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(Symptom symptom) {
        CrohnsAssistDatabase.databaseWriteExecutor.execute(() -> {
            symptomDao.insertSymptom(symptom);
        });
    }
}
