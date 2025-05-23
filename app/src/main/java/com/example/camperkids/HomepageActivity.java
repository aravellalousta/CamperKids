package com.example.camperkids;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

public class HomepageActivity extends AppCompatActivity {
    private int teenCount, childCount, toddCount;
    private String[] categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // 1) load the titles array
        categories = getResources().getStringArray(R.array.category_titles);

        // 2) set up each row, using its tag as the index
        setupCounter(R.id.rowTeen,      categories[0], newCount -> teenCount  = newCount);
        setupCounter(R.id.rowChild,     categories[1], newCount -> childCount = newCount);
        setupCounter(R.id.rowToddler,   categories[2], newCount -> toddCount  = newCount);
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