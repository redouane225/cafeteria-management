package com.example.cafeteriamanagement.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafeteriamanagement.Network.ApiCallback;
import com.example.cafeteriamanagement.Network.ApiService;
import com.example.cafeteriamanagement.R;
import com.example.cafeteriamanagement.model.Order;
import com.example.cafeteriamanagement.model.OrderItem;

import java.util.Arrays;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {
    private final List<Order> orders;
    private final Context context;

    // List of possible statuses (can be extended)
    private static final List<String> STATUS_LIST = Arrays.asList("in progress", "completed");

    public OrdersAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    public Order deleteOrder(int position) {
        Order removedOrder = orders.get(position);
        orders.remove(position);
        notifyItemRemoved(position);
        return removedOrder;
    }

    public void restoreOrder(Order order, int position) {
        orders.add(position, order);
        notifyItemInserted(position);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);

        // Bind table number
        holder.tableNumberTextView.setText("Table " + order.getTableNumber());

        // Create items summary
        StringBuilder itemsSummary = new StringBuilder();
        for (OrderItem item : order.getItems()) {
            itemsSummary.append(item.getQuantity()).append("x ").append(item.getItemName()).append("\n");
        }
        holder.itemsTextView.setText(itemsSummary.toString().trim());

        // Set the current status and color
        String currentStatus = order.getStatus();
        holder.statusTextView.setText(currentStatus);
        setStatusColor(holder.statusTextView, currentStatus);

        // Status click listener to toggle the status
        holder.statusTextView.setOnClickListener(v -> {
            // Determine the next status dynamically
            String newStatus = getNextStatus(currentStatus);

            // Update the status via the API
            ApiService.updateOrderStatus(order.getId(), newStatus, new ApiCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    ((Activity) holder.itemView.getContext()).runOnUiThread(() -> {
                        // Update UI on success
                        order.setStatus(newStatus);
                        holder.statusTextView.setText(newStatus);
                        setStatusColor(holder.statusTextView, newStatus);
                        Toast.makeText(context, "Order status updated to " + newStatus, Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onError(String errorMessage) {
                    // Show error message
                    Toast.makeText(context, "Failed to update status: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    // Helper method to determine the next status
    private String getNextStatus(String currentStatus) {
        int currentIndex = STATUS_LIST.indexOf(currentStatus);
        if (currentIndex == -1 || currentIndex == STATUS_LIST.size() - 1) {
            // If status is not found or it's the last in the list, return the first status
            return STATUS_LIST.get(0);
        }
        // Return the next status in the list
        return STATUS_LIST.get(currentIndex + 1);
    }

    // Helper method to set the status color
    private void setStatusColor(TextView statusTextView, String status) {
        switch (status) {
            case "in progress":
                statusTextView.setTextColor(ContextCompat.getColor(context, R.color.amber));
                break;
            case "completed":
                statusTextView.setTextColor(ContextCompat.getColor(context, R.color.green));
                break;
            default:
                statusTextView.setTextColor(ContextCompat.getColor(context, R.color.black));
                break;
        }
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        final TextView tableNumberTextView, itemsTextView, statusTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tableNumberTextView = itemView.findViewById(R.id.table_number);
            itemsTextView = itemView.findViewById(R.id.itemList);
            statusTextView = itemView.findViewById(R.id.StatusChip);
        }
    }
}