package com.example.cafeteriamanagement.Adapter;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.ViewHolder> {

    private List<String> staffList; // Dummy list to prevent errors

    public StaffAdapter(List<String> staffList) {
        this.staffList = staffList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // TODO: Inflate your staff item layout here
        return new ViewHolder(new View(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // TODO: Bind data to your views here
    }

    @Override
    public int getItemCount() {
        return staffList != null ? staffList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // TODO: Initialize your UI components here
        }
    }
}
