package com.example.pharma.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharma.Adapter.recycler_adapter;
import com.example.pharma.R;
import com.example.pharma.getAllDataFromAPI;
import com.example.pharma.module.PharmaciesModule;

import java.util.ArrayList;

public class pharmacy_details extends AppCompatActivity implements SearchView.OnQueryTextListener {
    Toolbar toolbar;
    SearchView search;
    RecyclerView recyclerView;
    recycler_adapter adapter;
    Context context = pharmacy_details.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharmacy_details);
        component();

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);

        String searchText = getIntent().getStringExtra("searchText");
        String city = getIntent().getStringExtra("city");
        String gov = getIntent().getStringExtra("gov");

        ArrayList<PharmaciesModule> pharmacies = getPharmacies(searchText);

        ArrayList pharmaciesToShow = new ArrayList();

        try {
            for (int i = 0; i < pharmacies.size(); i++) {
                if ((pharmacies.get(i).getCityId() + "").equals(getCityId(city)) && (pharmacies.get(i).getGovId() + "").equals(getGovId(gov))) {
                    pharmaciesToShow.add(pharmacies.get(i));
                }
            }
        } catch (Exception e) {

            pharmaciesToShow.add(new PharmaciesModule());
        }
        adapter = new recycler_adapter(context, pharmaciesToShow);
        recyclerView.setAdapter(adapter);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Online Pharmacy");


    }

    private String getGovId(String gov) {

        for (int i = 0; i < getAllDataFromAPI.gov_list.size(); i++) {
            if (getAllDataFromAPI.gov_list.get(i).getGovernorateName().equals(gov)) {
                gov = getAllDataFromAPI.gov_list.get(i).getId() + "";
            }
        }

        return gov;
    }

    private String getCityId(String city) {


        for (int i = 0; i < getAllDataFromAPI.cites_list.size(); i++) {
            if (getAllDataFromAPI.cites_list.get(i).getCityName().equals(city)) {
                city = getAllDataFromAPI.cites_list.get(i).getId() + "";
            }
        }
        return city;
    }

    private ArrayList<PharmaciesModule> getPharmacies(String searchText) {
        try {
            ArrayList<PharmaciesModule> pm = null;
            for (int i = 0; i < getAllDataFromAPI.drugs_list.size(); i++) {
                if (getAllDataFromAPI.drugs_list.get(i).getName().equals(searchText)) {
                    pm = getAllDataFromAPI.drugs_list.get(i).getPharmaciesList();
                }
            }
            return pm;
        } catch (Exception e) {
            return null;
        }
    }

    private void component() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.search);
        search = (SearchView) item.getActionView();
        search.setOnQueryTextListener(this);
        return true;

    }

    @Override
    public boolean onQueryTextSubmit(String query) {


        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        adapter.getFilter().filter(newText);
        return false;
    }
}
