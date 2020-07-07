package com.example.pharma.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pharma.R;
import com.example.pharma.getAllDataFromAPI;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btn1;
    AutoCompleteTextView auto;
    String[] drugs;
    Spinner cities, govs;
    String[] govsSpinnerArray;
    String[] citiesSpinnerArray;
    ArrayAdapter citiesAdapter;
    TextView goToLogin;
    String currentUserMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //////////////////////////////////////
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.whiteTextColor));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        //////////////////////////////////


        setContentView(R.layout.activity_main);
        cities = findViewById(R.id.spinner_2);
        govs = findViewById(R.id.spinner_1);
        goToLogin = findViewById(R.id.go_to_login);


        citiesSpinnerArray = new String[getAllDataFromAPI.cites_list.size()];

        for (int i = 0; i < getAllDataFromAPI.cites_list.size(); i++) {
            citiesSpinnerArray[i] = (getAllDataFromAPI.cites_list.get(i).getCityName());
        }

        setCitiesSpinner(citiesSpinnerArray);
        govsSpinnerArray = new String[getAllDataFromAPI.gov_list.size()];
        for (int i = 0; i < getAllDataFromAPI.gov_list.size(); i++) {
            govsSpinnerArray[i] = (getAllDataFromAPI.gov_list.get(i).getGovernorateName());
        }

        ArrayAdapter<String> govsAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, govsSpinnerArray);

        govsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        govs.setAdapter(govsAdapter);
        govs.setSelection(0);

        govs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String currentGov = govsSpinnerArray[position];
                int currentGovId = 0;

                for (int i = 0; i < getAllDataFromAPI.gov_list.size(); i++) {
                    if (currentGov.equals(getAllDataFromAPI.gov_list.get(i).getGovernorateName())) {
                        currentGovId = getAllDataFromAPI.gov_list.get(i).getId();
                    }
                }

                ArrayList newCities = new ArrayList();

                for (int i = 0; i < getAllDataFromAPI.cites_list.size(); i++) {
                    if (currentGovId == getAllDataFromAPI.cites_list.get(i).getGov_id()) {
                        newCities.add(getAllDataFromAPI.cites_list.get(i).getCityName());
                    }
                }

                setCitiesSpinner(newCities);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        component();
        auto_text();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pharmacies = new Intent(MainActivity.this, pharmacy_details.class);
                pharmacies.putExtra("searchText", auto.getText().toString());
                pharmacies.putExtra("city", cities.getSelectedItem().toString());
                pharmacies.putExtra("gov", govs.getSelectedItem().toString());
                startActivity(pharmacies);
            }
        });


        SharedPreferences pref = getApplicationContext().getSharedPreferences("authentication", 0); // 0 - for private mode
        currentUserMail = pref.getString("username", "null");
        String currentUserPass = pref.getString("password", "null");
        for (int i = 0; i < getAllDataFromAPI.pharmacies_list.size(); i++) {
            if (getAllDataFromAPI.pharmacies_list.get(i).getPharmMail().equals(currentUserMail)) {
                getAllDataFromAPI.currentUser = getAllDataFromAPI.pharmacies_list.get(i);
                getAllDataFromAPI.currentUser.setPharmPass(currentUserPass);
                goToLogin.setText("قائمه الادويه الخاصه بك");
            }
        }

    }

    private void setCitiesSpinner(String[] citiesSpinnerArray) {
        citiesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, citiesSpinnerArray);
        citiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cities.setAdapter(citiesAdapter);
        cities.setSelection(0);
    }

    private void setCitiesSpinner(ArrayList citiesSpinnerArray) {
        citiesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, citiesSpinnerArray);
        citiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cities.setAdapter(citiesAdapter);
        cities.setSelection(0);
    }


    public void Go_to_login(View View) {
        if (currentUserMail.equals("null") ||currentUserMail.equals("") ) {
            startActivity(new Intent(MainActivity.this, login.class));
        } else {
            startActivity(new Intent(MainActivity.this, medical_page.class));

        }
    }

    private void component() {

        btn1 = findViewById(R.id.btn1);
        auto = findViewById(R.id.auto_text);
    }

    private void auto_text() {

        drugs = new String[getAllDataFromAPI.drugs_list.size()];
        for (int i = 0; i < getAllDataFromAPI.drugs_list.size(); i++) {
            drugs[i] = getAllDataFromAPI.drugs_list.get(i).getName();
        }

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_dropdown_item_1line, drugs);

        auto.setAdapter(adapter);

    }




}
