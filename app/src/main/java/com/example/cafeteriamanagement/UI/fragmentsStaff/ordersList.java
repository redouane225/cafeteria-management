package com.example.cafeteriamanagement.UI.fragmentsStaff;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafeteriamanagement.Adapter.OrdersAdapter;
import com.example.cafeteriamanagement.Network.ApiCallback;
import com.example.cafeteriamanagement.Network.ApiService;
import com.example.cafeteriamanagement.databinding.FragmentOrdersListBinding;
import com.example.cafeteriamanagement.model.Order;
import com.example.cafeteriamanagement.model.OrderItem;
import com.example.cafeteriamanagement.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


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
        ordersAdapter = new OrdersAdapter(requireContext(),ordersList);
        binding.ordersRecyclerView.setAdapter(ordersAdapter);

        // Fetch orders from API
        fetchOrders(() -> {
            Log.d("order", "Initial orders fetched successfully.");
        });

        binding.swipeRefreshLayout.setOnRefreshListener(() ->{

            fetchOrders(() -> {
                // Stop the refreshing animation once data has been loaded
                binding.swipeRefreshLayout.setRefreshing(false);
            });
        });


        // Add Swipe-to-Delete functionality
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // No drag-and-drop functionality
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Order removedOrder = ordersAdapter.deleteOrder(position); // Remove order from adapter

                // Show Snackbar with Undo option
                Snackbar.make(binding.getRoot(), "Order deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo", v -> ordersAdapter.restoreOrder(removedOrder, position)) // Restore order on "Undo"
                        .setAnchorView(requireActivity().findViewById(R.id.staff_btmnavbar)) // Replace with the ID of your bottom view
                        .addCallback(new Snackbar.Callback() {

                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                if (event != DISMISS_EVENT_ACTION) {
                                    // Notify backend to delete the order
                                    ApiService.deleteOrder(removedOrder.getId(), new ApiCallback<String>() {

                                        @Override
                                        public void onSuccess(String result) {
                                            requireActivity().runOnUiThread(() ->
                                                    Toast.makeText(requireContext(), "Order deleted ", Toast.LENGTH_SHORT).show()
                                            );
                                        }

                                        @Override
                                        public void onError(String errorMessage) {
                                            requireActivity().runOnUiThread(() ->
                                                    Toast.makeText(requireContext(), "Failed to delete : " + errorMessage, Toast.LENGTH_LONG).show()
                                            );
                                        }
                                    });
                                    Log.d("DELETE_ORDER", "Order ID to delete: " + removedOrder.getId());
                                }
                            }
                        })
                        .show();
            }
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                // Add background and delete icon during swipe
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(Color.parseColor("#800000")) // Red background
                        .addActionIcon(R.drawable.ic_delete) // Delete icon
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        });

        itemTouchHelper.attachToRecyclerView(binding.ordersRecyclerView);



        return binding.getRoot(); // Return the root view of the binding
    }

    private void fetchOrders(Runnable onComplete) {
        ApiService.getOrders(new ApiCallback<List<Order>>() {
            @Override
            public void onSuccess(List<Order> orders) {
                // Update the orders list and notify the adapter
                ordersList.clear();
                ordersList.addAll(orders);
                requireActivity().runOnUiThread(() -> ordersAdapter.notifyDataSetChanged());
                onComplete.run();
            }

            @Override
            public void onError(String errorMessage) {
                // Handle errors and notify the user
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Error fetching orders: " + errorMessage, Toast.LENGTH_LONG).show()
                );
                onComplete.run();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Set binding to null to avoid memory leaks
    }
}