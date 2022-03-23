package com.aro.asistente_crohn;

import android.app.Application;

import androidx.room.Room;

import com.aro.asistente_crohn.database.CrohnsAssistDatabase;

public class DatabaseApplication extends Application {

    CrohnsAssistDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();

        // when upgrading versions, kill the original tables by using fallbackToDestructiveMigration()
        database = Room.databaseBuilder(this, CrohnsAssistDatabase.class, CrohnsAssistDatabase.NAME).fallbackToDestructiveMigration().build();
    }

    public CrohnsAssistDatabase getDatabase() {
        return database;
    }

}
