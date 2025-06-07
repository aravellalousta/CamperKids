package com.example.camperkids;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.camperkids.data.AppDatabase;
import com.example.camperkids.data.dao.UserDao;
import com.example.camperkids.data.entities.User;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private AppDatabase db;
    private UserDao userDao;
    private ExecutorService ioExecutor;
    private CircularProgressIndicator loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1) Load the logo from assets
        ImageView logoView = findViewById(R.id.logoImageView);
        try (InputStream is = getAssets().open("images/CamperKids-Logo.png")) {
            Drawable d = Drawable.createFromStream(is, null);
            logoView.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Fetch the users name from the database to display on the welcome message
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());

        if (db == null) {
            Log.e("DB", "AppDatabase instance is null!");
        } else {
            Log.e("DB", "AppDatabase instance is NOT NULL!");
            System.out.println(db);
        }

        userDao = db.userDao();

        ioExecutor = Executors.newSingleThreadExecutor();

        final TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        ioExecutor.execute(new Runnable() {
            @Override
            public void run() {
                User user = userDao.getUserById(1);
                System.out.println(user);
                final String name = (user != null ? user.name : "Guest");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        welcomeTextView.setText("Welcome, " + name + "!");
                    }
                });
            }
        });

        // Loader before moving to the next activity
        loader = findViewById(R.id.progressIndicator);
        loader.setVisibility(CircularProgressIndicator.VISIBLE);
        loader.show();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(MainActivity.this, HomepageActivity.class));
            finish();
        }, 2_000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // clean up
        ioExecutor.shutdown();
    }
}