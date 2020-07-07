package com.example.pharma.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharma.Adapter.medical_page_adapter;
import com.example.pharma.ProfileActivity;
import com.example.pharma.R;
import com.example.pharma.getAllDataFromAPI;
import com.example.pharma.module.DrugsModule;
import com.example.pharma.module.PharmaciesModule;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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


public class medical_page extends AppCompatActivity implements SearchView.OnQueryTextListener {

    Toolbar toolbar;
    SearchView search;
    FloatingActionButton addNew, backHome, goToProfile, showAllFloatButtons;
    static RecyclerView recyclerView;
    static medical_page_adapter adapter;
    static Context context;
    public static DrugsModule dm = new DrugsModule();
    static String pharmID;

    AlertDialog.Builder builder;
    View dView;
    public static AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //////////////////////////////////////
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.toolbar));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        //////////////////////////////////


        setContentView(R.layout.medical_page);
        context = medical_page.this;

        component();

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewMedicine();
            }
        });
        goToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });
        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        showAllFloatButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addNew.getVisibility() == View.GONE) {
                    addNew.show();
                    backHome.show();
                    goToProfile.show();
                    showAllFloatButtons.setImageResource(R.drawable.ic_baseline_arrow_downward_24);
                } else {
                    addNew.hide();
                    backHome.hide();
                    goToProfile.hide();
                    showAllFloatButtons.setImageResource(R.drawable.ic_baseline_arrow_upward_24);
                }

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        adapter = new medical_page_adapter(getAllDataFromAPI.drugs_list, context);
        recyclerView.setAdapter(adapter);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Pharmacy");


    }

    private void component() {
        toolbar = findViewById(R.id.toolbar2);
        recyclerView = findViewById(R.id.recycler2);
        addNew = findViewById(R.id.add_medicine);
        backHome = findViewById(R.id.backHome);
        goToProfile = findViewById(R.id.Profile_page);
        showAllFloatButtons = findViewById(R.id.showAllFloatButtons);

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

    private void addNewMedicine() {
        builder = new AlertDialog.Builder(context);
        dView = getLayoutInflater().inflate(R.layout.dailog_layout, null);
        final TextInputEditText nameEdit = dView.findViewById(R.id.ed_medName);
        final TextInputEditText salaryEdit = dView.findViewById(R.id.ed_medSalary);
        Button submit = dView.findViewById(R.id.add_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nameEdit.getText().toString().isEmpty() && !salaryEdit.getText().toString().isEmpty()) {

                    dm.setName(nameEdit.getText().toString());
                    dm.setPrice(salaryEdit.getText().toString());
                    pharmID = getAllDataFromAPI.currentUser.getPharmId() + "";

                    PostDrugsDetails postDrugsDetails = new PostDrugsDetails();
                    postDrugsDetails.execute();

                } else {
                    Toast.makeText(context, "Please Complete All Data ", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setView(dView);
        dialog = builder.create();

        dialog.show();
    }

    public class PostDrugsDetails extends AsyncTask<Void, Void, Void> {

        String LOG_TAG = "PostDrugsDetails";
        public int code;

        @Override
        protected Void doInBackground(Void... params) {
            DrugsModule drugModule = medical_page.dm;
            HashMap<String, String> parameters = new HashMap<>();

            System.out.println(getAllDataFromAPI.currentUser.getPharmMail());
            System.out.println(getAllDataFromAPI.currentUser.getPharmPass());

            parameters.put("mailCred", getAllDataFromAPI.currentUser.getPharmMail());
            parameters.put("passCred", getAllDataFromAPI.currentUser.getPharmPass());
            parameters.put("pharmID", pharmID);
            parameters.put("medName", drugModule.getName());
            parameters.put("medPrice", drugModule.getPrice());


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
            String url = "http://medprices.c1.biz/api/addDrug.php";
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

                ArrayList<PharmaciesModule> pharmaciesModules = new ArrayList<>();
                pharmaciesModules.add(getAllDataFromAPI.currentUser);

                DrugsModule newDrug = new DrugsModule();
                newDrug.setId(newDrug.getNextDrugId());
                newDrug.setName(dm.getName());
                newDrug.setPrice(dm.getPrice());
                newDrug.setPharmaciesList(pharmaciesModules);


                getAllDataFromAPI.drugs_list.add(newDrug);

                SweetAlertDialog confirmDialog = new SweetAlertDialog(medical_page.this, SweetAlertDialog.SUCCESS_TYPE);
                confirmDialog.setTitleText("Done add drug");
                confirmDialog.show();
            } else {
                SweetAlertDialog errorDialog = new SweetAlertDialog(medical_page.this, SweetAlertDialog.ERROR_TYPE);
                errorDialog.setTitleText("Oops...");
                errorDialog.setContentText("هناك خطأ ما حاول مره اخرى");
                errorDialog.show();
            }
            adapter.notifyDataSetChanged();
            dialog.dismiss();
        }
    }

    public static class MakeDrugAvailable extends AsyncTask<Void, Void, Void> {

        String LOG_TAG = "Make Available";
        public int code;

        @Override
        protected Void doInBackground(Void... params) {
            DrugsModule drugModule = medical_page.dm;
            HashMap<String, String> parameters = new HashMap<>();

            parameters.put("mail", getAllDataFromAPI.currentUser.getPharmMail());
            parameters.put("pass", getAllDataFromAPI.currentUser.getPharmPass());
            System.out.println("mail="+getAllDataFromAPI.currentUser.getPharmMail());
            System.out.println("pass="+getAllDataFromAPI.currentUser.getPharmPass());

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
            String url = "http://medprices.c1.biz/api/makeAvail.php?id=" + drugModule.getId();
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
                for (int i = 0; i < getAllDataFromAPI.drugs_list.size(); i++) {
                    if (dm.getId() == getAllDataFromAPI.drugs_list.get(i).getId()) {
                        getAllDataFromAPI.drugs_list.get(i).getPharmaciesList().add(getAllDataFromAPI.currentUser);
                    }
                }
                SweetAlertDialog confirmDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
                confirmDialog.setTitleText("Done");
                confirmDialog.show();
            } else {
                SweetAlertDialog errorDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
                errorDialog.setTitleText("Oops...");
                errorDialog.setContentText("هناك خطأ ما حاول مره اخرى");
                errorDialog.show();
            }
        }

    }

    public static class MakeDrugUnAvailable extends AsyncTask<Void, Void, Void> {

        String LOG_TAG = "MakeDrugUnAvailable";
        public int code;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                DrugsModule drugModule = medical_page.dm;
                HashMap<String, String> parameters = new HashMap<>();

                parameters.put("mail", getAllDataFromAPI.currentUser.getPharmMail());
                parameters.put("pass", getAllDataFromAPI.currentUser.getPharmPass());


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
                String url = "http://medprices.c1.biz/api/makeUnAvail.php?id=" + drugModule.getId();
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

            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (code == 2) {
                if (code == 2) {
                    for (int i = 0; i < getAllDataFromAPI.drugs_list.size(); i++) {
                        if (dm.getId() == getAllDataFromAPI.drugs_list.get(i).getId()) {
                            for (int j = 0; j < getAllDataFromAPI.drugs_list.get(i).
                                    getPharmaciesList().size(); j++) {

                                if (getAllDataFromAPI.drugs_list.get(i).getPharmaciesList().get(j)
                                        .getPharmMail()
                                        .equals(getAllDataFromAPI
                                                .currentUser.getPharmMail())) {

                                    getAllDataFromAPI.drugs_list.get(i).getPharmaciesList().remove(j);

                                }

                            }

                        }
                    }
                    SweetAlertDialog confirmDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
                    confirmDialog.setTitleText("Done");
                    confirmDialog.show();

                } else {
                    SweetAlertDialog errorDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
                    errorDialog.setTitleText("Oops...");
                    errorDialog.setContentText("هناك خطأ ما حاول مره اخرى");
                    errorDialog.show();
                }

            }

        }

    }
}
