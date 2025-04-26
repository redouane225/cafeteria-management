package com.example.cafeteriamanagement.Network;

import okhttp3.OkHttpClient;
import java.util.concurrent.TimeUnit;

public class ApiClient {

    private static OkHttpClient client;

    // Singleton instance of OkHttpClient
    public static OkHttpClient getClient() {
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS) // Set connection timeout
                    .readTimeout(30, TimeUnit.SECONDS) // Set read timeout
                    .writeTimeout(30, TimeUnit.SECONDS) // Set write timeout
                    .build();
        }
        return client;
    }
}