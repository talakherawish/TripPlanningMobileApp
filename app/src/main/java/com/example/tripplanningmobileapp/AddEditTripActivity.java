package com.example.tripplanningmobileapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEditTripActivity extends AppCompatActivity {

    private EditText etOrigin, etDestination, etPrice, etLuggage;
    private Button btnSelectDate, btnSave, btnCancel;
    private RadioGroup rgTripType, rgAvailability;
    private RadioButton rbCultural, rbAdventure, rbRelaxation, rbReligious;
    private RadioButton rbAvailable, rbSoldOut;
    private CheckBox cbPremium;
    private Switch switchNotifications;

    private Calendar selectedDate;
    private boolean isEditMode = false;
    private int editTripId = -1;
    private Trip editingTrip;
    private TripPreferences tripPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_trip);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        tripPreferences = new TripPreferences(this);
        selectedDate = Calendar.getInstance();

        initializeViews();

        isEditMode = getIntent().getBooleanExtra("EDIT_MODE", false);
        if (isEditMode) {
            editTripId = getIntent().getIntExtra("TRIP_ID", -1);
            loadTripData();
            setTitle("Edit Trip");
        } else {
            setTitle("Add New Trip");
        }
        setupListeners();
    }

    private void initializeViews() {
        etOrigin = findViewById(R.id.etOrigin);
        etDestination = findViewById(R.id.etDestination);
        etPrice = findViewById(R.id.etPrice);
        etLuggage = findViewById(R.id.etLuggage);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        rgTripType = findViewById(R.id.rgTripType);
        rbCultural = findViewById(R.id.rbCultural);
        rbAdventure = findViewById(R.id.rbAdventure);
        rbRelaxation = findViewById(R.id.rbRelaxation);
        rbReligious = findViewById(R.id.rbReligious);

        rgAvailability = findViewById(R.id.rgAvailability);
        rbAvailable = findViewById(R.id.rbAvailable);
        rbSoldOut = findViewById(R.id.rbSoldOut);

        cbPremium = findViewById(R.id.cbPremium);
        switchNotifications = findViewById(R.id.switchNotifications);
    }

    private void setupListeners() {
        btnSelectDate.setOnClickListener(v -> showDatePicker());
        btnSave.setOnClickListener(v -> saveTrip());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();

        // If editing, use the trip’s saved date
        if (isEditMode && editingTrip != null) {
            cal.setTimeInMillis(editingTrip.getDepartureDate());
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate.set(year, month, dayOfMonth);
                    updateDateButton();
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }


    private void updateDateButton() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        btnSelectDate.setText(sdf.format(selectedDate.getTime()));
    }

    private void loadTripData() {
        editingTrip = findTripById(editTripId);
        if (editingTrip == null) {
            Toast.makeText(this, "Trip not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etOrigin.setText(editingTrip.getOrigin());
        etDestination.setText(editingTrip.getDestination());
        etPrice.setText(String.valueOf(editingTrip.getPrice()));
        etLuggage.setText(String.valueOf(editingTrip.getLuggage()));

        // Set date
        selectedDate.setTimeInMillis(editingTrip.getDepartureDate());
        updateDateButton();

        // Set trip type radio button
        switch (editingTrip.getTripType()) {
            case "Cultural":
                rbCultural.setChecked(true);
                break;
            case "Adventure":
                rbAdventure.setChecked(true);
                break;
            case "Relaxation":
                rbRelaxation.setChecked(true);
                break;
            case "Religious":
                rbReligious.setChecked(true);
                break;
        }

        // Set availability
        if (editingTrip.isAvailable()) {
            rbAvailable.setChecked(true);
        } else {
            rbSoldOut.setChecked(true);
        }

        // Set premium checkbox
        cbPremium.setChecked(editingTrip.isPremium());

        // Set notifications switch
        switchNotifications.setChecked(editingTrip.isNotificationsEnabled());
    }

    private Trip findTripById(int id) {
        for (Trip trip : Trip.trips) {
            if (trip.getId() == id) {
                return trip;
            }
        }
        return null;
    }

    private void saveTrip() {
        if (!validateInputs()) {
            return;
        }

        String origin = etOrigin.getText().toString().trim();
        String destination = etDestination.getText().toString().trim();
        int price = Integer.parseInt(etPrice.getText().toString().trim());
        int luggage = Integer.parseInt(etLuggage.getText().toString().trim());
        String tripType = "Cultural"; // default
        int selectedTypeId = rgTripType.getCheckedRadioButtonId();
        if (selectedTypeId == R.id.rbCultural) {
            tripType = "Cultural";
        } else if (selectedTypeId == R.id.rbAdventure) {
            tripType = "Adventure";
        } else if (selectedTypeId == R.id.rbRelaxation) {
            tripType = "Relaxation";
        } else if (selectedTypeId == R.id.rbReligious) {
            tripType = "Religious";
        }

        boolean available = rbAvailable.isChecked();
        boolean isPremium = cbPremium.isChecked();
        boolean notificationsEnabled = switchNotifications.isChecked();
        int imageId = isEditMode ? editingTrip.getImageId() : R.drawable.placeholder;

        if (isEditMode) {
            editingTrip.setOrigin(origin);
            editingTrip.setDestination(destination);
            editingTrip.setPrice(price);
            editingTrip.setLuggage(luggage);
            editingTrip.setDepartureDate(selectedDate.getTimeInMillis());
            editingTrip.setTripType(tripType);
            editingTrip.setAvailable(available);
            editingTrip.setPremium(isPremium);
            editingTrip.setNotificationsEnabled(notificationsEnabled);

            Toast.makeText(this, "Trip updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Trip newTrip = new Trip(Trip.getNextId(), price, luggage, available, origin, destination, selectedDate.getTimeInMillis(), imageId, tripType, isPremium, notificationsEnabled);
            Trip.trips.add(newTrip);

            Toast.makeText(this, "Trip added successfully", Toast.LENGTH_SHORT).show();
        }

        tripPreferences.saveTrips(Trip.trips);
        tripPreferences.saveNextId(Trip.getNextId());

        finish();
    }

    private boolean validateInputs() {
        if (etOrigin.getText().toString().trim().isEmpty()) {
            etOrigin.setError("Origin is required");
            etOrigin.requestFocus();
            return false;
        }

        if (etDestination.getText().toString().trim().isEmpty()) {
            etDestination.setError("Destination is required");
            etDestination.requestFocus();
            return false;
        }

        if (etPrice.getText().toString().trim().isEmpty()) {
            etPrice.setError("Price is required");
            etPrice.requestFocus();
            return false;
        }

        try {
            int price = Integer.parseInt(etPrice.getText().toString().trim());
            if (price <= 0) {
                etPrice.setError("Price must be greater than 0");
                etPrice.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            etPrice.setError("Invalid price");
            etPrice.requestFocus();
            return false;
        }

        if (etLuggage.getText().toString().trim().isEmpty()) {
            etLuggage.setError("Luggage count is required");
            etLuggage.requestFocus();
            return false;
        }

        try {
            int luggage = Integer.parseInt(etLuggage.getText().toString().trim());
            if (luggage < 0) {
                etLuggage.setError("Luggage count cannot be negative");
                etLuggage.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            etLuggage.setError("Invalid luggage count");
            etLuggage.requestFocus();
            return false;
        }

        if (rgTripType.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select a trip type", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (rgAvailability.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select availability", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}