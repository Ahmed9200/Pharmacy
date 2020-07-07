package com.example.pharma;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.pharma.module.CitesModule;
import com.example.pharma.module.DrugsModule;
import com.example.pharma.module.GovModule;
import com.example.pharma.module.PharmaciesModule;
import com.github.florent37.expansionpanel.ExpansionLayout;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProfileActivity extends AppCompatActivity {


    public TextView editData, txt, pharmName_txt, pharmNearTo_txt, govs_txt, cities_txt,
            pharmAddress_txt, pharmEmail_txt, pharmPhone_txt;

    public EditText pharmName_et, pharmNearTo_et, pharmAddress_et, pharmPhone_et;

    public Spinner cities, govs;
    ExpansionLayout layout;
    LinearLayout linearLayout;
    PharmaciesModule userData;
    AlertDialog.Builder builder;
    View dView;
    static Context mContext;
    public static AlertDialog dialog;
    Button update_btn, changePw;
    String[] govsSpinnerArray;
    String[] citiesSpinnerArray;
    ArrayAdapter citiesAdapter;
    TextInputEditText changePw_et, oldPw_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userData = getAllDataFromAPI.currentUser;
        //////////////////////////////////////
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.whiteTextColor));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        //////////////////////////////////

        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        mContext = ProfileActivity.this;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");

        // call objects
        changePw = findViewById(R.id.Change_pass);
        changePw_et = findViewById(R.id.new_change_password);
        oldPw_et = findViewById(R.id.old_password);
        update_btn = findViewById(R.id.update_btn);
        pharmAddress_txt = findViewById(R.id.pharmAddress_txt);
        govs_txt = findViewById(R.id.govs_txt);
        cities_txt = findViewById(R.id.cities_txt);
        pharmAddress_et = findViewById(R.id.pharmAddress_et);
        cities = findViewById(R.id.pharmCity_et);
        pharmEmail_txt = findViewById(R.id.pharmEmail_txt);
        govs = findViewById(R.id.pharmGov_et);
        pharmName_txt = findViewById(R.id.pharmName_txt);
        pharmName_et = findViewById(R.id.pharmName_et);
        pharmPhone_txt = findViewById(R.id.pharmPhone_txt);
        pharmPhone_et = findViewById(R.id.pharmPhone_et);
        pharmNearTo_txt = findViewById(R.id.pharmNearTo_txt);
        pharmNearTo_et = findViewById(R.id.pharmNearTo_et);

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

        // set all data
        pharmEmail_txt.setText(userData.getPharmMail());
        pharmAddress_txt.setText(userData.getPharmAddress());
        govs_txt.setText(userData.getGovName());
        cities_txt.setText(userData.getCityName());
        pharmName_txt.setText(userData.getPharmName());
        pharmPhone_txt.setText(userData.getPharmPhone());
        pharmNearTo_txt.setText(userData.getPharmNear());
        govs_txt.setText(userData.getGovName());
        cities_txt.setText(userData.getCityName());

        editData = findViewById(R.id.editPharmData_tv);

        editData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (update_btn.getVisibility() == View.GONE) {

                    pharmAddress_txt.setVisibility(View.GONE);
                    cities_txt.setVisibility(View.GONE);
                    govs_txt.setVisibility(View.GONE);
                    pharmName_txt.setVisibility(View.GONE);
                    pharmPhone_txt.setVisibility(View.GONE);
                    pharmNearTo_txt.setVisibility(View.GONE);
                    cities_txt.setVisibility(View.GONE);
                    govs_txt.setVisibility(View.GONE);

                    pharmAddress_et.setText(userData.getPharmAddress());
                    pharmName_et.setText(userData.getPharmName());
                    pharmPhone_et.setText(userData.getPharmPhone());
                    pharmNearTo_et.setText(userData.getPharmNear());

                    pharmAddress_et.setVisibility(View.VISIBLE);
                    cities.setVisibility(View.VISIBLE);
                    govs.setVisibility(View.VISIBLE);
                    pharmName_et.setVisibility(View.VISIBLE);
                    pharmPhone_et.setVisibility(View.VISIBLE);
                    pharmNearTo_et.setVisibility(View.VISIBLE);
                    govs.setVisibility(View.VISIBLE);
                    cities.setVisibility(View.VISIBLE);

                    update_btn.setVisibility(View.VISIBLE);

                } else {

                    pharmAddress_txt.setVisibility(View.VISIBLE);
                    cities_txt.setVisibility(View.VISIBLE);
                    govs_txt.setVisibility(View.VISIBLE);
                    pharmName_txt.setVisibility(View.VISIBLE);
                    pharmPhone_txt.setVisibility(View.VISIBLE);
                    pharmNearTo_txt.setVisibility(View.VISIBLE);

                    pharmAddress_et.setVisibility(View.GONE);
                    cities.setVisibility(View.GONE);
                    govs.setVisibility(View.GONE);
                    pharmName_et.setVisibility(View.GONE);
                    pharmPhone_et.setVisibility(View.GONE);
                    pharmNearTo_et.setVisibility(View.GONE);

                    update_btn.setVisibility(View.GONE);

                }
            }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllDataFromAPI.currentUser.setPharmPhone(pharmPhone_et.getText().toString());
                getAllDataFromAPI.currentUser.setPharmNear(pharmNearTo_et.getText().toString());
                getAllDataFromAPI.currentUser.setPharmName(pharmName_et.getText().toString());
                getAllDataFromAPI.currentUser.setPharmAddress(pharmAddress_et.getText().toString());
                getAllDataFromAPI.currentUser.setGovId(new GovModule().getIdFromName(govs.getSelectedItem().toString()));
                getAllDataFromAPI.currentUser.setCityId(new CitesModule().getIdFromName(cities.getSelectedItem().toString()));

                UpdatePharmaciesDataAPI api = new UpdatePharmaciesDataAPI();
                api.execute();
            }
        });


        changePw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (oldPw_et.getText().toString().equals(getAllDataFromAPI.currentUser.getPharmPass())) {
                    getAllDataFromAPI.currentUser.setPharmPhone(pharmPhone_et.getText().toString());
                    getAllDataFromAPI.currentUser.setPharmNear(pharmNearTo_et.getText().toString());
                    getAllDataFromAPI.currentUser.setPharmName(pharmName_et.getText().toString());
                    getAllDataFromAPI.currentUser.setPharmAddress(pharmAddress_et.getText().toString());
                    getAllDataFromAPI.currentUser.setPharmPass(changePw_et.getText().toString());
                    getAllDataFromAPI.currentUser.setGovId(new GovModule().getIdFromName(govs.getSelectedItem().toString()));
                    getAllDataFromAPI.currentUser.setCityId(new CitesModule().getIdFromName(cities.getSelectedItem().toString()));

                    UpdatePharmaciesDataAPI api = new UpdatePharmaciesDataAPI();
                    api.execute();
                } else {
                    new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Old password not correct")
                            .show();
                }
            }
        });


        // set all drugs

        layout = findViewById(R.id.expansionLayout);
        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        Animation anim;
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 20);
        params.gravity = Gravity.CENTER;

        ArrayList<DrugsModule> list = userData.getDrugList();
        for (int i = 0; i < list.size(); i++) {
            txt = new TextView(this);
            txt.setText(list.get(i).getName());
            txt.setTextColor(getResources().getColor(R.color.drugList));
            txt.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            txt.setTextSize(25);
            txt.setLayoutParams(params);
            txt.startAnimation(anim);
            linearLayout.addView(txt);

        }
        layout.addView(linearLayout);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.Change_pass:
                Toast.makeText(ProfileActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();

                builder = new AlertDialog.Builder(mContext);
                dView = getLayoutInflater().inflate(R.layout.change_pw_dialog, null);
                builder.setView(dView);
                dialog = builder.create();
                dialog.show();
                break;
            case R.id.Loge_out:
                clearSharedPreferences();
                break;
            case R.id.delete:
                DeletePharmacyAccAPI api = new DeletePharmacyAccAPI();
                api.execute();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    public void clearSharedPreferences() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("authentication", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("username", "null"); // Storing string
        editor.putString("password", "null"); // Storing string

        editor.commit(); // commit changes

        Intent intent = new Intent(mContext, splashScreen.class);
        ProfileActivity.this.startActivity(intent);
        ProfileActivity.this.finish();
    }

    public class DeletePharmacyAccAPI extends AsyncTask<Void, Void, Void> {

        String LOG_TAG = "DeletePharmacyAccAPI";
        public int code;

        @Override
        protected Void doInBackground(Void... params) {

            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("id", getAllDataFromAPI.currentUser.getPharmId() + "");


            StringBuilder sbParams = new StringBuilder();
            int i = 0;
            for (String key : parameters.keySet()) {
                try {
                    if (i != 0) {
                        sbParams.append("&");
                    }
                    sbParams.append(key).append("=")
                            .append(URLEncoder.encode(parameters.get(key), "UTF-8"));

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                i++;
            }
            String url = "http://medprices.c1.biz/api/deletePharmacy.php";
            HttpURLConnection conn = null;
            try {
                URL urlObj = new URL(url);
                conn = (HttpURLConnection) urlObj.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept-Charset", "UTF-8");

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);

                conn.connect();

                String paramsString = sbParams.toString();

                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(paramsString);
                wr.flush();
                wr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                Log.d("Server API", "result from server: " + result.toString());

                JSONObject main = new JSONObject(result.toString());

                try {

                    JSONArray resultJSON = main.getJSONArray("error");
                    code = 1;
                } catch (Exception exx) {

                    JSONArray resultJSON = main.getJSONArray("success");
                    code = 2;


                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (code == 2) {

                new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Done")
                        .setContentText("Account Deleted")
                        .show();
                clearSharedPreferences();
            } else {
                new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Something went wrong!")
                        .show();
            }

        }

    }

    public class UpdatePharmaciesDataAPI extends AsyncTask<Void, Void, Void> {

        String LOG_TAG = "UpdatePharmaciesDataAPI";
        public int code;

        @Override
        protected Void doInBackground(Void... params) {
            PharmaciesModule pharmaciesModule = getAllDataFromAPI.currentUser;
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("newPharmName", pharmaciesModule.getPharmName());
            parameters.put("newPharmPhone", pharmaciesModule.getPharmPhone());
            parameters.put("newPharmAddress", pharmaciesModule.getPharmAddress());
            parameters.put("newPharmNear", pharmaciesModule.getPharmNear());
            parameters.put("newPharmMail", pharmaciesModule.getPharmMail());
            parameters.put("newPharmPass", pharmaciesModule.getPharmPass());
            parameters.put("newPharmGov", pharmaciesModule.getGovId() + "");
            parameters.put("newPharmCity", pharmaciesModule.getCityId() + "");
            parameters.put("oldId", pharmaciesModule.getPharmId() + "");


            StringBuilder sbParams = new StringBuilder();
            int i = 0;
            for (String key : parameters.keySet()) {
                try {
                    if (i != 0) {
                        sbParams.append("&");
                    }
                    sbParams.append(key).append("=")
                            .append(URLEncoder.encode(parameters.get(key), "UTF-8"));

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                i++;
            }
            String url = "http://medprices.c1.biz/api/updatePharmacy.php";
            HttpURLConnection conn = null;
            try {
                URL urlObj = new URL(url);
                conn = (HttpURLConnection) urlObj.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept-Charset", "UTF-8");

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);

                conn.connect();

                String paramsString = sbParams.toString();

                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(paramsString);
                wr.flush();
                wr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                Log.d("Server API", "result from server: " + result.toString());

                JSONObject main = new JSONObject(result.toString());

                try {

                    JSONArray resultJSON = main.getJSONArray("error");
                    code = 1;
                } catch (Exception exx) {

                    JSONArray resultJSON = main.getJSONArray("success");
                    code = 2;


                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (code == 2) {

                SharedPreferences pref = getApplicationContext().getSharedPreferences("authentication", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();

                editor.putString("username", getAllDataFromAPI.currentUser.getPharmMail()); // Storing string
                editor.putString("password", getAllDataFromAPI.currentUser.getPharmPass()); // Storing string

                editor.commit(); // commit changes

                new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Done")
                        .setContentText("Data Updated Correctly")
                        .show();

                ProfileActivity.this.startActivity(new Intent(mContext, ProfileActivity.class));
                ProfileActivity.this.finish();
            } else {
                new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Something went wrong!")
                        .show();
            }

        }

    }


}
