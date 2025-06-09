package com.example.camperkids;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.camperkids.data.AppDatabase;
import com.example.camperkids.data.entities.Camp;
import com.example.camperkids.data.entities.CampAvailability;
import com.google.android.material.radiobutton.MaterialRadioButton;

import java.util.concurrent.Executors;


public class CampDetailsActivity extends AppCompatActivity {
    private AppDatabase db;
    private int teenCount, childCount, toddCount;
    private CampAvailability availability;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_details);
        db = AppDatabase.getInstance(getApplicationContext());

        backButtonFunctionality();

        // We need the camp and availability objects, as well as some user input options from before
        Intent intent = getIntent();
        Camp camp = (Camp) getIntent().getSerializableExtra("camp");
        availability = (CampAvailability) getIntent().getSerializableExtra("availability");

        int periodId = intent.getIntExtra("periodId", -1);
        teenCount = intent.getIntExtra("teenCount", 0);
        childCount = intent.getIntExtra("childCount", 0);
        toddCount = intent.getIntExtra("toddCount", 0);
        int reviewCount = intent.getIntExtra("reviewsNumber", 0);

        // Filling in all the details using the intents
        fillCampInfo(camp, reviewCount);
        fillUserSelections(camp, periodId);
        fillPriceBreakdown(availability);
    }

    /**
     * Calculates and displays the price breakdown (teenager, child, toddler, taxes, total)
     * based on provided availability and visitor counts. If availability is insufficient,
     * it displays a dash and shows an alert.
     */
    private void fillPriceBreakdown(CampAvailability availability) {
        TextView priceTeenager = findViewById(R.id.priceTeen);
        TextView priceChild = findViewById(R.id.priceChild);
        TextView priceTodd = findViewById(R.id.priceToddler);
        TextView taxesTv = findViewById(R.id.taxes);
        TextView totalPriceTv = findViewById(R.id.totalPrice);

        int totalVisitorCount = teenCount + childCount + toddCount;
        int totalTeenPrice = 0, totalChildPrice = 0 , totalToddPrice = 0;

        if (totalVisitorCount <= availability.getAvailableSpots()){
            if (teenCount > 0) {
                totalTeenPrice = teenCount * availability.getPriceTeenager();
                priceTeenager.setText("€" + totalTeenPrice);
            } else {
                priceTeenager.setText("–");
            }

            if (childCount > 0) {
                totalChildPrice = childCount * availability.getPriceChild();
                priceChild.setText("€" + totalChildPrice);
            } else {
                priceChild.setText("–");
            }

            if (toddCount > 0) {
                totalToddPrice = toddCount * availability.getPriceToddler();
                priceTodd.setText("€" + totalToddPrice);
            } else {
                priceTodd.setText("–");
            }

            float taxes = (float) ((totalChildPrice + totalTeenPrice + totalToddPrice) * 24) / 100;
            if (taxes != 0){
                taxesTv.setText("€" + taxes);
            } else {
                taxesTv.setText("–");
            }

            float totalPrice = totalChildPrice + totalTeenPrice + totalToddPrice + taxes;
            if (totalPrice != 0){
                totalPriceTv.setText("€" + totalPrice);
            } else {
                totalPriceTv.setText("–");
            }
        } else {
            priceTeenager.setText("–");
            priceChild.setText("–");
            priceTodd.setText("–");
            taxesTv.setText("–");
            totalPriceTv.setText("–");
            new AlertDialog.Builder(this)
                    .setTitle("Not Enough Availability")
                    .setMessage("The selected number of visitors exceeds the available spots for this camp. Please reduce the number of visitors.")
                    .setPositiveButton("OK", null)
                    .show();
        }

    }

    /**
     * Sets the initial selected period, handles period radio button clicks to update camp availability
     * and price breakdown, and sets up visitor counters for teenagers, children, and toddlers.
     */
    private void fillUserSelections(Camp camp, int periodId) {
        MaterialRadioButton period1 = findViewById(R.id.period1);
        MaterialRadioButton period2 = findViewById(R.id.period2);
        MaterialRadioButton period3 = findViewById(R.id.period3);

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

        View.OnClickListener listener = v -> {
            period1.setChecked(v == period1);
            period2.setChecked(v == period2);
            period3.setChecked(v == period3);

            int selectedPeriodId;
            if (v == period1) selectedPeriodId = 1;
            else if (v == period2) selectedPeriodId = 2;
            else if (v == period3) selectedPeriodId = 3;
            else {
                selectedPeriodId = -1;
            }

            // Update availability with new period
            Executors.newSingleThreadExecutor().execute(() -> {
                CampAvailability newAvailability = db.campAvailabilityDao()
                        .getAvailabilityForCampAndPeriod(camp.getId(), selectedPeriodId);
                runOnUiThread(() -> {
                    availability = newAvailability;
                    fillPriceBreakdown(availability);
                });
            });
        };

        period1.setOnClickListener(listener);
        period2.setOnClickListener(listener);
        period3.setOnClickListener(listener);



        // Setting up the counter titles (Teenager, Child, Toddler)
        String[] categories = getResources().getStringArray(R.array.category_titles);
        setupCounter(R.id.rowTeen,      categories[0], teenCount, newCount -> teenCount  = newCount);
        setupCounter(R.id.rowChild,     categories[1], childCount, newCount -> childCount = newCount);
        setupCounter(R.id.rowToddler,   categories[2], toddCount, newCount -> toddCount  = newCount);
    }

    /**
     * Populates the camp's main information section in the UI.
     * Displays the camp's name, rating, number of reviews, location, and description.
     */
    private void fillCampInfo(Camp camp, int reviewCount) {
        TextView campTitle = findViewById(R.id.tvCampTitle);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        TextView reviews = findViewById(R.id.tvReviewCount);
        TextView location = findViewById(R.id.tvLocation);
        TextView description = findViewById(R.id.tvCampDesc);

        campTitle.setText(camp.getName());
        ratingBar.setRating(camp.getRating().floatValue());
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
            if (availability != null) fillPriceBreakdown(availability);
        });
        btnPlus.setOnClickListener(v -> {
            int val = Integer.parseInt(tvCnt.getText().toString()) + 1;
            tvCnt.setText(String.valueOf(val));
            cb.onCountChanged(val);
            if (availability != null) fillPriceBreakdown(availability);
        });
    }

    private void backButtonFunctionality() {
        ImageButton backBtn = findViewById(R.id.backButton);
        backBtn.setOnClickListener(v -> {
            finish();
        });
    }
}
