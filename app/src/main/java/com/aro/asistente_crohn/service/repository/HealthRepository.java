package com.aro.asistente_crohn.service.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.aro.asistente_crohn.database.CrohnsAssistDatabase;
import com.aro.asistente_crohn.database.DateConverter;
import com.aro.asistente_crohn.database.HealthDAO;
import com.aro.asistente_crohn.model.Health;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HealthRepository {
    private HealthDAO healthDAO;
    private Health health;
    private LiveData<List<Health>> todayHealth;

    public HealthRepository(Application application) {
        CrohnsAssistDatabase db = CrohnsAssistDatabase.getDatabase(application);
        healthDAO = db.healthDAO();
        health = getHealth();

        Date before = Calendar.getInstance().getTime();
        before.setHours(00); before.setMinutes(00); before.setSeconds(00);
        Date after = Calendar.getInstance().getTime();
        after.setHours(23); after.setMinutes(59); after.setSeconds(59);
        todayHealth = healthDAO.getTodayHealth(DateConverter.fromDate(before), DateConverter.fromDate(after));
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public Health getHealth() {
        return health;
    }

    public LiveData<List<Health>> getTodayHealth(){
        return todayHealth;
    }

    public LiveData<List<Health>> getSelectedDayHealth(Date before, Date after){
        return healthDAO.getTodayHealth(DateConverter.fromDate(before), DateConverter.fromDate(after));
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(Health health) {
        CrohnsAssistDatabase.databaseWriteExecutor.execute(() -> healthDAO.insert(health));
    }

    public void update(Health health) {
        CrohnsAssistDatabase.databaseWriteExecutor.execute(() -> healthDAO.update(health));
    }

    public void deleteAll(){
        CrohnsAssistDatabase.databaseWriteExecutor.execute(() -> healthDAO.deleteAll());
    }
}
