package com.aro.asistente_crohn.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.aro.asistente_crohn.database.CrohnsAssistDatabase;
import com.aro.asistente_crohn.database.DateConverter;
import com.aro.asistente_crohn.database.SymptomDAO;
import com.aro.asistente_crohn.model.Symptom;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SymptomRepository {
    private SymptomDAO symptomDao;
    private LiveData<List<Symptom>> allSymptoms;
    private LiveData<List<Symptom>> todaySymptoms;


    public SymptomRepository(Application application) {
        CrohnsAssistDatabase db = CrohnsAssistDatabase.getDatabase(application);
        symptomDao = db.symptomDAO();
        allSymptoms = symptomDao.getSymptoms();

        Date before = Calendar.getInstance().getTime();
        before.setHours(00); before.setMinutes(00); before.setSeconds(00);
        Date after = Calendar.getInstance().getTime();
        after.setHours(23); after.setMinutes(59); after.setSeconds(59);
        todaySymptoms = symptomDao.getTodaySymptoms(DateConverter.fromDate(before), DateConverter.fromDate(after));
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Symptom>> getAllSymptoms() {
        return allSymptoms;
    }

    public LiveData<List<Symptom>> getTodaySymptoms(){
        return todaySymptoms;
    }

    public LiveData<List<Symptom>> getBySymptom(String symptom){
        return symptomDao.getBySymptom(symptom);
    }

    public LiveData<List<Symptom>> getSelectedDaySymptoms(Date before, Date after){
        return symptomDao.getTodaySymptoms(DateConverter.fromDate(before), DateConverter.fromDate(after));
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(Symptom symptom) {
        CrohnsAssistDatabase.databaseWriteExecutor.execute(() -> symptomDao.insertSymptom(symptom));
    }

    public void delete(Symptom symptom){
        CrohnsAssistDatabase.databaseWriteExecutor.execute(() -> symptomDao.deleteSymptom(symptom));
    }
}
