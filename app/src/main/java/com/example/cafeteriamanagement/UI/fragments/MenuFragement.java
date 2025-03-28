package com.example.cafeteriamanagement.UI.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cafeteriamanagement.Adapter.MenuAdapter;
import com.example.cafeteriamanagement.Network.ApiService;
import com.example.cafeteriamanagement.Network.RetrofitClient;
import com.example.cafeteriamanagement.R;
import com.example.cafeteriamanagement.model.Menu_item;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuFragment extends Fragment {
    private RecyclerView recyclerView;
    private MenuAdapter adapter;
    private List<Menu_item> menuItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        loadMenuItems();  // Charger les données depuis le serveur

        return view;
    }

    private void loadMenuItems() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Menu_item>> call = apiService.getMenuItems("beverages"); // Filtrer par catégorie

        call.enqueue(new Callback<List<Menu_item>>() {
            @Override
            public void onResponse(Call<List<Menu_item>> call, Response<List<Menu_item>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    menuItems = response.body();
                    adapter = new MenuAdapter(getContext(), menuItems);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Erreur de chargement", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Menu_item>> call, Throwable t) {
                Log.e("API Error", t.getMessage());
                Toast.makeText(getContext(), "Échec de connexion au serveur", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
