package com.example.tripplanningmobileapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Trip {
    private int id;
    private int price;
    private int luggage;
    private boolean available;
    private String origin;
    private String destination;
    private long departureDate; // Changed to long for easy storage
    private int imageId;
    private String tripType; // New field for radio button
    private boolean isPremium; // New field for checkbox
    private boolean notificationsEnabled; // New field for switch

    public Trip(int id, int price, int luggage, boolean available,
                String origin, String destination,
                long departureDate, int imageId,
                String tripType, boolean isPremium, boolean notificationsEnabled) {
        this.id = id;
        this.price = price;
        this.luggage = luggage;
        this.available = available;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.imageId = imageId;
        this.tripType = tripType;
        this.isPremium = isPremium;
        this.notificationsEnabled = notificationsEnabled;
    }

    // Static list - will be loaded from SharedPreferences
    public static final ArrayList<Trip> trips = new ArrayList<>();
    private static int nextId = 1;

    // Initialize with default trips if SharedPreferences is empty
    public static void initializeDefaultTrips() {
        if (trips.isEmpty()) {
            long currentTime = System.currentTimeMillis();

            trips.add(new Trip(nextId++, 1200, 2, true,
                    "Ramallah", "Jerusalem",
                    currentTime, R.drawable.jerusalem,
                    "Cultural", true, true));

            trips.add(new Trip(nextId++, 450, 1, true,
                    "Ramallah", "Nablus",
                    currentTime + 86400000L, R.drawable.nablus,
                    "Adventure", false, false));

            trips.add(new Trip(nextId++, 850, 2, false,
                    "Ramallah", "Jericho",
                    currentTime + 172800000L, R.drawable.jericho,
                    "Relaxation", true, true));

            trips.add(new Trip(nextId++, 1800, 3, true,
                    "Ramallah", "Sabastia",
                    currentTime + 259200000L, R.drawable.ramallah,
                    "Cultural", true, false));

            trips.add(new Trip(nextId++, 300, 1, false,
                    "Ramallah", "Bethlehem",
                    currentTime + 345600000L, R.drawable.bethlehem,
                    "Religious", false, true));

            trips.add(new Trip(nextId++, 950, 2, true,
                    "Ramallah", "Multiple cities with homestay",
                    currentTime + 432000000L, R.drawable.hebron,
                    "Adventure", true, true));
        }
    }

    public static int getNextId() {
        return nextId++;
    }

    public static void setNextId(int id) {
        nextId = id;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public int getLuggage() {
        return luggage;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public long getDepartureDate() {
        return departureDate;
    }

    public int getImageId() {
        return imageId;
    }

    public String getTripType() {
        return tripType;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setLuggage(int luggage) {
        this.luggage = luggage;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDepartureDate(long departureDate) {
        this.departureDate = departureDate;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    // Utility method to format date
    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return sdf.format(new Date(departureDate));
    }

    // Method to search trips
    public boolean matchesSearch(String query) {
        if (query == null || query.trim().isEmpty()) {
            return true;
        }
        String lowerQuery = query.toLowerCase();
        return destination.toLowerCase().contains(lowerQuery) ||
                origin.toLowerCase().contains(lowerQuery) ||
                tripType.toLowerCase().contains(lowerQuery) ||
                String.valueOf(price).contains(lowerQuery);
    }
}