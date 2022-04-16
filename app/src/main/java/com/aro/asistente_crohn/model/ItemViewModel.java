package com.aro.asistente_crohn.model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.aro.asistente_crohn.repository.FoodRepoRepository;
import com.aro.asistente_crohn.repository.HealthRepository;
import com.aro.asistente_crohn.repository.SymptomRepository;

import java.util.List;

public class ItemViewModel extends AndroidViewModel {
    private SymptomRepository symptomRepository;
    private HealthRepository healthRepository;
    private FoodRepoRepository foodRepoRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<Symptom>> allSymptoms;
    private LiveData<List<FoodRepo>> allFoods;
    private LiveData<List<Symptom>> todaySymptoms;

    public ItemViewModel(Application application) {
        super(application);
        symptomRepository = new SymptomRepository(application);
        allSymptoms = symptomRepository.getAllSymptoms();
        todaySymptoms = symptomRepository.getTodaySymptoms();
        healthRepository = new HealthRepository(application);
        foodRepoRepository = new FoodRepoRepository(application);
    }

    public LiveData<List<Symptom>> getAllSymptoms() {
        if (allSymptoms == null) {
            allSymptoms = symptomRepository.getAllSymptoms();
        }
        return allSymptoms;
    }

    public LiveData<List<FoodRepo>> getAllFoods() {
        if (allFoods == null) {
            allFoods = foodRepoRepository.getAllFoods();
        }
        return allFoods;
    }

    public LiveData<List<Symptom>> getTodaySymptoms(){
        if (todaySymptoms == null) {
            todaySymptoms = symptomRepository.getTodaySymptoms();
        }
        return todaySymptoms;
    }

    public void insertSymptom(Symptom symptom) {
        symptomRepository.insert(symptom);
    }

    public void insertHealth(Health health){
        healthRepository.insert(health);
    }

    public void deleteSymptom(Symptom symptom){
        symptomRepository.delete(symptom);
    }
}
