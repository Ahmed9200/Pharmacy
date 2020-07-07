package com.example.pharma;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pharma.Activity.MainActivity;
import com.example.pharma.module.DrugsModule;
import com.example.pharma.module.PharmaciesModule;

import java.util.ArrayList;

public class splashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2500;
    Animation anim;
    ImageView image_splash;

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

        setContentView(R.layout.activity_splash_screen);

        final getAllDataFromAPI.FetchGovDetails govsDataFromAPI = new getAllDataFromAPI.FetchGovDetails();
        final getAllDataFromAPI.FetchCitiesDetails citiesDataFromAPI = new getAllDataFromAPI.FetchCitiesDetails();
        final getAllDataFromAPI.FetchDrugsDetails drugsDataFromAPI = new getAllDataFromAPI.FetchDrugsDetails();
        final getAllDataFromAPI.FetchPharmaciesDetails pharmaciesDataFromAPI = new getAllDataFromAPI.FetchPharmaciesDetails();
        govsDataFromAPI.execute();
        citiesDataFromAPI.execute();
        drugsDataFromAPI.execute();
        pharmaciesDataFromAPI.execute();

        image_splash = findViewById(R.id.logo_splash);
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_up);
        image_splash.startAnimation(anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    if (drugsDataFromAPI.getStatus() == AsyncTask.Status.FINISHED &&
                            citiesDataFromAPI.getStatus() == AsyncTask.Status.FINISHED &&
                            pharmaciesDataFromAPI.getStatus() == AsyncTask.Status.FINISHED &&
                            govsDataFromAPI.getStatus() == AsyncTask.Status.FINISHED) {
                        break;
                    }
                }

                for (int i = 0; i < getAllDataFromAPI.drugs_list.size(); i++) {
                    getAllDataFromAPI.drugs_list.get(i).setPharmaciesList(
                            getPharmacies(getAllDataFromAPI.drugs_list.get(i).getName()));
                }

                Intent intent = new Intent(splashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        }, SPLASH_TIME_OUT);
    }

    private ArrayList<PharmaciesModule> getPharmacies(String searchText) {
        try {
            DrugsModule drug = new DrugsModule();
            for (int i = 0; i < getAllDataFromAPI.drugs_list.size(); i++) {
                if (getAllDataFromAPI.drugs_list.get(i).getName().equals(searchText)) {
                    drug = getAllDataFromAPI.drugs_list.get(i);
                }
            }

            String tst = drug.getAvailabilityPharmacies().replace('|', ',');
            String[] pharmaciesId = tst.split(",", 0);

            ArrayList<PharmaciesModule> pharmaciesData = new ArrayList();

            for (int i = 0; i < pharmaciesId.length; i++) {
                for (int k = 0; k < getAllDataFromAPI.pharmacies_list.size(); k++) {
                    if (Integer.parseInt(pharmaciesId[i]) == getAllDataFromAPI.pharmacies_list.get(k).getPharmId()) {
                        pharmaciesData.add(getAllDataFromAPI.pharmacies_list.get(k));
                    }
                }
            }
            return pharmaciesData;
        } catch (Exception e) {
            return null;
        }
    }

}