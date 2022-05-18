package com.aro.asistente_crohn.expert;

import android.content.SharedPreferences;

import androidx.lifecycle.LifecycleOwner;

import com.aro.asistente_crohn.model.Health;
import com.aro.asistente_crohn.model.ItemViewModel;
import com.aro.asistente_crohn.model.Symptom;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CrohnAnalyzer {
    /*
    * Se crean los patrones de brotes
    * - ArrayList con sus nombres como const
    *   - (generic)     diarrea, dolor abdominal, cansancio, menos peso, sangre, menos apetito, dolor articular, dolor cabeza, piel
    *   - (int delgado) diarrea, dolor abdominal, cansancio, fiebre, menos peso, aftas, piel
    *   - (colon)       diarrea, dolor abdominal, fiebre, sangre, ir más al baño, aftas, piel
    *   - (estomac/sup) dolor abdominal, fiebre, nauseas, menos apetito, menos peso, aftas, piel
    *   - (perianal)    diarrea, dolor anal, sangrado, herida perianal, piel
    *
    * Para cada día D CON REGISTROS de síntomas
    * - Se analizan sus síntomas si Health.active==false && Health.relatedSymptoms==null && PREV_DAYs_Health.active==false
    *   - Se comprueban los síntomas de D según los patrones en cuestión (1 met por patron)
    *   - Si resulta que hay positivo en D:
    *     - Se notifica que para el día D hay un positivo en la App
    *     - Se pone EL PATRÓN del positivo en el array de Health.relatedSymptoms para el día D
    *     - Ahora es necesario ver los días anteriores
    *       - DIAS_A_REVISAR_HACIA_ATRAS = Variable en Ajustes, default=3
    *       - Si se repite dicho patrón (ver Health.relatedSymptoms) en más de DIAS_A_REVISAR_HACIA_ATRAS días => BROTE
    *         - Se actualiza D y PREV_DAYs a Health.active=true
    *         - Notificar al usuario vía notificación y en App
    * - Si es Health.active==false && PREV_DAYs_Health.active==TRUE
    *   - Se analizan los síntomas del PATRÓN de los PREV_DAYs
    *   - Si resulta que todavía hay positivo en D:
    *     - Se pone EL PATRÓN en el array de Health.relatedSymptoms para el día D
    *     - Se actualiza D a Health.active=true
    *     - Notificar al usuario vía App
    * */

    private final ItemViewModel viewModel;
    private final LifecycleOwner lifecycleOwner;
    private final SharedPreferences preferences;
    private static final String DAYS_TO_ANALYZE = "daysToAnalyze";

    public CrohnAnalyzer(ItemViewModel viewModel, LifecycleOwner lifecycleOwner, SharedPreferences preferences){
        this.viewModel = viewModel;
        this.lifecycleOwner = lifecycleOwner;
        this.preferences = preferences;
    }

    public void analyze(){

        viewModel.getTodayHealth().observe(this.lifecycleOwner, healthList -> {
            List<Health> cacheTodayHealthList = new ArrayList<>();
            Health health = new Health();
            if (!healthList.isEmpty()) {
                cacheTodayHealthList = healthList;
                health = cacheTodayHealthList.get(0);
            }

            if(!health.getCrohnActive() && health.getRelatedSymptoms().isEmpty() && !this.isActivePrevDays()){
                //Get today´s symptoms
                Health finalHealth = health;
                this.viewModel.getTodaySymptoms().observe(this.lifecycleOwner, symptomList -> {
                    List<Symptom> cacheTodaySymptomList = new ArrayList<>();
                    if (!symptomList.isEmpty()) {
                        cacheTodaySymptomList.addAll(symptomList);
                    }

                    //We check today´s symptoms looking for occurrences in any pattern
                    //If coincidence, the Observer in the Health fragment will notify the user via interface
                    //If the pattern is repeated a few days before, it means a critical state (ACTIVE) and must notify via push too
                    if(this.isPatternGeneric(cacheTodaySymptomList)){
                        finalHealth.setRelatedSymptoms(SymptomConstants.PATTERN_GENERIC);
                        viewModel.updateHealth(finalHealth);
                        if(this.isSamePattern(SymptomConstants.PATTERN_GENERIC)){
                            this.setActivePrevDays(SymptomConstants.PATTERN_GENERIC);
                        }
                    } else if(this.isPatternSmallBowel(cacheTodaySymptomList)){
                        finalHealth.setRelatedSymptoms(SymptomConstants.PATTERN_SMALL_BOWEL);
                        viewModel.updateHealth(finalHealth);
                        if(this.isSamePattern(SymptomConstants.PATTERN_SMALL_BOWEL)){
                            this.setActivePrevDays(SymptomConstants.PATTERN_SMALL_BOWEL);
                        }
                    } else if(this.isPatternColon(cacheTodaySymptomList)){
                        finalHealth.setRelatedSymptoms(SymptomConstants.PATTERN_COLON);
                        viewModel.updateHealth(finalHealth);
                        if(this.isSamePattern(SymptomConstants.PATTERN_COLON)){
                            this.setActivePrevDays(SymptomConstants.PATTERN_COLON);
                        }
                    } else if(this.isPatternUpperTract(cacheTodaySymptomList)){
                        finalHealth.setRelatedSymptoms(SymptomConstants.PATTERN_UPPER_TRACT);
                        viewModel.updateHealth(finalHealth);
                        if(this.isSamePattern(SymptomConstants.PATTERN_UPPER_TRACT)){
                            this.setActivePrevDays(SymptomConstants.PATTERN_UPPER_TRACT);
                        }
                    } else if(this.isPatternPerianal(cacheTodaySymptomList)){
                        finalHealth.setRelatedSymptoms(SymptomConstants.PATTERN_PERIANAL);
                        viewModel.updateHealth(finalHealth);
                        if(this.isSamePattern(SymptomConstants.PATTERN_PERIANAL)){
                            this.setActivePrevDays(SymptomConstants.PATTERN_PERIANAL);
                        }
                    }

                });
            } else if(!health.getCrohnActive() && this.isActivePrevDays()){
                //In this case, the disease is showing a pattern in the last days
                //So we just check that pattern
                String actualPattern = this.getActualPattern();
                Health finalHealth = health;
                this.viewModel.getTodaySymptoms().observe(this.lifecycleOwner, symptomList -> {
                    List<Symptom> cacheTodaySymptomList = new ArrayList<>();
                    if (symptomList != null) {
                        cacheTodaySymptomList.addAll(symptomList);
                    }

                    //If the pattern is repeated, it means user is still having a critical state (ACTIVE) and must notify via app only
                    if(actualPattern.equalsIgnoreCase(SymptomConstants.PATTERN_GENERIC) && this.isPatternGeneric(cacheTodaySymptomList)){
                        finalHealth.setRelatedSymptoms(SymptomConstants.PATTERN_GENERIC);
                        this.setActivePrevDays(SymptomConstants.PATTERN_GENERIC);
                        viewModel.updateHealth(finalHealth);
                    } else if(actualPattern.equalsIgnoreCase(SymptomConstants.PATTERN_SMALL_BOWEL) && this.isPatternSmallBowel(cacheTodaySymptomList)){
                        finalHealth.setRelatedSymptoms(SymptomConstants.PATTERN_SMALL_BOWEL);
                        this.setActivePrevDays(SymptomConstants.PATTERN_SMALL_BOWEL);
                        viewModel.updateHealth(finalHealth);
                    } else if(actualPattern.equalsIgnoreCase(SymptomConstants.PATTERN_COLON) && this.isPatternColon(cacheTodaySymptomList)){
                        finalHealth.setRelatedSymptoms(SymptomConstants.PATTERN_COLON);
                        this.setActivePrevDays(SymptomConstants.PATTERN_COLON);
                        viewModel.updateHealth(finalHealth);
                    } else if(actualPattern.equalsIgnoreCase(SymptomConstants.PATTERN_UPPER_TRACT) && this.isPatternUpperTract(cacheTodaySymptomList)){
                        finalHealth.setRelatedSymptoms(SymptomConstants.PATTERN_UPPER_TRACT);
                        this.setActivePrevDays(SymptomConstants.PATTERN_UPPER_TRACT);
                        viewModel.updateHealth(finalHealth);
                    } else if(actualPattern.equalsIgnoreCase(SymptomConstants.PATTERN_PERIANAL) && this.isPatternPerianal(cacheTodaySymptomList)){
                        finalHealth.setRelatedSymptoms(SymptomConstants.PATTERN_PERIANAL);
                        this.setActivePrevDays(SymptomConstants.PATTERN_PERIANAL);
                        viewModel.updateHealth(finalHealth);
                    }
                });
            }
        });


    }

    private void setActivePrevDays(String pattern){
        int prevDays = Integer.parseInt(this.preferences.getString(DAYS_TO_ANALYZE, null));

        //For the previous days, we set Health.active=true and Health.relatedSymptoms=pattern
        for(int i=1; i<=prevDays; i++){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, -i);

            Date before = cal.getTime();
            before.setHours(00); before.setMinutes(00); before.setSeconds(00);
            Date after = cal.getTime();
            after.setHours(23); after.setMinutes(59); after.setSeconds(59);

            this.viewModel.getSelectedDayHealth(before, after).observe(this.lifecycleOwner, healthList -> {
                List<Health> cacheTodayHealthList = new ArrayList<>();
                Health health = new Health();
                if (healthList != null) {
                    cacheTodayHealthList = healthList;
                    health = cacheTodayHealthList.get(0);

                    //Health.active=true
                    health.setCrohnActive(true);
                    //Health.relatedSymptoms=pattern
                    health.setRelatedSymptoms(pattern);

                    viewModel.updateHealth(health);
                }
            });
        }
    }

    private boolean isActivePrevDays(){
        int prevDays = Integer.parseInt(this.preferences.getString(DAYS_TO_ANALYZE, null));
        List<Boolean> positivity = new ArrayList<>();

        //For the previous days, we check if Health.crohnActive is true
        for(int i=1; i<=prevDays; i++){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, -i);

            Date before = cal.getTime();
            before.setHours(00); before.setMinutes(00); before.setSeconds(00);
            Date after = cal.getTime();
            after.setHours(23); after.setMinutes(59); after.setSeconds(59);

            this.viewModel.getSelectedDayHealth(before, after).observe(this.lifecycleOwner, healthList -> {
                List<Health> cacheTodayHealthList = new ArrayList<>();
                Health health = new Health();
                if (!healthList.isEmpty()) {
                    cacheTodayHealthList = healthList;
                    health = cacheTodayHealthList.get(0);

                    //we add the status of the disease for this day to the positivity list
                    positivity.add(health.getCrohnActive());
                }
            });
        }

        //Now we just check if there are more "true" than "false" in the positivity list
        int occurrences = Collections.frequency(positivity, true);
        return occurrences >= prevDays;
    }

    private boolean isSamePattern(String pattern){
        int prevDays = Integer.parseInt(this.preferences.getString(DAYS_TO_ANALYZE, null));
        AtomicInteger occurrences = new AtomicInteger();

        //For the previous days, we check if Health.relatedSymptoms is the same
        for(int i=1; i<=prevDays; i++){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, -i);

            Date before = cal.getTime();
            before.setHours(00); before.setMinutes(00); before.setSeconds(00);
            Date after = cal.getTime();
            after.setHours(23); after.setMinutes(59); after.setSeconds(59);

            this.viewModel.getSelectedDayHealth(before, after).observe(this.lifecycleOwner, healthList -> {
                List<Health> cacheTodayHealthList = new ArrayList<>();
                Health health = new Health();
                if (!healthList.isEmpty()) {
                    cacheTodayHealthList = healthList;
                    health = cacheTodayHealthList.get(0);

                    //we compare the pattern
                    if(health.getRelatedSymptoms().equalsIgnoreCase(pattern)){
                        occurrences.addAndGet(1);
                    }
                }
            });
        }

        return occurrences.get() >= prevDays;
    }

    private String getActualPattern(){
        if(this.isSamePattern(SymptomConstants.PATTERN_GENERIC)){
            return SymptomConstants.PATTERN_GENERIC;
        } else if(this.isSamePattern(SymptomConstants.PATTERN_SMALL_BOWEL)){
            return SymptomConstants.PATTERN_SMALL_BOWEL;
        } else if(this.isSamePattern(SymptomConstants.PATTERN_COLON)){
            return SymptomConstants.PATTERN_COLON;
        } else if(this.isSamePattern(SymptomConstants.PATTERN_UPPER_TRACT)){
            return SymptomConstants.PATTERN_UPPER_TRACT;
        } else if(this.isSamePattern(SymptomConstants.PATTERN_PERIANAL)){
            return SymptomConstants.PATTERN_PERIANAL;
        }

        return null;
    }

    private boolean isPatternGeneric(List<Symptom> symptoms){
        int positivity = 0;

        for(Symptom s : symptoms){
            //We iterate
            if(s.getName().equalsIgnoreCase(SymptomConstants.DIAREE)){
                positivity += SymptomConstants.VALUE_DIAREE;
            }
            if(s.getName().equalsIgnoreCase(SymptomConstants.ABDOMINAL_PAIN)){
                positivity += SymptomConstants.VALUE_ABDOMINAL_PAIN;
            }
            if(s.getName().equalsIgnoreCase(SymptomConstants.TIRED)){
                positivity += SymptomConstants.VALUE_TIRED;
            }
            if(s.getName().equalsIgnoreCase(SymptomConstants.LESS_WEIGHT)){
                positivity += SymptomConstants.VALUE_LESS_WEIGHT;
            }
            if(s.getName().equalsIgnoreCase(SymptomConstants.BLOOD)){
                positivity += SymptomConstants.VALUE_BLOOD;
            }
            if(s.getName().equalsIgnoreCase(SymptomConstants.NOT_HUNGRY)){
                positivity += SymptomConstants.VALUE_NOT_HUNGRY;
            }
            if(s.getName().equalsIgnoreCase(SymptomConstants.ARTICULAR_PAIN)){
                positivity += SymptomConstants.VALUE_ARTICULAR_PAIN;
            }
            if(s.getName().equalsIgnoreCase(SymptomConstants.HEADACHE)){
                positivity += SymptomConstants.VALUE_HEADACHE;
            }
            if(s.getName().equalsIgnoreCase(SymptomConstants.SKIN_ISSUE)){
                positivity += SymptomConstants.VALUE_SKIN_ISSUE;
            }
        }

        //If the positivity is higher than 50% points of the pattern, we must notify
        return positivity >= SymptomConstants.VALUE_PATTERN_GENERIC;
    }

    private boolean isPatternSmallBowel(List<Symptom> symptoms){
        int positivity = 0;

        for(Symptom s : symptoms){
            //We iterate
            if(s.getName().equalsIgnoreCase(SymptomConstants.DIAREE)){
                positivity += SymptomConstants.VALUE_DIAREE;
            }
            if(s.getName().equalsIgnoreCase(SymptomConstants.ABDOMINAL_PAIN)){
                positivity += SymptomConstants.VALUE_ABDOMINAL_PAIN;
            }
            if(s.getName().equalsIgnoreCase(SymptomConstants.TIRED)){
                positivity += SymptomConstants.VALUE_TIRED;
            }
            if(s.getName().equalsIgnoreCase(SymptomConstants.FEVER)){
                positivity += SymptomConstants.VALUE_FEVER;
            }
            if(s.getName().equalsIgnoreCase(SymptomConstants.LESS_WEIGHT)){
                positivity += SymptomConstants.VALUE_LESS_WEIGHT;
            }
            if(s.getName().equalsIgnoreCase(SymptomConstants.APHTHAS)){
                positivity += SymptomConstants.VALUE_APHTHAS;
            }
            if(s.getName().equalsIgnoreCase(SymptomConstants.SKIN_ISSUE)){
                positivity += SymptomConstants.VALUE_SKIN_ISSUE;
            }
        }

        //If the positivity is higher than 50% points of the pattern, we must notify
        return positivity >= SymptomConstants.VALUE_PATTERN_SMALL_BOWEL;
    }

    private boolean isPatternColon(List<Symptom> symptoms){
        int positivity = 0;

        for(Symptom s : symptoms){
            //We iterate
            if(s.getName().equalsIgnoreCase(SymptomConstants.DIAREE)){
                positivity += SymptomConstants.VALUE_DIAREE;
            }
            if(s.getName().equalsIgnoreCase(SymptomConstants.ABDOMINAL_PAIN)){
                positivity += SymptomConstants.VALUE_ABDOMINAL_PAIN;
            }
            if(s.getName().equalsIgnoreCase(SymptomConstants.FEVER)){
                positivity += SymptomConstants.VALUE_FEVER;
            }
            if(s.getName().equalsIgnoreCase(SymptomConstants.BLOOD)){
                positivity += SymptomConstants.VALUE_BLOOD;
            }
            if(s.getName().equalsIgnoreCase(SymptomConstants.BATHROOM_URGE)){
                positivity += SymptomConstants.VALUE_BATHROOM_URGE;
            }
            if(s.getName().equalsIgnoreCase(SymptomConstants.APHTHAS)){
                positivity += SymptomConstants.VALUE_APHTHAS;
            }
            if(s.getName().equalsIgnoreCase(SymptomConstants.SKIN_ISSUE)){
                positivity += SymptomConstants.VALUE_SKIN_ISSUE;
            }
        }

        //If the positivity is higher than 50% points of the pattern, we must notify
        return positivity >= SymptomConstants.VALUE_PATTERN_COLON;
    }

    private boolean isPatternUpperTract(List<Symptom> symptoms){
        int positivity = 0;

        for(Symptom s : symptoms){
            //We iterate
            if(s.getName().equalsIgnoreCase(SymptomConstants.FEVER)){
                positivity += SymptomConstants.VALUE_FEVER;
            }
            if(s.getName().equalsIgnoreCase(SymptomConstants.NAUSEAS)){
                positivity += SymptomConstants.VALUE_NAUSEAS;
            }
            if(s.getName().equalsIgnoreCase(SymptomConstants.NOT_HUNGRY)){
                positivity += SymptomConstants.VALUE_NOT_HUNGRY;
            }
            if(s.getName().equalsIgnoreCase(SymptomConstants.LESS_WEIGHT)){
                positivity += SymptomConstants.VALUE_LESS_WEIGHT;
            }
            if(s.getName().equalsIgnoreCase(SymptomConstants.APHTHAS)){
                positivity += SymptomConstants.VALUE_APHTHAS;
            }
            if(s.getName().equalsIgnoreCase(SymptomConstants.SKIN_ISSUE)){
                positivity += SymptomConstants.VALUE_SKIN_ISSUE;
            }
        }

        //If the positivity is higher than 50% points of the pattern, we must notify
        return positivity >= SymptomConstants.VALUE_PATTERN_UPPER_TRACT;
    }

    private boolean isPatternPerianal(List<Symptom> symptoms){
        int positivity = 0;

        for(Symptom s : symptoms){
            //We iterate
            if(s.getName().equalsIgnoreCase(SymptomConstants.DIAREE)){
                positivity += SymptomConstants.VALUE_DIAREE;
            }
            if(s.getName().equalsIgnoreCase(SymptomConstants.ANAL_PAIN)){
                positivity += SymptomConstants.VALUE_ANAL_PAIN;
            }
            if(s.getName().equalsIgnoreCase(SymptomConstants.BLOOD)){
                positivity += SymptomConstants.VALUE_BLOOD;
            }
            if(s.getName().equalsIgnoreCase(SymptomConstants.PERIANAL)){
                positivity += SymptomConstants.VALUE_PERIANAL;
            }
            if(s.getName().equalsIgnoreCase(SymptomConstants.SKIN_ISSUE)){
                positivity += SymptomConstants.VALUE_SKIN_ISSUE;
            }
        }

        //If the positivity is higher than 50% points of the pattern, we must notify
        return positivity >= SymptomConstants.VALUE_PATTERN_PERIANAL;
    }
}
