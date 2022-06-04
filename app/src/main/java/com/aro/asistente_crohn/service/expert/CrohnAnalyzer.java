package com.aro.asistente_crohn.service.expert;

import android.content.SharedPreferences;

import androidx.lifecycle.LifecycleOwner;

import com.aro.asistente_crohn.model.Health;
import com.aro.asistente_crohn.view.viewmodel.ItemViewModel;
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
     *   - Se comprueban los síntomas de D según el patrón en cuestión
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
    private static final String TYPE = "pattern";

    public CrohnAnalyzer(ItemViewModel viewModel, LifecycleOwner lifecycleOwner, SharedPreferences preferences){
        this.viewModel = viewModel;
        this.lifecycleOwner = lifecycleOwner;
        this.preferences = preferences;
    }

    public void analyze(){

        int prevDays = Integer.parseInt(this.preferences.getString(DAYS_TO_ANALYZE, null));

        //For the previous days, we set Health.active=true and Health.relatedSymptoms=type
        for(int i=0; i<=prevDays; i++) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, -i);

            Date before = cal.getTime();
            before.setHours(00);
            before.setMinutes(00);
            before.setSeconds(00);
            Date after = cal.getTime();
            after.setHours(23);
            after.setMinutes(59);
            after.setSeconds(59);

            this.viewModel.getSelectedDayHealth(before, after).observe(this.lifecycleOwner, healthList -> {
                List<Health> cacheTodayHealthList = new ArrayList<>();
                Health health = new Health();
                health.setDetectionDate(before);
                if (!healthList.isEmpty()) {
                    cacheTodayHealthList = healthList;
                    health = cacheTodayHealthList.get(0);
                }

                if (!health.getCrohnActive() && health.getRelatedSymptoms().isEmpty() && !this.isActivePrevDays()) {
                    //Get today´s symptoms
                    Health finalHealth = health;
                    this.viewModel.getSelectedDaySymptoms(before, after).observe(this.lifecycleOwner, symptomList -> {
                        List<Symptom> cacheTodaySymptomList = new ArrayList<>();
                        if (!symptomList.isEmpty()) {
                            cacheTodaySymptomList.addAll(symptomList);
                        }

                        //We check today´s symptoms looking for occurrences in the user type
                        //If coincidence, the Observer in the Health fragment will notify the user via interface
                        //If the type is repeated a few days before, it means a critical state (ACTIVE) and must notify too
                        if (this.isTypeGeneric(cacheTodaySymptomList)) {
                            finalHealth.setRelatedSymptoms(SymptomConstants.TYPE_ILEOCOLITIS);
                            updateHealthViewModel(healthList, finalHealth);
                            if (this.doesTheTypeRepeat(SymptomConstants.TYPE_ILEOCOLITIS)) {
                                this.setActivePrevDays(SymptomConstants.TYPE_ILEOCOLITIS);
                            }
                        } else if (this.isTypeSmallBowel(cacheTodaySymptomList)) {
                            finalHealth.setRelatedSymptoms(SymptomConstants.TYPE_ILEITIS);
                            updateHealthViewModel(healthList, finalHealth);
                            if (this.doesTheTypeRepeat(SymptomConstants.TYPE_ILEITIS)) {
                                this.setActivePrevDays(SymptomConstants.TYPE_ILEITIS);
                            }
                        } else if (this.isTypeColon(cacheTodaySymptomList)) {
                            finalHealth.setRelatedSymptoms(SymptomConstants.TYPE_COLITIS);
                            updateHealthViewModel(healthList, finalHealth);
                            if (this.doesTheTypeRepeat(SymptomConstants.TYPE_COLITIS)) {
                                this.setActivePrevDays(SymptomConstants.TYPE_COLITIS);
                            }
                        } else if (this.isTypeUpperTract(cacheTodaySymptomList)) {
                            finalHealth.setRelatedSymptoms(SymptomConstants.TYPE_UPPER_TRACT);
                            updateHealthViewModel(healthList, finalHealth);
                            if (this.doesTheTypeRepeat(SymptomConstants.TYPE_UPPER_TRACT)) {
                                this.setActivePrevDays(SymptomConstants.TYPE_UPPER_TRACT);
                            }
                        } else if (this.isTypePerianal(cacheTodaySymptomList)) {
                            finalHealth.setRelatedSymptoms(SymptomConstants.TYPE_PERIANAL);
                            updateHealthViewModel(healthList, finalHealth);
                            if (this.doesTheTypeRepeat(SymptomConstants.TYPE_PERIANAL)) {
                                this.setActivePrevDays(SymptomConstants.TYPE_PERIANAL);
                            }
                        }

                    });
                } else if (!health.getCrohnActive() && this.isActivePrevDays()) {
                    //In this case, the disease is showing the user type in the last days
                    //So we just check that type
                    String actualType = this.preferences.getString(TYPE, null);
                    Health finalHealth = health;
                    this.viewModel.getSelectedDaySymptoms(before, after).observe(this.lifecycleOwner, symptomList -> {
                        List<Symptom> cacheTodaySymptomList = new ArrayList<>();
                        if (symptomList != null) {
                            cacheTodaySymptomList.addAll(symptomList);
                        }

                        //If the type is repeated, it means user is still having a critical state (ACTIVE) and must notify via app only
                        if (actualType.equalsIgnoreCase(SymptomConstants.TYPE_ILEOCOLITIS) && this.isTypeGeneric(cacheTodaySymptomList)) {
                            finalHealth.setRelatedSymptoms(SymptomConstants.TYPE_ILEOCOLITIS);
                            this.setActivePrevDays(SymptomConstants.TYPE_ILEOCOLITIS);
                            updateHealthViewModel(healthList, finalHealth);
                        } else if (actualType.equalsIgnoreCase(SymptomConstants.TYPE_ILEITIS) && this.isTypeSmallBowel(cacheTodaySymptomList)) {
                            finalHealth.setRelatedSymptoms(SymptomConstants.TYPE_ILEITIS);
                            this.setActivePrevDays(SymptomConstants.TYPE_ILEITIS);
                            updateHealthViewModel(healthList, finalHealth);
                        } else if (actualType.equalsIgnoreCase(SymptomConstants.TYPE_COLITIS) && this.isTypeColon(cacheTodaySymptomList)) {
                            finalHealth.setRelatedSymptoms(SymptomConstants.TYPE_COLITIS);
                            this.setActivePrevDays(SymptomConstants.TYPE_COLITIS);
                            updateHealthViewModel(healthList, finalHealth);
                        } else if (actualType.equalsIgnoreCase(SymptomConstants.TYPE_UPPER_TRACT) && this.isTypeUpperTract(cacheTodaySymptomList)) {
                            finalHealth.setRelatedSymptoms(SymptomConstants.TYPE_UPPER_TRACT);
                            this.setActivePrevDays(SymptomConstants.TYPE_UPPER_TRACT);
                            updateHealthViewModel(healthList, finalHealth);
                        } else if (actualType.equalsIgnoreCase(SymptomConstants.TYPE_PERIANAL) && this.isTypePerianal(cacheTodaySymptomList)) {
                            finalHealth.setRelatedSymptoms(SymptomConstants.TYPE_PERIANAL);
                            this.setActivePrevDays(SymptomConstants.TYPE_PERIANAL);
                            updateHealthViewModel(healthList, finalHealth);
                        }
                    });
                }
            });
        }
    }

    private void updateHealthViewModel(List<Health> healthList,Health health){
        if(healthList.isEmpty()){
            this.viewModel.insertHealth(health);
        } else {
            this.viewModel.updateHealth(health);
        }
    }

    //Method to update ths Crohn State to TRUE and set the use type to the positive days
    private void setActivePrevDays(String type){
        int prevDays = Integer.parseInt(this.preferences.getString(DAYS_TO_ANALYZE, null));

        //For the previous days, we set Health.active=true and Health.relatedSymptoms=type
        for(int i=0; i<=prevDays; i++){
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
                    //Health.relatedSymptoms=type
                    health.setRelatedSymptoms(type);

                    updateHealthViewModel(cacheTodayHealthList, health);
                }
            });
        }
    }

    //Method to check is Crohn is TRUE in the past DAYS_TO_ANALYZE days
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

    //Method to check if the user type is repeating in the past DAYS_TO_ANALYZE days
    private boolean doesTheTypeRepeat(String type){
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

                    //we compare the type
                    if(health.getRelatedSymptoms().equalsIgnoreCase(type)){
                        occurrences.addAndGet(1);
                    }
                }
            });
        }

        return occurrences.get() >= prevDays;
    }

    private boolean isTypeGeneric(List<Symptom> symptoms){

        if(this.preferences.getString(TYPE, null).equalsIgnoreCase(SymptomConstants.TYPE_ILEOCOLITIS)){
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

            //If the positivity is higher than 50% points of the type, we must notify
            return positivity >= SymptomConstants.VALUE_TYPE_ILEOCOLITIS /2;
        } else return false;
    }

    private boolean isTypeSmallBowel(List<Symptom> symptoms){

        if(this.preferences.getString(TYPE, null).equalsIgnoreCase(SymptomConstants.TYPE_ILEITIS)){
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

            //If the positivity is higher than 50% points of the type, we must notify
            return positivity >= SymptomConstants.VALUE_TYPE_ILEITIS /2;
        } else return false;
    }

    private boolean isTypeColon(List<Symptom> symptoms){

        if(this.preferences.getString(TYPE, null).equalsIgnoreCase(SymptomConstants.TYPE_COLITIS)){
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

            //If the positivity is higher than 50% points of the type, we must notify
            return positivity >= SymptomConstants.VALUE_TYPE_COLITIS /2;
        } else return false;
    }

    private boolean isTypeUpperTract(List<Symptom> symptoms){

        if(this.preferences.getString(TYPE, null).equalsIgnoreCase(SymptomConstants.TYPE_UPPER_TRACT)){
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

            //If the positivity is higher than 50% points of the type, we must notify
            return positivity >= SymptomConstants.VALUE_TYPE_UPPER_TRACT /2;
        } else return false;
    }

    private boolean isTypePerianal(List<Symptom> symptoms){

        if(this.preferences.getString(TYPE, null).equalsIgnoreCase(SymptomConstants.TYPE_PERIANAL)){
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

            //If the positivity is higher than 50% points of the type, we must notify
            return positivity >= SymptomConstants.VALUE_TYPE_PERIANAL /2;
        } else return false;
    }
}
