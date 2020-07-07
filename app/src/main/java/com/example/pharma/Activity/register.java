package com.example.pharma.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pharma.R;
import com.example.pharma.getAllDataFromAPI;
import com.example.pharma.module.PharmaciesModule;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class register extends AppCompatActivity {

    public static CircularProgressButton cirLoginButton;
    Spinner cities, govs;
    String[] govsSpinnerArray;
    String[] citiesSpinnerArray;
    ArrayAdapter citiesAdapter;
    TextInputEditText ph_name, ph_address, ph_nearBy, ph_phone, ph_email, ph_password;
    String pharmacy_name_str, pharmacy_address_str, pharmacy_near_str, pharmacy_email_str,
            pharmacy_password_str, pharmacy_phone_str, pharmacy_gov_name, pharmacy_city_name;
    public static PharmaciesModule rm;
    public static ProgressDialog progress;
    public static Context mContext;
    TextInputLayout tf_email;
    static SweetAlertDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        mContext = getApplicationContext();

        progress = new ProgressDialog(this);
        cities = findViewById(R.id.spinner2);
        govs = findViewById(R.id.spinner1);
        ph_address = findViewById(R.id.ph_address);
        ph_email = findViewById(R.id.ph_email);
        ph_name = findViewById(R.id.ph_name);
        ph_nearBy = findViewById(R.id.ph_near);
        ph_phone = findViewById(R.id.ph_phone);
        ph_password = findViewById(R.id.ph_password);
        cirLoginButton = findViewById(R.id.cirRegisterButton);
        tf_email = findViewById(R.id.tf_email);


        pDialog = new SweetAlertDialog(register.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(true);


        ph_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                for (int i = 0; i < getAllDataFromAPI.pharmacies_list.size(); i++) {
                    if (ph_email.getText().toString().equals(getAllDataFromAPI.pharmacies_list.get(i).getPharmMail())) {
                        new SweetAlertDialog(register.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("هذا البريد مستخدم من قبل")
                                .show();
                    }
                }
            }
        });
        /////////////////////////////////

        citiesSpinnerArray = new String[getAllDataFromAPI.cites_list.size()];

        for (int i = 0; i < getAllDataFromAPI.cites_list.size(); i++) {
            citiesSpinnerArray[i] = (getAllDataFromAPI.cites_list.get(i).getCityName());
        }

        setCitiesSpinner(citiesSpinnerArray);
        govsSpinnerArray = new String[getAllDataFromAPI.gov_list.size()];
        for (int i = 0; i < getAllDataFromAPI.gov_list.size(); i++) {
            govsSpinnerArray[i] = (getAllDataFromAPI.gov_list.get(i).getGovernorateName());
        }

        try {

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

        } catch (Exception e) {
        }

        /////////////////////////////////
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.register_bk_color));
        ////////////////////////////////


