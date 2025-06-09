package com.example.camperkids;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.camperkids.data.entities.Camp;

public class CampDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Camp camp = (Camp) getIntent().getSerializableExtra("camp");

        int periodId = intent.getIntExtra("periodId", -1);
        int teenCount = intent.getIntExtra("teenCount", 0);
        int childCount = intent.getIntExtra("childCount", 0);
        int toddCount = intent.getIntExtra("toddCount", 0);
    }
}
