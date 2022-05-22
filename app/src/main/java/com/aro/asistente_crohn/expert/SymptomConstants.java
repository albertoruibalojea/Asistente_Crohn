package com.aro.asistente_crohn.expert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SymptomConstants {
    public static final String DIAREE = "Diarrea";
    public static final String NAUSEAS = "Náuseas";
    public static final String NOT_HUNGRY = "Menos apetito";
    public static final String ABDOMINAL_PAIN = "Dolor barriga";
    public static final String ARTICULAR_PAIN = "Dolor articular";
    public static final String ANAL_PAIN = "Dolor anal";
    public static final String HEADACHE = "Dolor cabeza";
    public static final String FEVER = "Fiebre";
    public static final String TIRED = "Cansancio";
    public static final String BATHROOM_URGE = "Ir más al baño";
    public static final String LESS_WEIGHT = "Perdí peso";
    public static final String BLOOD = "Sangrado";
    public static final String APHTHAS = "Aftas";
    public static final String PERIANAL = "Herida perianal";
    public static final String SKIN_ISSUE = "Piel";

    //For each symptom, its value
    public static final Integer VALUE_DIAREE = 5; //7dias
    public static final Integer VALUE_NAUSEAS = 3;
    public static final Integer VALUE_NOT_HUNGRY = 3;
    public static final Integer VALUE_ABDOMINAL_PAIN = 5;
    public static final Integer VALUE_ARTICULAR_PAIN = 1;
    public static final Integer VALUE_ANAL_PAIN = 4;
    public static final Integer VALUE_HEADACHE = 1;
    public static final Integer VALUE_FEVER = 3;//3dias
    public static final Integer VALUE_TIRED = 3;
    public static final Integer VALUE_BATHROOM_URGE = 4;
    public static final Integer VALUE_LESS_WEIGHT = 2;
    public static final Integer VALUE_BLOOD = 4;
    public static final Integer VALUE_APHTHAS = 1;
    public static final Integer VALUE_PERIANAL = 3;
    public static final Integer VALUE_SKIN_ISSUE = 2;


    //Patterns
    public static final String PATTERN_ILEOCOLITIS = "PATTERN_ILEOCOLITIS";
    public static final String PATTERN_ILEITIS = "PATTERN_ILEITIS";
    public static final String PATTERN_COLITIS = "PATTERN_COLITIS";
    public static final String PATTERN_UPPER_TRACT = "PATTERN_UPPER_TRACT";
    public static final String PATTERN_PERIANAL = "PATTERN_PERIANAL";

    //Total sum of each pattern
    public static final Integer VALUE_PATTERN_ILEOCOLITIS = VALUE_DIAREE + VALUE_ABDOMINAL_PAIN + VALUE_TIRED + VALUE_LESS_WEIGHT + VALUE_BLOOD + VALUE_NOT_HUNGRY + VALUE_ARTICULAR_PAIN + VALUE_HEADACHE + VALUE_SKIN_ISSUE;
    public static final Integer VALUE_PATTERN_ILEITIS = VALUE_DIAREE + VALUE_ABDOMINAL_PAIN + VALUE_TIRED + VALUE_FEVER + VALUE_LESS_WEIGHT + VALUE_APHTHAS + VALUE_SKIN_ISSUE;
    public static final Integer VALUE_PATTERN_COLITIS = VALUE_DIAREE + VALUE_ABDOMINAL_PAIN + VALUE_FEVER + VALUE_BLOOD + VALUE_BATHROOM_URGE + VALUE_APHTHAS + VALUE_SKIN_ISSUE;
    public static final Integer VALUE_PATTERN_UPPER_TRACT = VALUE_FEVER + VALUE_NAUSEAS + VALUE_NOT_HUNGRY + VALUE_LESS_WEIGHT + VALUE_APHTHAS + VALUE_SKIN_ISSUE;
    public static final Integer VALUE_PATTERN_PERIANAL = VALUE_DIAREE + VALUE_ANAL_PAIN + VALUE_BLOOD + VALUE_PERIANAL + VALUE_SKIN_ISSUE;
}
