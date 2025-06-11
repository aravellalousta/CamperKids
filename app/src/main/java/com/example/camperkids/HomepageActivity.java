package com.example.camperkids;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.camperkids.data.AppDatabase;
import com.example.camperkids.data.dao.RegionDao;
import com.example.camperkids.data.entities.Region;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.Executors;

public class HomepageActivity extends AppCompatActivity {
    private AppDatabase db;
    private RegionDao regionDao;
    private Region region;
    private String selectedPeriod = null;

    private int teenCount, childCount, toddCount;
    private int totalCount = 0;
    private String[] categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // Search for Location
        searchFieldFunctionality();

        // Dropdown with Periods
        dropdownFunctionality();

        // Counters
        countersFunctionality();

    }

    private void countersFunctionality() {
        // Setting up the counter titles (Teenager, Child, Toddler)
        categories = getResources().getStringArray(R.array.category_titles);
        setupCounter(R.id.rowTeen,      categories[0], newCount -> teenCount  = newCount);
        setupCounter(R.id.rowChild,     categories[1], newCount -> childCount = newCount);
        setupCounter(R.id.rowToddler,   categories[2], newCount -> toddCount  = newCount);
    }

    // Setting up the options of the dropdown
    private void dropdownFunctionality() {
        AutoCompleteTextView periodDropdown = findViewById(R.id.periodDropdown);
        TextInputLayout tilPeriod = findViewById(R.id.tilPeriod);


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

        periodDropdown.setOnItemClickListener((parent, view, position, id) -> {
            selectedPeriod = (String) parent.getItemAtPosition(position);
            tilPeriod.setError(null);
        });
    }

    /**
     * Handles the search input and search button. Uses the entered region keyword,
     * searches the database, and navigates to SearchResultsActivity if the region exists.
     * If the region is not found or keyword is empty, it shows a Toast.
     */
    private void searchFieldFunctionality() {
        TextInputEditText searchInput = findViewById(R.id.etLocation);
        Button searchButton = findViewById(R.id.btnSearch);
        TextInputLayout tilPeriod = findViewById(R.id.tilPeriod);


        searchButton.setOnClickListener(v -> {
            totalCount = teenCount + childCount + toddCount;
            String keyword = String.valueOf(searchInput.getText());
            if (keyword.isEmpty()) {
                searchInput.setError("Please enter a region name");
                return;
            } else if (selectedPeriod == null){
                tilPeriod.setError("Please select a time period");
                return;
            } else if (totalCount == 0){
                Toast.makeText(this, "Please enter the amount of kids attending", Toast.LENGTH_SHORT).show();
                return;
            }

            db = AppDatabase.getInstance(getApplicationContext());
            regionDao = db.regionDao();

            Executors.newSingleThreadExecutor().execute(() -> {
                Region region = regionDao.getRegionByName(keyword);

                if (region == null) {
                    runOnUiThread(() -> Toast.makeText(this, "Region not found", Toast.LENGTH_SHORT).show());
                } else {
                    Intent intent = new Intent(HomepageActivity.this, SearchResultsActivity.class);
                    intent.putExtra("region", region); // must be Serializable
                    intent.putExtra("period", selectedPeriod);
                    intent.putExtra("teenCount", teenCount);
                    intent.putExtra("childCount", childCount);
                    intent.putExtra("toddCount", toddCount);
                    startActivity(intent);
                }
            });

        });

    }

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