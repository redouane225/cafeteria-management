package com.example.cafeteriamanagement.UI.fragmentsStaff;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafeteriamanagement.Adapter.OrdersAdapter;
import com.example.cafeteriamanagement.Network.ApiCallback;
import com.example.cafeteriamanagement.Network.ApiService;
import com.example.cafeteriamanagement.databinding.FragmentOrdersListBinding;
import com.example.cafeteriamanagement.model.Order;
import com.example.cafeteriamanagement.model.OrderItem;
import com.example.cafeteriamanagement.R;

import java.util.ArrayList;
import java.util.List;


public class ordersList extends Fragment {
    private FragmentOrdersListBinding binding; // Binding object for the fragment
    private OrdersAdapter ordersAdapter;
    private List<Order> ordersList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Initialize the binding object
        binding = FragmentOrdersListBinding.inflate(inflater, container, false);

        // Initialize RecyclerView and Adapter
        binding.ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ordersList = new ArrayList<>();
        ordersAdapter = new OrdersAdapter(ordersList);
        binding.ordersRecyclerView.setAdapter(ordersAdapter);

        // Fetch orders from API
        fetchOrders();

        return binding.getRoot(); // Return the root view of the binding
    }

    private void fetchOrders() {
        ApiService.getOrders(new ApiCallback<List<Order>>() {
            @Override
            public void onSuccess(List<Order> orders) {
                // Update the orders list and notify the adapter
                ordersList.clear();
                ordersList.addAll(orders);

                requireActivity().runOnUiThread(() -> ordersAdapter.notifyDataSetChanged());
            }

            @Override
            public void onError(String errorMessage) {
                // Handle errors and notify the user
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Error fetching orders: " + errorMessage, Toast.LENGTH_LONG).show()
                );
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Set binding to null to avoid memory leaks
    }
}