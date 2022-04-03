package com.aro.asistente_crohn.model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.aro.asistente_crohn.repository.HealthRepository;
import com.aro.asistente_crohn.repository.SymptomRepository;

import java.util.List;

public class ItemViewModel extends AndroidViewModel {
    private SymptomRepository symptomRepository;
    private HealthRepository healthRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<Symptom>> allSymptoms;

    public ItemViewModel(Application application) {
        super(application);
        symptomRepository = new SymptomRepository(application);
        allSymptoms = symptomRepository.getAllSymptoms();

        healthRepository = new HealthRepository(application);
    }

    public LiveData<List<Symptom>> getAllSymptoms() {
        if (allSymptoms == null) {
            allSymptoms = symptomRepository.getAllSymptoms();
        }
        return allSymptoms;
    }

    public void insertSymptom(Symptom symptom) {
        symptomRepository.insert(symptom);
    }

    public void insertHealth(Health health){
        healthRepository.insert(health);
    }
}
