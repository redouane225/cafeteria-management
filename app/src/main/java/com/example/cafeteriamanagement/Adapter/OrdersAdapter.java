package com.example.cafeteriamanagement.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafeteriamanagement.model.Order;
import com.example.cafeteriamanagement.R;
import com.example.cafeteriamanagement.model.OrderItem;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {
    private final List<Order> orders;

    public OrdersAdapter(List<Order> orders) {
        this.orders = orders;
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
        holder.tableNumberTextView.setText("Table " + order.getTableNumber()); // No change needed since it handles int

        StringBuilder itemsSummary = new StringBuilder();
        for (OrderItem item : order.getItems()) {
            itemsSummary.append(item.getQuantity()).append("x ").append(item.getItemName()).append("\n ");
        }
        if (itemsSummary.length() > 0) {
            itemsSummary.setLength(itemsSummary.length() - 2); // Remove the trailing comma
        }

        holder.itemsTextView.setText(itemsSummary.toString());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tableNumberTextView, itemsTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tableNumberTextView = itemView.findViewById(R.id.table_number);
            itemsTextView = itemView.findViewById(R.id.itemList);
        }
    }
}