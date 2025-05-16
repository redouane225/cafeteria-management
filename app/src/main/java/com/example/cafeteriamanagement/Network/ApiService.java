package com.example.cafeteriamanagement.Network;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cafeteriamanagement.model.MenuItem;
import com.example.cafeteriamanagement.model.Order;
import com.example.cafeteriamanagement.model.OrderItem;
import com.example.cafeteriamanagement.model.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiService {

    private static final String TAG = "ApiService";
    private static void runOnUiThread(Runnable task) {
        new Handler(Looper.getMainLooper()).post(task);
    }

    // MediaType for JSON
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    // Log in user
    public static void login(String username, String password, ApiCallback<String> callback) {
        OkHttpClient client = ApiClient.getClient();

        try {
            JSONObject loginJson = new JSONObject();
            loginJson.put("username", username);
            loginJson.put("password", password);

            RequestBody requestBody = RequestBody.create(loginJson.toString(), JSON);

            Request request = new Request.Builder()
                    .url(ApiEndpoint.LOG_IN)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "Login request failed", e);
                    callback.onError("Network error. Please try again.");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            String responseBody = response.body().string();
                            JSONObject jsonResponse = new JSONObject(responseBody);

                            if (jsonResponse.getString("status").equals("success")) {
                                String role = jsonResponse.getString("role");
                                callback.onSuccess(role);
                            } else {
                                String message = jsonResponse.optString("message", "Invalid credentials");
                                callback.onError(message);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON parsing error", e);
                            callback.onError("Failed to parse response");
                        }
                    } else {
                        callback.onError("Login failed: " + response.message());
                    }
                }
            });
        } catch (JSONException e) {
            Log.e(TAG, "Failed to create login request body", e);
            callback.onError("Failed to create request body");
        }
    }

    // GET Menu Items
    public static void getMenuItems(ApiCallback<List<MenuItem>> callback) {
        OkHttpClient client = ApiClient.getClient();

        Request request = new Request.Builder()
                .url(ApiEndpoint.GET_MENU_ITEMS)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Failed to fetch menu items", e);
                runOnUiThread(() -> callback.onError(e.getMessage()));
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject menuObject = new JSONObject(responseBody);
                        JSONArray menuItemsArray = menuObject.getJSONArray("data");
                        List<MenuItem> menuItems = new ArrayList<>();
                        Log.d(TAG, responseBody);
                        // Parse JSON array into Java objects
                        for (int i = 0; i < menuItemsArray.length(); i++) {
                            JSONObject menuItemJson = menuItemsArray.getJSONObject(i);
                            MenuItem menuItem = new MenuItem(
                                    menuItemJson.getInt("id"),
                                    menuItemJson.getString("name"),
                                    menuItemJson.getDouble("price"),
                                    menuItemJson.getString("availability"),
                                    menuItemJson.getString("category")

                            );
                            menuItems.add(menuItem);
                            Log.d(TAG, "Parsed menu items: " + menuItems.size());
                        }

                            for (MenuItem item : menuItems) {
                                Log.d(TAG, "Item: " + item.getName() + ", Category: " + item.getCategory());
                            }



                        runOnUiThread(() -> callback.onSuccess(menuItems));

                    } catch (JSONException e) {
                        Log.e(TAG, "JSON parsing error", e);
                        runOnUiThread(() -> callback.onError("Failed to parse response"));
                    }
                } else {
                    runOnUiThread(() -> callback.onError("Failed to fetch menu items: " + response.message()));
                }
            }
        });
    }

    // UPDATE Menu Item
    public static void updateMenuItem(MenuItem menuItem, ApiCallback<String> callback) {
        OkHttpClient client = ApiClient.getClient();

        try {
            JSONObject menuItemJson = new JSONObject();
            menuItemJson.put("id", menuItem.getId());
            menuItemJson.put("name", menuItem.getName());
            menuItemJson.put("price", menuItem.getPrice());
            menuItemJson.put("availability", menuItem.getIsAvailable());
            menuItemJson.put("category", menuItem.getCategory());

            RequestBody requestBody = RequestBody.create(menuItemJson.toString(), JSON);

            Request request = new Request.Builder()
                    .url(ApiEndpoint.UPDATE_MENU_ITEM)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "Failed to update menu item", e);
                    runOnUiThread(() -> callback.onError(e.getMessage()));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            String responseBody = response.body().string();
                            JSONObject jsonResponse = new JSONObject(responseBody);
                            String message = jsonResponse.optString("message", "Menu item updated successfully");
                            runOnUiThread(() -> callback.onSuccess(message));
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON parsing error", e);
                            runOnUiThread(() -> callback.onError("Failed to parse response"));
                        }
                    } else {
                        runOnUiThread(() -> callback.onError("Failed to update menu item: " + response.message()));
                    }
                }
            });

        } catch (JSONException e) {
            Log.e(TAG, "Failed to create JSON body", e);
            runOnUiThread(() -> callback.onError("Failed to create request body"));
        }
    }

    // ADD Menu Item
    public static void addMenuItem(MenuItem menuItem, ApiCallback<String> callback) {
        OkHttpClient client = ApiClient.getClient();

        try {
            JSONObject menuItemJson = new JSONObject();
            menuItemJson.put("name", menuItem.getName());
            menuItemJson.put("price", menuItem.getPrice());
            menuItemJson.put("availability", menuItem.getIsAvailable());
            menuItemJson.put("category", menuItem.getCategory());

            RequestBody requestBody = RequestBody.create(menuItemJson.toString(), JSON);

            Request request = new Request.Builder()
                    .url(ApiEndpoint.ADD_MENU_ITEM)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "Failed to add menu item", e);
                    runOnUiThread(() -> callback.onError(e.getMessage()));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            String responseBody = response.body().string();
                            JSONObject jsonResponse = new JSONObject(responseBody);
                            String message = jsonResponse.optString("message", "Menu item added successfully");
                            runOnUiThread(() -> callback.onSuccess(message)); // Ensure callback runs on main thread
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON parsing error", e);
                            runOnUiThread(() -> callback.onError("Failed to parse response"));
                        }
                    } else {
                        runOnUiThread(() -> callback.onError("Failed to add menu item: " + response.message()));
                    }
                }
            });

        } catch (JSONException e) {
            Log.e(TAG, "Failed to create JSON body", e);
            runOnUiThread(() -> callback.onError("Failed to create request body"));        }
    }


    public static void getOrders(ApiCallback<List<Order>> callback) {
        OkHttpClient client = ApiClient.getClient(); // Get the OkHttpClient from ApiClient

        Request request = new Request.Builder()
                .url(ApiEndpoint.GET_ORDERS) // Replace with your endpoint constant
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Failed to fetch orders", e);
                runOnUiThread(() -> callback.onError(e.getMessage())); // Notify error on UI thread
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject responseObject = new JSONObject(responseBody); // Parse the response as a JSON object

                        // Extract the "data" array from the response object
                        if (responseObject.has("data")) {
                            JSONArray ordersArray = responseObject.getJSONArray("data");
                            List<Order> ordersList = new ArrayList<>();

                            Log.d(TAG, responseBody);

                            // Parse JSON array into Java objects
                            for (int i = 0; i < ordersArray.length(); i++) {
                                JSONObject orderJson = ordersArray.getJSONObject(i);
                                int orderId = orderJson.getInt("id");
                                int tableNumber = orderJson.getInt("table_nbr");
                                String specialRequest = orderJson.optString("special_request", null);
                                String status = orderJson.getString("status"); // Get the status field

                                // Parse order items
                                JSONArray itemsArray = orderJson.getJSONArray("items");
                                List<OrderItem> items = new ArrayList<>();
                                for (int j = 0; j < itemsArray.length(); j++) {
                                    JSONObject itemJson = itemsArray.getJSONObject(j);
                                    String itemName = itemJson.getString("item_name");
                                    int quantity = itemJson.getInt("quantity");
                                    items.add(new OrderItem(itemName, quantity));
                                }

                                // Create Order object with status
                                Order order = new Order(orderId, tableNumber, specialRequest, items, status);
                                ordersList.add(order);
                            }

                            runOnUiThread(() -> callback.onSuccess(ordersList)); // Notify success on UI thread
                        } else {
                            runOnUiThread(() -> callback.onError("Response does not contain 'data' field"));
                        }

                    } catch (JSONException e) {
                        Log.e(TAG, "JSON parsing error", e);
                        runOnUiThread(() -> callback.onError("Failed to parse response"));
                    }
                } else {
                    runOnUiThread(() -> callback.onError("Failed to fetch orders: " + response.message()));
                }
            }
        });
    }
    public static void getTables(ApiCallback<List<Table>> callback) {
        OkHttpClient client = ApiClient.getClient();
        Request request = new Request.Builder()
                .url(ApiEndpoint.GET_TABLES) // Replace with your endpoint constant
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Failed to connect to server");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onError("Server returned an error");
                    return;
                }

                try {
                    String json = response.body().string();
                    JSONObject jsonObject = new JSONObject(json);

                    if (jsonObject.getString("status").equals("success")) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        List<Table> tables = new ArrayList<>();

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = data.getJSONObject(i);
                            int id = obj.getInt("id");
                            int number = obj.getInt("number");
                            tables.add(new Table(id, number));
                        }
                        callback.onSuccess(tables);
                    } else {
                        callback.onError(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    callback.onError("Parsing error: " + e.getMessage());
                }
            }
        });
    }
    public static void addNewOrder(JSONObject orderJson, ApiCallback<String> callback) {
        OkHttpClient client = ApiClient.getClient();

        try {
            // Add the "status" field to the JSON object
            orderJson.put("status", "in progress"); // Ensure status is always set to "in progress"
        } catch (JSONException e) {
            callback.onError("Failed to add status to order: " + e.getMessage());
            return;
        }

        RequestBody body = RequestBody.create(
                orderJson.toString(), // Convert JSONObject to string
                MediaType.parse("application/json") // Set content type to JSON
        );

        // Build the POST request
        Request request = new Request.Builder()
                .url(ApiEndpoint.ADD_NEW_ORDER)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Failed to connect to server: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onError("Server returned an error: " + response.code());
                    return;
                }

                try {
                    // Parse the JSON response
                    String json = response.body().string();
                    JSONObject jsonObject = new JSONObject(json);

                    if (jsonObject.getString("status").equals("success")) {
                        callback.onSuccess(jsonObject.getString("message"));
                    } else {
                        callback.onError(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    callback.onError("Parsing error: " + e.getMessage());
                }
            }
        });
    }


    // Delete Order API Call
    public static void deleteOrder(int orderId, ApiCallback<String> callback) {

        // Build the request body
        RequestBody body = new FormBody.Builder()
                .add("order_id", Integer.toString(orderId)) // Pass the int directly as a string
                .build();

        // Build the DELETE request
        Request request = new Request.Builder()
                .url(ApiEndpoint.DELETE_ORDER)
                .delete(body) // Attach the body to the DELETE request
                .build();

        // Send the request
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError("Failed to connect to server: " + e.getMessage()); // Handle failure
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        // Parse the JSON response
                        String json = response.body().string();
                        JSONObject jsonObject = new JSONObject(json);

                        if (jsonObject.getString("status").equals("success")) {
                            callback.onSuccess(jsonObject.getString("message"));
                        } else {
                            // Handle the "Order is in progress" case or other error messages
                            callback.onError(jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        callback.onError("Parsing error: " + e.getMessage());
                    }
                } else {
                    callback.onError( response.message()); // Handle non-200 responses
                }
            }
        });
    }
    public static void updateOrderStatus(int orderId, String status, ApiCallback<String> callback) {

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("order_id", orderId);
            jsonBody.put("status", status);
        } catch (JSONException e) {
            callback.onError("Failed to construct JSON body: " + e.getMessage());
            return;
        }

        // Create the request
        RequestBody body = RequestBody.create(
                jsonBody.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(ApiEndpoint.UPDATE_ORDER_STATUS)
                .post(body)
                .build();

        // Send the request using OkHttpClient
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // Handle failure
                callback.onError("Failed to connect to server: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // Handle the server's response
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);

                        if (jsonResponse.getBoolean("success")) {
                            callback.onSuccess(jsonResponse.getString("message"));
                        } else {
                            callback.onError(jsonResponse.getString("error"));
                        }
                    } catch (JSONException e) {
                        callback.onError("Failed to parse server response: " + e.getMessage());
                    }
                } else {
                    callback.onError("Server responded with error: " + response.message());
                }
            }
        });
    }
}