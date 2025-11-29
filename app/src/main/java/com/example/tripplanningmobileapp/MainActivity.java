package com.example.tripplanningmobileapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TripAdapter adapter;
    private TripPreferences tripPreferences;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tripPreferences = new TripPreferences(this);
        loadTripsFromPreferences();

        recyclerView = findViewById(R.id.trips);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TripAdapter(new ArrayList<>(Trip.trips));
        recyclerView.setAdapter(adapter);

        searchView = findViewById(R.id.searchView);
        searchView.setQueryHint("Search trips...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        findViewById(R.id.btnAddTrip).setOnClickListener(v ->
                startActivity(new Intent(this, AddEditTripActivity.class))
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTripsFromPreferences();
        adapter.updateTrips(Trip.trips); // refresh adapter with latest trips
    }

    private void loadTripsFromPreferences() {
        ArrayList<Trip> loadedTrips = tripPreferences.loadTrips();
        Trip.trips.clear();

        if (loadedTrips.isEmpty()) {
            Trip.initializeDefaultTrips();
            tripPreferences.saveTrips(Trip.trips);
            tripPreferences.saveNextId(Trip.getNextId());
        } else {
            Trip.trips.addAll(loadedTrips);
            Trip.setNextId(tripPreferences.loadNextId());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveTrips();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveTrips();
    }

    private void saveTrips() {
        tripPreferences.saveTrips(Trip.trips);
        tripPreferences.saveNextId(Trip.getNextId());
    }
}