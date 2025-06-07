package com.example.camperkids;

import android.content.Intent;
import android.os.Bundle;
import android.os.health.SystemHealthManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;
import androidx.room.Room;

import com.example.camperkids.data.AppDatabase;
import com.example.camperkids.data.dao.RegionDao;
import com.example.camperkids.data.dao.CampDao;
import com.example.camperkids.data.entities.Camp;
import com.example.camperkids.data.entities.Region;

import java.util.List;
import java.util.concurrent.Executors;

public class SearchResultsActivity extends AppCompatActivity {
    private AppDatabase db;
    private RegionDao regionDao;
    private CampDao campDao;

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

        displayCamps(region);


    }

    private void displayCamps(Region region) {
        db = AppDatabase.getInstance(getApplicationContext());

        regionDao = db.regionDao();
        campDao = db.campDao();
        Executors.newSingleThreadExecutor().execute(() -> {
            int regionId = regionDao.getRegionIdByName(region.getName());
            List<Camp> campsInRegion = campDao.getCampsForRegion(regionId);

            runOnUiThread(() -> {
                displayCampCards(campsInRegion);
            });
        });
    }

    private void displayCampCards(List<Camp> campsInRegion) {
        int randomNumForReviews = new java.util.Random().nextInt(901) + 100; // Number of reviews ranging from 100 to 1000

        LinearLayout container = findViewById(R.id.campCard);
        LayoutInflater inflater = LayoutInflater.from(this);

        for (Camp camp : campsInRegion) {
            View card = inflater.inflate(R.layout.camp_card, container, false);

            // Populate the cards content
            TextView name = card.findViewById(R.id.tvCampName);
            TextView location = card.findViewById(R.id.tvLocation);
            TextView reviewCount = card.findViewById(R.id.tvReviewCount);
            TextView cert = card.findViewById(R.id.tvCert);
            TextView price = card.findViewById(R.id.tvPrice);
            RatingBar ratingBar = card.findViewById(R.id.ratingBar);
            ImageView image = card.findViewById(R.id.imageCamp);

            name.setText(camp.getName());
            location.setText(camp.getVillageName());
            reviewCount.setText("(" + randomNumForReviews + " reviews)"); //set random
            cert.setText(R.string.kepa_certified_instructors);
            // price.setText(camp.getPrice()); TODO PRICE NEEDS CAMP AVAILABILITY TABLE
            ratingBar.setRating(camp.getRating().floatValue());
            // image.setImageResource(camp.getImageRes()); TODO FIND A WAY TO ADD THE IMAGE

            // Add the card to the container
            container.addView(card);
        }
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
