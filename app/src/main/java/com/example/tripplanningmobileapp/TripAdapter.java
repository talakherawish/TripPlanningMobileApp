package com.example.tripplanningmobileapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> implements Filterable {

    private ArrayList<Trip> trips;
    private ArrayList<Trip> tripsFull;

    public TripAdapter(ArrayList<Trip> trips) {
        this.trips = trips;
        this.tripsFull = new ArrayList<>(trips);
    }

    public void updateTrips(ArrayList<Trip> newTrips) {
        trips.clear();
        trips.addAll(newTrips);
        tripsFull.clear();
        tripsFull.addAll(newTrips);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView tripImage;
        TextView tripName;
        TextView tripPrice;
        TextView tripDate;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            tripImage = itemView.findViewById(R.id.tripImage);
            tripName = itemView.findViewById(R.id.tripName);
            tripPrice = itemView.findViewById(R.id.tripPrice);
            tripDate = itemView.findViewById(R.id.departureDate);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Trip trip = trips.get(position);

        holder.tripName.setText(trip.getDestination());
        holder.tripPrice.setText("$" + trip.getPrice());
        holder.tripImage.setImageResource(trip.getImageId());
        holder.tripDate.setText(trip.getFormattedDate());

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), TripDetails.class);
            intent.putExtra("TRIP_ID", trip.getId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return trips == null ? 0 : trips.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Trip> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(tripsFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Trip trip : tripsFull) {
                        if (trip.matchesSearch(filterPattern)) {
                            filteredList.add(trip);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                trips.clear();
                trips.addAll((ArrayList<Trip>) results.values);
                notifyDataSetChanged();
            }
        };
    }
}