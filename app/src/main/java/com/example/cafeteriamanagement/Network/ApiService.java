package com.example.cafeteriamanagement.Network;

import android.util.Log;

import com.example.cafeteriamanagement.model.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiService {

    private static final String TAG = "ApiService";

    // MediaType for JSON
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    // log in user
    public static void login(String username, String password, ApiCallback<String> callback) {
        OkHttpClient client = ApiClient.getClient();

        try {
            // Create JSON body for the request
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
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        JSONArray menuItemsArray = new JSONArray(responseBody);
                        List<MenuItem> menuItems = new ArrayList<>();

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
                        }

                        callback.onSuccess(menuItems);

                    } catch (JSONException e) {
                        Log.e(TAG, "JSON parsing error", e);
                        callback.onError("Failed to parse response");
                    }
                } else {
                    callback.onError("Failed to fetch menu items: " + response.message());
                }
            }
        });
    }

    // UPDATE Menu Item
    public static void updateMenuItem(MenuItem menuItem, ApiCallback<String> callback) {
        OkHttpClient client = ApiClient.getClient();

        try {
            // Create JSON body for the request
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
                    callback.onError(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            String responseBody = response.body().string();
                            JSONObject jsonResponse = new JSONObject(responseBody);
                            String message = jsonResponse.optString("message", "Menu item updated successfully");
                            callback.onSuccess(message);
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON parsing error", e);
                            callback.onError("Failed to parse response");
                        }
                    } else {
                        callback.onError("Failed to update menu item: " + response.message());
                    }
                }
            });

        } catch (JSONException e) {
            Log.e(TAG, "Failed to create JSON body", e);
            callback.onError("Failed to create request body");
        }
    }

    // ADD Menu Item
    public static void addMenuItem(MenuItem menuItem, ApiCallback<String> callback) {
        OkHttpClient client = ApiClient.getClient();

        try {
            // Create JSON body for the request
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
                    callback.onError(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            String responseBody = response.body().string();
                            JSONObject jsonResponse = new JSONObject(responseBody);
                            String message = jsonResponse.optString("message", "Menu item added successfully");
                            callback.onSuccess(message);
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON parsing error", e);
                            callback.onError("Failed to parse response");
                        }
                    } else {
                        callback.onError("Failed to add menu item: " + response.message());
                    }
                }
            });

        } catch (JSONException e) {
            Log.e(TAG, "Failed to create JSON body", e);
            callback.onError("Failed to create request body");
        }
    }
}