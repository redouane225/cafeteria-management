package com.example.cafeteriamanagement.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafeteriamanagement.R;
import com.example.cafeteriamanagement.model.User;


import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder> {
    private Context context;
    private List<User> staffList;
    private OnStaffClickListener listener;

    // Interface pour gérer le clic sur un élément du staff
    public interface OnStaffClickListener {
        void onStaffClick(User staff);
    }

    public StaffAdapter(Context context, List<User> staffList, OnStaffClickListener listener) {
        this.context = context;
        this.staffList = staffList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.staff_card, parent, false);
        return new StaffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder holder, int position) {
        User staff = staffList.get(position);
        holder.name.setText(staff.getUsername());
        holder.role.setText(staff.getRole());

        // Affichage du statut (juste un exemple, à adapter si tu as un vrai statut)
        holder.status.setText("Active");

        // Gestion du clic sur l'élément
        holder.itemView.setOnClickListener(v -> listener.onStaffClick(staff));

        // Gestion du menu des options (modifier/supprimer)
        holder.menuButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.menuButton);
            popupMenu.inflate(R.menu.menu_editing);
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.edit_staff) {
                    Toast.makeText(context, "Modifier " + staff.getUsername(), Toast.LENGTH_SHORT).show();
                    return true;
                } else if (item.getItemId() == R.id.delete_staff) {
                    Toast.makeText(context, "Supprimer " + staff.getUsername(), Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }

    public static class StaffViewHolder extends RecyclerView.ViewHolder {
        TextView name, role, status;
        ImageView menuButton;

        public StaffViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.staffName);
            role = itemView.findViewById(R.id.staffRole);
            status = itemView.findViewById(R.id.staffStatus);
            menuButton = itemView.findViewById(R.id.staffMenuButton);
            public void filter (String text) {
                staffList.clear();
                if (text.isEmpty()) {
                    staffList.addAll(fullStaffList); // Reset to full list if search is empty
                } else {
                    text = text.toLowerCase();
                    for (Staff staff : fullStaffList) {
                        if (staff.getUsername().toLowerCase().contains(text) ||
                                staff.getRole().toLowerCase().contains(text)) {
                            staffList.add(staff);
                        }
                    }
                }
                notifyDataSetChanged(); // Refresh the RecyclerView
            }
        }
        }
}

