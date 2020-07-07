package com.example.pharma.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pharma.R;
import com.example.pharma.forget_pass;
import com.example.pharma.getAllDataFromAPI;
import com.example.pharma.module.PharmaciesModule;
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
import java.util.HashMap;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class login extends AppCompatActivity {

    private CircularProgressButton cirLoginButton;
    TextView textView_forgetPass;
    TextInputEditText username, password;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        //////////////////////////////////////
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.whiteTextColor));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        //////////////////////////////////
        setContentView(R.layout.login);
        component();

        textView_forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this, forget_pass.class));
                finish();
            }
        });

        cirLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkUserAPI api = new checkUserAPI();
                api.mail = username.getText().toString();
                api.pass = password.getText().toString();

                api.execute();
            }
        });


    }

    private void component() {
        textView_forgetPass = findViewById(R.id.forget_text);
        cirLoginButton = findViewById(R.id.cirRegisterButton);
        username = findViewById(R.id.email);
        password = findViewById(R.id.password);

    }


    public void onLoginClick(View View) {
        startActivity(new Intent(this, register.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
        cirLoginButton.revertAnimation();
        finish();

    }

    public class checkUserAPI extends AsyncTask<Void, Void, Void> {

        String LOG_TAG = "checkUserAPI";
        public int code;
        public String mail, pass;

        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String, String> parameters = new HashMap<>();

            parameters.put("mail", mail);
            parameters.put("pass", pass);


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
            String url = "http://medprices.c1.biz/api/pharmSignin.php";
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

                    JSONArray resultJSON = main.getJSONArray("success");
                    code = 2;
                } catch (Exception exx) {

                    JSONArray resultJSON = main.getJSONArray("error");
                    code = 1;


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
                PharmaciesModule rm = new PharmaciesModule();
                getAllDataFromAPI.currentUser = rm.getPharmacyByUsername_pw(mail,pass);

                SharedPreferences pref = getApplicationContext().getSharedPreferences("authentication", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();

                editor.putString("username", getAllDataFromAPI.currentUser.getPharmMail()); // Storing string
                editor.putString("password", getAllDataFromAPI.currentUser.getPharmPass()); // Storing string

                editor.commit(); // commit changes

                cirLoginButton.animate();
                Intent intent = new Intent(mContext, medical_page.class);
                login.this.startActivity(intent);
            } else {
                Toast.makeText(mContext,"something went wrong",Toast.LENGTH_SHORT).show();

            }

        }

    }

}
