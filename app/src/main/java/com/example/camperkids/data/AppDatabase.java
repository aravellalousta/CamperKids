package com.example.camperkids.data;

import android.app.Application;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.camperkids.data.entities.Region;
import com.example.camperkids.data.entities.Camp;
import com.example.camperkids.data.entities.Period;
import com.example.camperkids.data.entities.CampAvailability;
import com.example.camperkids.data.entities.User;
import com.example.camperkids.data.dao.RegionDao;
import com.example.camperkids.data.dao.CampDao;
import com.example.camperkids.data.dao.PeriodDao;
import com.example.camperkids.data.dao.CampAvailabilityDao;
import com.example.camperkids.data.dao.UserDao;

@Database(
        entities = {
                Region.class,
                Camp.class,
                Period.class,
                CampAvailability.class,
                User.class
        },
        version = 2,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract RegionDao regionDao();
    public abstract CampDao campDao();
    public abstract PeriodDao periodDao();
    public abstract CampAvailabilityDao campAvailabilityDao();
    public abstract UserDao userDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "camper_kids_db.db"
                            )
                            .createFromAsset("databases/camper_kids_db.db")
                            .fallbackToDestructiveMigration() // optional, but prevents schema mismatch crashes
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}