//        cirLoginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                cirLoginButton.startAnimation();
//                startActivity(new Intent(register.this, medical_page.class));
//                finish();
//            }
//        });
    }

    private void setCitiesSpinner(String[] citiesSpinnerArray) {
        try {
            citiesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, citiesSpinnerArray);
            citiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cities.setAdapter(citiesAdapter);
            cities.setSelection(0);

        } catch (Exception e) {

        }
    }

    private void setCitiesSpinner(ArrayList citiesSpinnerArray) {
        try {
            citiesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, citiesSpinnerArray);
            citiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cities.setAdapter(citiesAdapter);
            cities.setSelection(0);

        } catch (Exception e) {
        }
    }

    private void getDataFromEditText() {
        pharmacy_name_str = ph_name.getText().toString();
        pharmacy_phone_str = ph_phone.getText().toString();
        pharmacy_near_str = ph_nearBy.getText().toString();
        pharmacy_address_str = ph_address.getText().toString();
        pharmacy_email_str = ph_email.getText().toString();
        pharmacy_password_str = ph_password.getText().toString();
        pharmacy_city_name = cities.getSelectedItem().toString();
        pharmacy_gov_name = govs.getSelectedItem().toString();
    }

    private PharmaciesModule setDataToRegisterObject() {
        PharmaciesModule module = new PharmaciesModule();
        module.setPharmAddress(pharmacy_address_str);
        module.setPharmMail(pharmacy_email_str);
        module.setPharmName(pharmacy_name_str);
        module.setPharmNear(pharmacy_near_str);
        module.setPharmPhone(pharmacy_phone_str);
        module.setPharmPass(pharmacy_password_str);
        module.setCityId(convertCityToCityId());
        module.setGovId(convertGovToGovId());
        return module;
    }

    private boolean checkData() {
        return ((pharmacy_gov_name.equals("")) ||
                (pharmacy_city_name.equals("")) ||
                (pharmacy_password_str.equals("")) ||
                (pharmacy_near_str.equals("")) ||
                (pharmacy_phone_str.equals("")) ||
                (pharmacy_name_str.equals("")) ||
                (pharmacy_email_str.equals("")) ||
                (pharmacy_address_str.equals("")));
    }

    private int convertGovToGovId() {
        int govId = 0;

        for (int i = 0; i < getAllDataFromAPI.gov_list.size(); i++) {
            if (pharmacy_gov_name.equals(getAllDataFromAPI.gov_list.get(i).getGovernorateName())) {
                govId = getAllDataFromAPI.gov_list.get(i).getId();
            }
        }
        return govId;
    }

    private int convertCityToCityId() {
        int cityId = 0;
        for (int i = 0; i < getAllDataFromAPI.cites_list.size(); i++) {
            if (pharmacy_city_name.equals(getAllDataFromAPI.cites_list.get(i).getCityName())) {
                cityId = getAllDataFromAPI.cites_list.get(i).getId();
            }
        }
        return cityId;
    }

    public void onLoginClick(View View) {
        startActivity(new Intent(this, login.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.stay);
        cirLoginButton.revertAnimation();
        finish();

    }

    public void registerAnPharmacy(View view) {
        getDataFromEditText();
        if (checkData()) {
            new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("انتبه من فضلك")
                    .setContentText("ايرجى ادخال البيانات بالكامل وبطريقه صحيحه")
                    .show();
        } else {
            rm = setDataToRegisterObject();

            pDialog.show();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            PostPharmaciesDetails postPharmaciesDetails = new PostPharmaciesDetails();
            postPharmaciesDetails.execute();


        }
    }

    public class PostPharmaciesDetails extends AsyncTask<Void, Void, Void> {

        String LOG_TAG = "PostPharmaciesDetails";
        public int code;

        @Override
        protected Void doInBackground(Void... params) {
            PharmaciesModule pharmaciesModule = register.rm;
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("pharmName", pharmaciesModule.getPharmName());
            parameters.put("pharmPhone", pharmaciesModule.getPharmPhone());
            parameters.put("pharmAddress", pharmaciesModule.getPharmAddress());
            parameters.put("pharmNear", pharmaciesModule.getPharmNear());
            parameters.put("pharmMail", pharmaciesModule.getPharmMail());
            parameters.put("pharmPass", pharmaciesModule.getPharmPass());
            parameters.put("pharmGov", pharmaciesModule.getGovId() + "");
            parameters.put("pharmCity", pharmaciesModule.getCityId() + "");


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
            String url = "http://medprices.c1.biz/api/addPharmacy.php";
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

//            progress.dismiss();

            pDialog.dismiss();
            System.out.println("code = " + code);
            if (code == 2) {
                getAllDataFromAPI.currentUser = rm;
                getAllDataFromAPI.pharmacies_list.add(rm);

                SharedPreferences pref = getApplicationContext().getSharedPreferences("authentication", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();

                editor.putString("username", rm.getPharmMail()); // Storing string
                editor.putString("password", rm.getPharmPass()); // Storing string

                editor.commit(); // commit changes

                cirLoginButton.animate();
                Intent intent = new Intent(mContext, medical_page.class);
                register.this.startActivity(intent);
            } else {
                new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Something went wrong!")
                        .show();
            }

        }

    }

}
