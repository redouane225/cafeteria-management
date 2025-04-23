package com.example.cafeteriamanagement.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafeteriamanagement.R;
import com.example.cafeteriamanagement.model.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private final List<MenuItem> menuItemList;
    private final List<MenuItem> menuItemListFull;
    private final OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(MenuItem menuItem);
    }

    public MenuAdapter(List<MenuItem> menuItemList, OnItemClickListener onItemClickListener) {
        this.menuItemList = new ArrayList<>(menuItemList);
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
        for (int i = 0; i < menuItemList.size(); i++) {
            if (menuItemList.get(i).getId() == updatedItem.getId()) {
                menuItemList.set(i, updatedItem);
                notifyItemChanged(i);
                return;
            }
        }
        // Add new item if not found
        menuItemList.add(updatedItem);
        notifyItemInserted(menuItemList.size() - 1);
        Log.d("MenuFlow", "MenuAdapter updating item: " + updatedItem.getName());

    }

    class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView itemNameTextView;
        private final TextView itemPriceTextView;

        MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.item_name);
            itemPriceTextView = itemView.findViewById(R.id.item_price);
            itemView.setOnClickListener(this);
        }

        void bind(MenuItem menuItem) {
            itemNameTextView.setText(menuItem.getName());
            itemPriceTextView.setText(String.valueOf(menuItem.getPrice()));
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
