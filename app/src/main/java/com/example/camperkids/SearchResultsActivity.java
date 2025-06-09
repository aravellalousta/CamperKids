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
import com.example.camperkids.data.dao.CampAvailabilityDao;
import com.example.camperkids.data.dao.RegionDao;
import com.example.camperkids.data.dao.CampDao;
import com.example.camperkids.data.entities.Camp;
import com.example.camperkids.data.entities.CampAvailability;
import com.example.camperkids.data.entities.Region;

import java.util.List;
import java.util.concurrent.Executors;

public class SearchResultsActivity extends AppCompatActivity {
    private AppDatabase db;
    private RegionDao regionDao;
    private CampDao campDao;
    public int totalVisitorCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        backButtonFunctionality();

        // In order to display the search results we need the inputs from the previous activity
        Intent intent = getIntent();
        Region region = (Region) intent.getSerializableExtra("region");
        String selectedPeriod = intent.getStringExtra("period");
        int periodId = getPeriodIdFromLabel(selectedPeriod);

        int teenCount = intent.getIntExtra("teenCount", 0);
        int childCount = intent.getIntExtra("childCount", 0);
        int toddCount = intent.getIntExtra("toddCount", 0);
        totalVisitorCount = teenCount + childCount + toddCount;

        // On the top of the activity we display the search criteria from the previous activity
        displaySearchCriteria(selectedPeriod, region, totalVisitorCount);

        // Based on the search criteria and the database we find the available camps
        getCampsByRegion(region, periodId, teenCount, childCount, toddCount);
    }

    // Assign an id on every period
    private int getPeriodIdFromLabel(String selectedPeriod) {
        if (selectedPeriod.contains("Period 1")) return 1;
        else if (selectedPeriod.contains("Period 2")) return 2;
        else if (selectedPeriod.contains("Period 3")) return 3;
        else return -1;
    }

    /** Using the region from the search criteria and our database,
     * we find all the camps in that region
     */
    private void getCampsByRegion(Region region, int periodId, int teenCount, int childCount, int toddCount) {
        db = AppDatabase.getInstance(getApplicationContext());
        regionDao = db.regionDao();
        campDao = db.campDao();

        Executors.newSingleThreadExecutor().execute(() -> {
            int regionId = regionDao.getRegionIdByName(region.getName());
            List<Camp> campsInRegion = campDao.getCampsForRegion(regionId);

            runOnUiThread(() -> {
                displayCampCards(campsInRegion, periodId, teenCount, childCount, toddCount);
            });
        });
    }

    /**
     * After finding the camps in a region, this method displays a list of camp cards based on availability.
     * For each camp, it checks available spots and if there are enough,
     * it populates a card with camp details and adds it to the UI.
     */
    private void displayCampCards(List<Camp> campsInRegion, int periodId, int teenCount, int childCount, int toddCount) {
        LinearLayout container = findViewById(R.id.campCard);
        LayoutInflater inflater = LayoutInflater.from(this);

        Executors.newSingleThreadExecutor().execute(() -> {
            for (Camp camp : campsInRegion) {
                CampAvailability availability = db.campAvailabilityDao()
                        .getAvailabilityForCampAndPeriod(camp.getId(), periodId);

                // Here we populate the card
                if (availability.getAvailableSpots() >= totalVisitorCount) {
                    runOnUiThread(() -> {
                        View card = inflater.inflate(R.layout.camp_card, container, false);

                        TextView name = card.findViewById(R.id.tvCampName);
                        TextView location = card.findViewById(R.id.tvLocation);
                        TextView reviewCount = card.findViewById(R.id.tvReviewCount);
                        TextView cert = card.findViewById(R.id.tvCert);
                        TextView priceTv = card.findViewById(R.id.tvPrice);
                        RatingBar ratingBar = card.findViewById(R.id.ratingBar);
                        ImageView image = card.findViewById(R.id.imageCamp);

                        // Set values for all the elements in the card
                        int randomNumForReviews = new java.util.Random().nextInt(901) + 100;
                        name.setText(camp.getName());
                        location.setText(camp.getVillageName());
                        reviewCount.setText("(" + randomNumForReviews + " reviews)");
                        cert.setText(R.string.kepa_certified_instructors);
                        ratingBar.setRating(camp.getRating().floatValue());
                        setCampLogo(camp, image);

                        int totalPrice = teenCount * availability.getPriceTeenager()
                                + childCount * availability.getPriceChild()
                                + toddCount * availability.getPriceToddler();

                        priceTv.setText("Total: €" + totalPrice);

                        // Add card to the view
                        container.addView(card);

                        // Start new activity when clicking on the card
                        card.setOnClickListener(v -> {
                            Intent intent = new Intent(SearchResultsActivity.this, CampDetailsActivity.class);

                            // Pass camp object
                            intent.putExtra("camp", camp);
                            intent.putExtra("availability", availability);

                            // Pass user selections
                            intent.putExtra("periodId", periodId);
                            intent.putExtra("teenCount", teenCount);
                            intent.putExtra("childCount", childCount);
                            intent.putExtra("toddCount", toddCount);
                            intent.putExtra("reviewsNumber", randomNumForReviews);

                            startActivity(intent);
                        });

                    });
                }
            }
        });
    }

    private void setCampLogo(Camp camp, ImageView imageView) {
        String campName = camp.getName().toLowerCase();

        if (campName.contains("ymca")) {
            imageView.setImageResource(R.drawable.ymca_camp);
        } else if (campName.contains("skouras")) {
            imageView.setImageResource(R.drawable.skouras_camp);
        } else if (campName.contains("happy days")) {
            imageView.setImageResource(R.drawable.happy_days_camp);
        } else if (campName.contains("alexandra")) {
            imageView.setImageResource(R.drawable.alexandra_camp);
        } else if (campName.contains("evangelical")) {
            imageView.setImageResource(R.drawable.gexa_katerini_camp);
        } else if (campName.contains("vrontou")) {
            imageView.setImageResource(R.drawable.vrontou_camp);
        } else if (campName.contains("tsaf tsouf")) {
            imageView.setImageResource(R.drawable.tsaf_tsouf_camp);
        } else {
            imageView.setImageResource(R.drawable.camper_kids_logo);
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
