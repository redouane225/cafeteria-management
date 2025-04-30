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

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private final List<MenuItem> menuItemList;        // Reference to fragment's list
    private final List<MenuItem> menuItemListFull;    // For filtering
    private final OnItemClickListener onItemClickListener;

    private static final String STATUS_AVAILABLE = "Available";
    private static final String STATUS_UNAVAILABLE = "Unavailable";

    public interface OnItemClickListener {
        void onItemClick(MenuItem menuItem);
    }

    public MenuAdapter(List<MenuItem> menuItemList, OnItemClickListener onItemClickListener) {
        this.menuItemList = menuItemList;
        this.menuItemListFull = new ArrayList<>(menuItemList);
        this.onItemClickListener = onItemClickListener;
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

    public void updateMenuItem(MenuItem updatedItem) {
        synchronized (menuItemList) {
            for (int i = 0; i < menuItemList.size(); i++) {
                if (menuItemList.get(i).getId() == updatedItem.getId()) {
                    menuItemList.set(i, updatedItem);
                    menuItemListFull.set(i, updatedItem);
                    notifyItemChanged(i);
                    Log.d("MenuFlow", "Updated item with ID: " + updatedItem.getId());
                    return;
                }
            }
            // Item not found, add new
            menuItemList.add(updatedItem);
            menuItemListFull.add(updatedItem);
            notifyItemInserted(menuItemList.size() - 1);
            Log.d("MenuFlow", "Added new item with ID: " + updatedItem.getId());
        }
    }

    public void filter(String query) {
        menuItemList.clear();
        if (query.isEmpty()) {
            menuItemList.addAll(menuItemListFull);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (MenuItem item : menuItemListFull) {
                if (item.getName().toLowerCase().contains(lowerCaseQuery)) {
                    menuItemList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView itemNameTextView;
        private final TextView itemPriceTextView;
        private final TextView availability;

        MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.item_name);
            itemPriceTextView = itemView.findViewById(R.id.item_price);
            availability = itemView.findViewById(R.id.menu_availability);
            itemView.setOnClickListener(this);
        }

        void bind(MenuItem menuItem) {
            if (menuItem != null) {
                itemNameTextView.setText(menuItem.getName());
                itemPriceTextView.setText(String.format("$%.2f", menuItem.getPrice()));
                availability.setText(menuItem.getIsAvailable());

                if (STATUS_AVAILABLE.equalsIgnoreCase(menuItem.getIsAvailable())) {
                    availability.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.green));
                } else {
                    availability.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.red));
                }
            } else {
                itemNameTextView.setText("Unknown");
                itemPriceTextView.setText("N/A");
                availability.setText("Unavailable");
                availability.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.red));
            }
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                onItemClickListener.onItemClick(menuItemList.get(position));
            }
        }
    }
}
