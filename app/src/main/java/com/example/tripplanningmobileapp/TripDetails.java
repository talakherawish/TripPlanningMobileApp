package com.example.tripplanningmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TripDetails extends AppCompatActivity {
    private Trip currentTrip;
    private TripPreferences tripPreferences;
    private ImageView tripImageDetail;
    private TextView tripNameDetail;
    private TextView tripOriginDetail;
    private TextView tripPriceDetail;
    private TextView tripLuggageDetail;
    private TextView tripAvailabilityDetail;
    private TextView tripDateDetail;
    private TextView tripTypeDetail;
    private TextView tripPremiumDetail;
    private TextView tripNotificationsDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trip_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tripPreferences = new TripPreferences(this);
        int tripId = getIntent().getIntExtra("TRIP_ID", -1);
        currentTrip = findTripById(tripId);

        if (currentTrip == null) {
            Toast.makeText(this, "Trip not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tripImageDetail = findViewById(R.id.tripImageDetail);
        tripNameDetail = findViewById(R.id.tripNameDetail);
        tripOriginDetail = findViewById(R.id.tripOriginDetail);
        tripPriceDetail = findViewById(R.id.tripPriceDetail);
        tripLuggageDetail = findViewById(R.id.tripLuggageDetail);
        tripAvailabilityDetail = findViewById(R.id.tripAvailabilityDetail);
        tripDateDetail = findViewById(R.id.tripDateDetail);
        tripTypeDetail = findViewById(R.id.tripTypeDetail);
        tripPremiumDetail = findViewById(R.id.tripPremiumDetail);
        tripNotificationsDetail = findViewById(R.id.tripNotificationsDetail);

        Button btnEdit = findViewById(R.id.btnEdit);
        Button btnDelete = findViewById(R.id.btnDelete);
        updateUI();
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(TripDetails.this, AddEditTripActivity.class);
            intent.putExtra("TRIP_ID", currentTrip.getId());
            intent.putExtra("EDIT_MODE", true);
            startActivity(intent);
        });

        // Delete button with confirmation
        btnDelete.setOnClickListener(v -> showDeleteConfirmation());
    }

    private Trip findTripById(int id) {
        for (Trip trip : Trip.trips) {
            if (trip.getId() == id) {
                return trip;
            }
        }
        return null;
    }

    private void showDeleteConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Trip")
                .setMessage("Are you sure you want to delete this trip?")
                .setPositiveButton("Delete", (dialog, which) -> deleteTrip())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteTrip() {
        Trip.trips.remove(currentTrip);
        tripPreferences.saveTrips(Trip.trips);
        Toast.makeText(this, "Trip deleted", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload trip data in case it was edited
        if (currentTrip != null) {
            Trip updatedTrip = findTripById(currentTrip.getId());
            if (updatedTrip != null) {
                currentTrip = updatedTrip;
                updateUI();
            }
        }
    }

    private void updateUI() {
        if (currentTrip == null) return;

        tripImageDetail.setImageResource(currentTrip.getImageId());
        tripNameDetail.setText(currentTrip.getDestination());
        tripOriginDetail.setText("From: " + currentTrip.getOrigin());
        tripPriceDetail.setText("$" + currentTrip.getPrice());
        tripLuggageDetail.setText("Luggage: " + currentTrip.getLuggage() + " bags");
        tripAvailabilityDetail.setText(currentTrip.isAvailable() ? "Available" : "Sold Out");
        tripAvailabilityDetail.setTextColor(currentTrip.isAvailable() ?
                getResources().getColor(android.R.color.holo_green_dark) :
                getResources().getColor(android.R.color.holo_red_dark));
        tripDateDetail.setText("Departure: " + currentTrip.getFormattedDate());
        tripTypeDetail.setText("Type: " + currentTrip.getTripType());
        tripPremiumDetail.setText(currentTrip.isPremium() ? "Premium Trip ⭐" : "Standard Trip");
        tripNotificationsDetail.setText("Notifications: " + (currentTrip.isNotificationsEnabled() ? "ON" : "OFF"));
    }
}