package com.example.dotaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyPairGenerator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements HeroAdapter.OnHeroListener {
    String TAG = "MAIN_ACTIVITY";
    private ArrayList<Hero> heroes;
    private RecyclerView recyclerView;
    private HeroAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText searchEdit = (EditText) findViewById(R.id.editSearch);
        recyclerView = (RecyclerView) findViewById(R.id.heroRecyclerView);

        //get location permission
        int locationPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (locationPermissionCheck != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        //Create queue to get data from API
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://api.opendota.com/api/heroes";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray root = new JSONArray(response);
                            //Create a list of heroes and fill it with data from the JSON
                            heroes = new ArrayList<Hero>();
                            for (int i = 0; i < root.length(); i++) {
                                JSONObject jsonHero = root.getJSONObject(i);

                                int id = jsonHero.getInt("id");
                                String name = jsonHero.getString("name");
                                String localized_name = jsonHero.getString("localized_name");
                                String primary_attr = jsonHero.getString("primary_attr");
                                String attack_type = jsonHero.getString("attack_type");
                                ArrayList<String> rolesList = new ArrayList<String>();
                                int legs = jsonHero.getInt("legs");

                                //Each hero has a list of roles. The below code populates the list
                                JSONArray jsonArray = (JSONArray) jsonHero.getJSONArray("roles");
                                if (jsonArray != null) {
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        rolesList.add(jsonArray.get(j).toString());
                                    }
                                    //create Hero object and add it to the list
                                    if (i == 0){
                                        Hero throwaway = new Hero();
                                        throwaway.resetAtomic();
                                    }
                                    heroes.add(new Hero(id, name, localized_name, primary_attr, attack_type, rolesList, legs));
                                }
                            }
                            setAdapter();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "Didn't work");
            }
        });
        //Snackbar on launch of app
        Snackbar.make(recyclerView, "Choose a hero!", Snackbar.LENGTH_LONG).show();
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    //Create the RecyclerView and add the list
    private void setAdapter() {
        adapter = new HeroAdapter(heroes, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);

        recyclerView.addItemDecoration(itemDecor);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    //Filter the list when the user uses the search bar
    private void filter(String text) {
        ArrayList<Hero> filteredList = new ArrayList<>();

        for (Hero hero : heroes) {
            if (hero.getLocalized_name().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(hero);
            }
        }
        adapter.filterList(filteredList);
    }

    @Override
    public void onHeroClick(int currentId) {
        Intent intent = new Intent(this, HeroActivity.class);
        intent.putExtra("hero", heroes.get(currentId - 1));
        startActivity(intent);
    }
}