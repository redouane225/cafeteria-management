package com.example.cafeteriamanagement.UI.fragmentsStaff;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cafeteriamanagement.Network.ApiCallback;
import com.example.cafeteriamanagement.Network.ApiService;
import com.example.cafeteriamanagement.databinding.FragmentNewOrderBinding;
import com.example.cafeteriamanagement.databinding.ItemOrderRowBinding;
import com.example.cafeteriamanagement.model.MenuItem;
import com.example.cafeteriamanagement.model.Table;
import com.example.cafeteriamanagement.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class newOrder extends Fragment {

    private FragmentNewOrderBinding binding;
    private List<Table> tableList = new ArrayList<>();
    private List<String> menuItemsList = new ArrayList<>();
    private static final String TAG = "NewOrder";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentNewOrderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fetchTables();
        fetchMenuItems();

        binding.btnAddItem.setOnClickListener(v -> addNewItemRow());
        binding.btnSubmitOrder.setOnClickListener(v -> submitOrder());
    }

    private void fetchTables() {
        ApiService.getTables(new ApiCallback<List<Table>>() {
            @Override
            public void onSuccess(List<Table> result) {
                tableList = result;
                List<String> tableNumbers = new ArrayList<>();
                tableNumbers.add("Select a table");
                for (Table t : result) {
                    tableNumbers.add(String.valueOf(t.getNumber()));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_spinner_item, tableNumbers);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinnerTable.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {
                getActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), "Failed to load tables", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Table fetch error: " + errorMessage);
                });
            }
        });
    }

    private void fetchMenuItems() {
        ApiService.getMenuItems(new ApiCallback<List<MenuItem>>() {
            @Override
            public void onSuccess(List<MenuItem> result) {
                menuItemsList.clear();
                for (MenuItem item : result) {
                    menuItemsList.add(item.getName());
                }
            }

            @Override
            public void onError(String errorMessage) {
                getActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), "Failed to load menu items", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Menu items fetch error: " + errorMessage);});
            }
        });
    }

    private void addNewItemRow() {
        ItemOrderRowBinding itemBinding = ItemOrderRowBinding.inflate(LayoutInflater.from(requireContext()));

        // Set adapter
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, menuItemsList);
        itemBinding.ItemName.setAdapter(itemAdapter);

        // Remove item button
        itemBinding.btnRemoveItem.setOnClickListener(v ->
                binding.itemsContainer.removeView(itemBinding.getRoot()));

        // Apply margins
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(10, 10, 10, 20);// left, top, right, bottom
        itemBinding.getRoot().setLayoutParams(params);

        // Add the row to container
        binding.itemsContainer.addView(itemBinding.getRoot());
    }


    private void submitOrder() {
        try {
            int selectedPosition = binding.spinnerTable.getSelectedItemPosition();
            if (selectedPosition == 0) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Please select a table", Toast.LENGTH_SHORT).show();});
                return;
            }

            String tableNumberStr = binding.spinnerTable.getSelectedItem().toString();
            String specialRequest = binding.etSpecialRequest.getText().toString().trim();

            JSONArray itemsArray = new JSONArray();
            int itemCount = binding.itemsContainer.getChildCount();

            if (itemCount == 0) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Add at least one item", Toast.LENGTH_SHORT).show();});
                return;
            }

            for (int i = 0; i < itemCount; i++) {
                View itemView = binding.itemsContainer.getChildAt(i);
                AutoCompleteTextView actvItemName = itemView.findViewById(R.id.ItemName);
                EditText etQuantity = itemView.findViewById(R.id.etQuantity);

                String itemName = actvItemName.getText().toString().trim();
                String quantityStr = etQuantity.getText().toString().trim();

                if (itemName.isEmpty() || quantityStr.isEmpty()) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "name and quantity must be filled", Toast.LENGTH_SHORT).show();});
                    return;
                }

                int quantity = Integer.parseInt(quantityStr);
                if (quantity <= 0) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "Quantity must be greater than 0", Toast.LENGTH_SHORT).show();});
                    return;
                }

                JSONObject itemJson = new JSONObject();
                itemJson.put("item_name", itemName);
                itemJson.put("quantity", quantity);
                itemsArray.put(itemJson);
            }

            JSONObject orderJson = new JSONObject();
            orderJson.put("table_number", Integer.parseInt(tableNumberStr));
            orderJson.put("special_request", specialRequest.isEmpty() ? "no special request" : specialRequest);
            orderJson.put("items", itemsArray);

            Log.d("json format", "Sending order: " + orderJson.toString());


            binding.btnSubmitOrder.setEnabled(false);
            ApiService.addNewOrder(orderJson, new ApiCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "Order submitted!", Toast.LENGTH_SHORT).show();
                        clearForm();
                    });
                }

                @Override
                public void onError(String errorMessage) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "Submission failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Order submit error: " + errorMessage);
                        binding.btnSubmitOrder.setEnabled(true); // Re-enable button
                    });
                }
            });

        } catch (JSONException | NumberFormatException e) {
            e.printStackTrace();
            requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), "Error parsing form. Check your inputs.", Toast.LENGTH_SHORT).show();});
        }
    }


    private void clearForm() {

        binding.spinnerTable.setSelection(0);  // resets to "Select a table"
        binding.etSpecialRequest.setText("");
        binding.itemsContainer.removeAllViews();

        }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}