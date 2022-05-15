package com.aro.asistente_crohn.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.aro.asistente_crohn.model.FoodRepo;
import com.aro.asistente_crohn.model.Health;
import com.aro.asistente_crohn.model.Food;
import com.aro.asistente_crohn.model.Recommendation;
import com.aro.asistente_crohn.model.Symptom;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// bump version number if the schema changes
@Database(
            entities={Health.class, Food.class, Recommendation.class, Symptom.class, FoodRepo.class},
            version=5,
            autoMigrations = { @AutoMigration(from = 4, to = 5)
})
@TypeConverters({DateConverter.class})
public abstract class CrohnsAssistDatabase extends RoomDatabase {

    // Database name to be used
    public static final String NAME = "CrohnsAssistDatabase.db";

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile CrohnsAssistDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static CrohnsAssistDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CrohnsAssistDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CrohnsAssistDatabase.class, NAME)
                            .createFromAsset("database/CrohnsAssistDatabase.db")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    //Override the onCreate method to populate the database.
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };

    // Declaring data access objects as abstract
    public abstract HealthDAO healthDAO();

    public abstract FoodDAO foodDAO();

    public abstract FoodRepoDAO foodRepoDAO();

    public abstract RecommendationDAO recommendationDAO();

    public abstract SymptomDAO symptomDAO();
}
