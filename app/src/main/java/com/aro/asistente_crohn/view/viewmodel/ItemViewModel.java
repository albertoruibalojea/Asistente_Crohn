package com.aro.asistente_crohn.view.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.aro.asistente_crohn.model.Food;
import com.aro.asistente_crohn.model.FoodRepo;
import com.aro.asistente_crohn.model.Health;
import com.aro.asistente_crohn.model.Poop;
import com.aro.asistente_crohn.model.Recommendation;
import com.aro.asistente_crohn.model.Symptom;
import com.aro.asistente_crohn.service.repository.FoodRepoRepository;
import com.aro.asistente_crohn.service.repository.FoodRepository;
import com.aro.asistente_crohn.service.repository.HealthRepository;
import com.aro.asistente_crohn.service.repository.PoopRepository;
import com.aro.asistente_crohn.service.repository.RecommendationRepository;
import com.aro.asistente_crohn.service.repository.SymptomRepository;

import java.util.Date;
import java.util.List;

public class ItemViewModel extends AndroidViewModel {
    private SymptomRepository symptomRepository;
    private HealthRepository healthRepository;
    private FoodRepoRepository foodRepoRepository;
    private FoodRepository foodRepository;
    private RecommendationRepository recommendationRepository;

    private PoopRepository poopRepository;
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

    private LiveData<List<Recommendation>> allRecommendations;

    private LiveData<List<Poop>> allPoops;
    private LiveData<List<Poop>> todayPoops;

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

        recommendationRepository = new RecommendationRepository(application);
        allRecommendations = recommendationRepository.getAllRecommendations();

        poopRepository = new PoopRepository(application);
        allPoops = poopRepository.getAllPoops();
        todayPoops = poopRepository.getTodayPoops();
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

    public void insertFoodRepo(FoodRepo food) { foodRepoRepository.insert(food); }

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


    public LiveData<List<Recommendation>> getAllRecommendations() {
        if (allRecommendations == null) {
            allRecommendations = recommendationRepository.getAllRecommendations();
        }
        return allRecommendations;
    }


    public LiveData<List<Poop>> getAllPoops() {
        if (allPoops == null) {
            allPoops = poopRepository.getAllPoops();
        }
        return allPoops;
    }

    public LiveData<List<Poop>> getTodayPoops(){
        if (todayPoops == null) {
            todayPoops = poopRepository.getTodayPoops();
        }
        return todayPoops;
    }

    public LiveData<List<Poop>> getSelectedDayPoops(Date before, Date after){
        return poopRepository.getSelectedDayPoops(before, after);
    }

    public void insertPoop(Poop poop) {
        poopRepository.insert(poop);
    }

    public void deletePoop(Poop poop){
        poopRepository.delete(poop);
    }


    //WARNING THIS STATEMENT DELETES ALL DATA FROM DB
    public void deleteAllDBData(){
        foodRepository.deleteAll();
        healthRepository.deleteAll();
        symptomRepository.deleteAll();
    }
}
