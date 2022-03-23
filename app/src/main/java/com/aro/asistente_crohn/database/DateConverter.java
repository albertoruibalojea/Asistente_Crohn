package com.aro.asistente_crohn.database;

import androidx.room.TypeConverter;

import java.util.Date;

//we need a TypeConverter to save the date into the room database
public class DateConverter {

    @TypeConverter
    public static Date toDate(Long dateLong){
        return dateLong == null ? null: new Date(dateLong);
    }

    @TypeConverter
    public static Long fromDate(Date date){
        return date == null ? null : date.getTime();
    }
}
