package com.example.camperkids;

import android.app.Application;
import androidx.room.Room;
import com.example.camperkids.data.AppDatabase;

public class CamperKids extends Application {
    private static AppDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        db = Room.databaseBuilder(
                        getApplicationContext(),
                        AppDatabase.class,
                        "camper_kids.db"
                )
                .createFromAsset("databases/camper_kids.sqlite3")
                .allowMainThreadQueries()
                .build();
        //db.getOpenHelper().getWritableDatabase();
    }

    public static AppDatabase getDatabase() {
        return db;
    }
}
