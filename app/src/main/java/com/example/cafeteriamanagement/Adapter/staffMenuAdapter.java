package com.example.cafeteriamanagement.Adapter;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafeteriamanagement.R;
import com.example.cafeteriamanagement.model.MenuItem;

import java.util.List;

public class staffMenuAdapter extends RecyclerView.Adapter<staffMenuAdapter.MenuViewHolder> {

    private final List<MenuItem> menuItemList; // Reference to the list of menu items

    public staffMenuAdapter(List<MenuItem> menuItemList) {
        this.menuItemList = menuItemList; // Initialize the list
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {

        MenuItem menuItem = menuItemList.get(position);
        holder.bind(menuItem);
    }

    @Override
    public int getItemCount() {
        return menuItemList.size();
    }

    class MenuViewHolder extends RecyclerView.ViewHolder {

        private final TextView itemNameTextView;
        private final TextView itemPriceTextView;
        private final TextView availability;

        MenuViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize the views in the card layout
            itemNameTextView = itemView.findViewById(R.id.item_name);
            itemPriceTextView = itemView.findViewById(R.id.item_price);
            availability = itemView.findViewById(R.id.menu_availability);
        }

        void bind(MenuItem menuItem) {
            // Bind the data to the views
            if (menuItem != null) {
                itemNameTextView.setText(menuItem.getName());
                itemPriceTextView.setText(String.format("$%.2f", menuItem.getPrice()));
                availability.setText(menuItem.getIsAvailable());

                // Set the availability text color
                if (STATUS_AVAILABLE.equalsIgnoreCase(menuItem.getIsAvailable())) {
                    availability.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.green));
                } else {
                    availability.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.red));
                }
            } else {
                // Handle null menu item (should not happen)
                itemNameTextView.setText("Unknown");
                itemPriceTextView.setText("N/A");
                availability.setText("Unavailable");
                availability.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.red));
            }
        }
    }

    // Constants for availability status
    private static final String STATUS_AVAILABLE = "Available";
    private static final String STATUS_UNAVAILABLE = "Unavailable";
}