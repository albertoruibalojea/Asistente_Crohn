package com.aro.asistente_crohn.service.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.aro.asistente_crohn.database.CrohnsAssistDatabase;
import com.aro.asistente_crohn.database.DateConverter;
import com.aro.asistente_crohn.database.PoopDAO;
import com.aro.asistente_crohn.model.Poop;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PoopRepository {
    private PoopDAO poopDao;
    private LiveData<List<Poop>> allPoops;
    private LiveData<List<Poop>> todayPoops;


    public PoopRepository(Application application) {
        CrohnsAssistDatabase db = CrohnsAssistDatabase.getDatabase(application);
        poopDao = db.poopDAO();
        allPoops = poopDao.getPoops();

        Date before = Calendar.getInstance().getTime();
        before.setHours(00); before.setMinutes(00); before.setSeconds(00);
        Date after = Calendar.getInstance().getTime();
        after.setHours(23); after.setMinutes(59); after.setSeconds(59);
        todayPoops = poopDao.getTodayPoops(DateConverter.fromDate(before), DateConverter.fromDate(after));
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Poop>> getAllPoops() {
        return allPoops;
    }

    public LiveData<List<Poop>> getTodayPoops(){
        return todayPoops;
    }

    public LiveData<List<Poop>> getSelectedDayPoops(Date before, Date after){
        return poopDao.getTodayPoops(DateConverter.fromDate(before), DateConverter.fromDate(after));
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(Poop poop) {
        CrohnsAssistDatabase.databaseWriteExecutor.execute(() -> poopDao.insertPoop(poop));
    }

    public void delete(Poop poop){
        CrohnsAssistDatabase.databaseWriteExecutor.execute(() -> poopDao.deletePoop(poop));
    }

    public void deleteAll(){
        CrohnsAssistDatabase.databaseWriteExecutor.execute(poopDao::deleteAll);
    }
}
