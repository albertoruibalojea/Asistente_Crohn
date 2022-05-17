package com.aro.asistente_crohn.model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.aro.asistente_crohn.repository.FoodRepoRepository;
import com.aro.asistente_crohn.repository.FoodRepository;
import com.aro.asistente_crohn.repository.HealthRepository;
import com.aro.asistente_crohn.repository.SymptomRepository;

import java.util.Date;
import java.util.List;

public class ItemViewModel extends AndroidViewModel {
    private SymptomRepository symptomRepository;
    private HealthRepository healthRepository;
    private FoodRepoRepository foodRepoRepository;
    private FoodRepository foodRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<Symptom>> allSymptoms;
    private LiveData<List<Symptom>> todaySymptoms;

    private LiveData<List<Food>> todayFoods;
    private LiveData<List<Food>> allEatenFoods;
    private LiveData<List<Food>> forbiddenFoods;
    private LiveData<List<FoodRepo>> allFoods;

    private LiveData<List<Health>> todayHealth;


    public ItemViewModel(Application application) {
        super(application);
        symptomRepository = new SymptomRepository(application);
        allSymptoms = symptomRepository.getAllSymptoms();
        todaySymptoms = symptomRepository.getTodaySymptoms();

        foodRepoRepository = new FoodRepoRepository(application);
        foodRepository = new FoodRepository(application);
        allFoods = foodRepoRepository.getAllFoods();
        allEatenFoods = foodRepository.getAllFoods();
        todayFoods = foodRepository.getTodayFoods();
        forbiddenFoods = foodRepository.getForbiddenFoods();

        healthRepository = new HealthRepository(application);
        todayHealth = healthRepository.getTodayHealth();
    }

    public LiveData<List<Symptom>> getAllSymptoms() {
        if (allSymptoms == null) {
            allSymptoms = symptomRepository.getAllSymptoms();
        }
        return allSymptoms;
    }

    public LiveData<List<Symptom>> getTodaySymptoms(){
        if (todaySymptoms == null) {
            todaySymptoms = symptomRepository.getTodaySymptoms();
        }
        return todaySymptoms;
    }

    public LiveData<List<Symptom>> getSelectedDaySymptoms(Date before, Date after){
        return symptomRepository.getSelectedDaySymptoms(before, after);
    }

    public LiveData<List<Symptom>> getBySymptom(String symptom){
        return symptomRepository.getBySymptom(symptom);
    }

    public void insertSymptom(Symptom symptom) {
        symptomRepository.insert(symptom);
    }
    public void deleteSymptom(Symptom symptom){
        symptomRepository.delete(symptom);
    }



    public LiveData<Food> getFoodById(Integer id){
        return foodRepository.getById(id);
    }

    public LiveData<List<FoodRepo>> getAllRepoFoods() {
        if (allFoods == null) {
            allFoods = foodRepoRepository.getAllFoods();
        }
        return allFoods;
    }

    public LiveData<List<Food>> getAllEatenFoods() {
        if (allEatenFoods == null) {
            allEatenFoods = foodRepository.getAllFoods();
        }
        return allEatenFoods;
    }

    public LiveData<List<Food>> getTodayFoods(){
        if (todayFoods == null) {
            todayFoods = foodRepository.getTodayFoods();
        }
        return todayFoods;
    }

    public LiveData<List<Food>> getSelectedDayFoods(Date before, Date after){
        return foodRepository.getSelectedDayFoods(before, after);
    }

    public LiveData<List<Food>> getForbiddenFoods() {
        if (forbiddenFoods == null) {
            forbiddenFoods = foodRepository.getForbiddenFoods();
        }
        return forbiddenFoods;
    }

    public void insertFood(Food food) { foodRepository.insert(food); }
    public void deleteFood(Food food){
        foodRepository.delete(food);
    }
    public void updateFood(Food food) { foodRepository.update(food);}



    public LiveData<List<Health>> getSelectedDayHealth(Date before, Date after){
        return healthRepository.getSelectedDayHealth(before, after);
    }

    public LiveData<List<Health>> getTodayHealth(){
        if (todayHealth == null) {
            todayHealth = healthRepository.getTodayHealth();
        }
        return todayHealth;
    }


    public void insertHealth(Health health){
        healthRepository.insert(health);
    }
    public void updateHealth(Health health) { healthRepository.update(health);}



    //WARNING THIS STATEMENT DELETES ALL DATA FROM DB
    public void deleteAllDBData(){
        foodRepository.deleteAll();
        healthRepository.deleteAll();
        symptomRepository.deleteAll();
    }
}
