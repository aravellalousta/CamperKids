package com.example.camperkids;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.example.camperkids.data.entities.Region;

public class SearchResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Intent intent = getIntent();

        Region region = (Region) intent.getSerializableExtra("region");
        String selectedPeriod = intent.getStringExtra("period");
        int teenCount = intent.getIntExtra("teenCount", 0);
        int childCount = intent.getIntExtra("childCount", 0);
        int toddCount = intent.getIntExtra("toddCount", 0);
        int totalVisitorCount = teenCount + childCount + toddCount;

        displaySearchCriteria(selectedPeriod, region, totalVisitorCount);
        backButtonFunctionality();


    }

    private void backButtonFunctionality() {
        ImageButton backBtn = findViewById(R.id.backButton);
        backBtn.setOnClickListener(v -> {
            startActivity(new Intent(SearchResultsActivity.this, HomepageActivity.class));
        });

    }

    private void displaySearchCriteria(String selectedPeriod, Region region, int totalVisitorCount) {
        TextView period = findViewById(R.id.time_period);
        TextView location = findViewById(R.id.camp_location);
        TextView visitors = findViewById(R.id.visitors);

        period.setText(selectedPeriod);
        location.setText(region.getName());
        visitors.setText(String.valueOf(totalVisitorCount));
    }
}
