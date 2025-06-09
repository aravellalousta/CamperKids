package com.example.camperkids;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import com.example.camperkids.data.entities.Camp;
import com.google.android.material.checkbox.MaterialCheckBox;

public class CampDetailsActivity extends AppCompatActivity {
    private int teenCount, childCount, toddCount;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_details);

        Intent intent = getIntent();
        Camp camp = (Camp) getIntent().getSerializableExtra("camp");

        int periodId = intent.getIntExtra("periodId", -1);
        teenCount = intent.getIntExtra("teenCount", 0);
        childCount = intent.getIntExtra("childCount", 0);
        toddCount = intent.getIntExtra("toddCount", 0);
        int reviewCount = intent.getIntExtra("reviewsNumber", 0);

        fillCampInfo(camp, reviewCount);
        fillUserSelections(periodId);
        backButtonFunctionality();
    }

    private void fillUserSelections(int periodId) {
        MaterialCheckBox period1 = findViewById(R.id.period1);
        MaterialCheckBox period2 = findViewById(R.id.period2);
        MaterialCheckBox period3 = findViewById(R.id.period3);

        switch (periodId) {
            case 1:
                period1.setChecked(true);
                break;
            case 2:
                period2.setChecked(true);
                break;
            case 3:
                period3.setChecked(true);
                break;
        }

        // Setting up the counter titles (Teenager, Child, Toddler)
        String[] categories = getResources().getStringArray(R.array.category_titles);
        setupCounter(R.id.rowTeen,      categories[0], teenCount, newCount -> teenCount  = newCount);
        setupCounter(R.id.rowChild,     categories[1], childCount, newCount -> childCount = newCount);
        setupCounter(R.id.rowToddler,   categories[2], toddCount, newCount -> toddCount  = newCount);
    }

    private void fillCampInfo(Camp camp, int reviewCount) {
        TextView campTitle = findViewById(R.id.tvCampTitle);
        TextView reviews = findViewById(R.id.tvReviewCount);
        TextView location = findViewById(R.id.tvLocation);
        TextView description = findViewById(R.id.tvCampDesc);

        campTitle.setText(camp.getName());
        reviews.setText("(" + reviewCount + ")");
        location.setText(camp.getVillageName());
        description.setText(camp.getDescription());
    }

    private interface CounterCallback { void onCountChanged(int newCount); }

    private void setupCounter(@IdRes int rowId, String label, int initialValue, CounterCallback cb) {
        View row = findViewById(rowId);
        TextView tvLbl = row.findViewById(R.id.tvLabel);
        TextView tvCnt = row.findViewById(R.id.tvCount);
        View btnMinus = row.findViewById(R.id.btnMinus);
        View btnPlus = row.findViewById(R.id.btnPlus);

        tvLbl.setText(label);
        tvCnt.setText(String.valueOf(initialValue)); // set the initial value

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

    private void backButtonFunctionality() {
        ImageButton backBtn = findViewById(R.id.backButton);
        backBtn.setOnClickListener(v -> {
            finish();
        });
    }
}
