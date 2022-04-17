package com.aro.asistente_crohn.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.aro.asistente_crohn.database.CrohnsAssistDatabase;
import com.aro.asistente_crohn.database.DateConverter;
import com.aro.asistente_crohn.database.FoodDAO;
import com.aro.asistente_crohn.model.Food;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FoodRepository {
    private FoodDAO foodDao;
    private LiveData<List<Food>> allFoods;
    private LiveData<List<Food>> forbiddenFoods;
    private LiveData<List<Food>> todayFoods;

    public FoodRepository(Application application){
        CrohnsAssistDatabase db = CrohnsAssistDatabase.getDatabase(application);
        foodDao = db.foodDAO();
        allFoods = foodDao.getFoods();
        forbiddenFoods = foodDao.getForbiddenFoods();

        Date before = Calendar.getInstance().getTime();
        before.setHours(00); before.setMinutes(00); before.setSeconds(00);
        Date after = Calendar.getInstance().getTime();
        after.setHours(23); after.setMinutes(59); after.setSeconds(59);
        todayFoods = foodDao.getTodayFoods(DateConverter.fromDate(before), DateConverter.fromDate(after));
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Food>> getAllFoods() {
        return allFoods;
    }

    public LiveData<List<Food>> getTodayFoods(){
        return todayFoods;
    }

    public LiveData<List<Food>> getForbiddenFoods(){
        return forbiddenFoods;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(Food food) {
        CrohnsAssistDatabase.databaseWriteExecutor.execute(() -> foodDao.insertFood(food));
    }

    public void update(Food food) {
        CrohnsAssistDatabase.databaseWriteExecutor.execute(() -> foodDao.updateFood(food));
    }

    public void delete(Food food){
        CrohnsAssistDatabase.databaseWriteExecutor.execute(() -> foodDao.deleteFood(food));
    }
}
