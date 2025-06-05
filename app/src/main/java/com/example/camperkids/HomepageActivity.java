package com.example.camperkids;

import static java.sql.Types.NULL;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.example.camperkids.data.AppDatabase;
import com.example.camperkids.data.dao.RegionDao;
import com.example.camperkids.data.entities.Region;
import com.example.camperkids.data.entities.User;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.concurrent.Executors;

public class HomepageActivity extends AppCompatActivity {
    private AppDatabase db;
    private RegionDao regionDao;
    private int teenCount, childCount, toddCount;
    private String[] categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // Search for Location
        searchFieldFunctionality();

        // Dropdown with Periods
        dropdownFunctionality();




        // Setting up the counter titles (Teenager, Child, Toddler)
        categories = getResources().getStringArray(R.array.category_titles);
        setupCounter(R.id.rowTeen,      categories[0], newCount -> teenCount  = newCount);
        setupCounter(R.id.rowChild,     categories[1], newCount -> childCount = newCount);
        setupCounter(R.id.rowToddler,   categories[2], newCount -> toddCount  = newCount);



    }

    private void dropdownFunctionality() {
        AutoCompleteTextView periodDropdown = findViewById(R.id.periodDropdown);

        String[] timePeriods = new String[] {
                "Period 1 (June 15 - July 30)",
                "Period 2 (July 1 - July 15)",
                "Period 3 (July 16 - August 1)"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                timePeriods
        );

        periodDropdown.setAdapter(adapter);
        periodDropdown.setDropDownBackgroundResource(R.color.color_dropdown);


    }

    private void searchFieldFunctionality() {
        // Searching for locations
        TextInputEditText searchInput = findViewById(R.id.etLocation);
        Button searchButton = findViewById(R.id.btnSearch);

        searchButton.setOnClickListener(v -> {
            String keyword = String.valueOf(searchInput.getText());
            if (keyword.isEmpty()) {
                Toast.makeText(this, "Please enter a region name", Toast.LENGTH_SHORT).show();
                return;
            }

            db = Room.databaseBuilder(
                    getApplicationContext(),
                    AppDatabase.class,
                    "camper_kids.db"
            ).build();
            regionDao = db.regionDao();

            // Run Room query in background
            Executors.newSingleThreadExecutor().execute(() -> {
                Region region = regionDao.getRegionByName(keyword);

                System.out.println(region);

                if (region == null) {
                    runOnUiThread(() -> Toast.makeText(this, "Region not found", Toast.LENGTH_SHORT).show());
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Region exists: " + region.getName(), Toast.LENGTH_SHORT).show());
                }
            });
        });
    }

    // callback interface for count changes
    private interface CounterCallback { void onCountChanged(int newCount); }

    private void setupCounter(@IdRes int rowId, String label, CounterCallback cb) {
        View row      = findViewById(rowId);
        TextView tvLbl   = row.findViewById(R.id.tvLabel);
        TextView tvCnt   = row.findViewById(R.id.tvCount);
        View btnMinus = row.findViewById(R.id.btnMinus);
        View btnPlus  = row.findViewById(R.id.btnPlus);

        // set the correct title
        tvLbl.setText(label);
        btnMinus.setOnClickListener(v -> {
            int val = Math.max(0, Integer.parseInt(tvCnt.getText().toString()) - 1);
            tvCnt.setText(String.valueOf(val));
            cb.onCountChanged(val);
        });
        btnPlus.setOnClickListener(v -> {
            int val = Integer.parseInt(tvCnt.getText().toString()) + 1;
            tvCnt.setText(String.valueOf(val));
            cb.onCountChanged(val);
        });
    }
}