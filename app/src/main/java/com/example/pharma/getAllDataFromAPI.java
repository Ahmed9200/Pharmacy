package com.example.pharma;

import android.os.AsyncTask;
import android.util.Log;

import com.example.pharma.module.CitesModule;
import com.example.pharma.module.DrugsModule;
import com.example.pharma.module.GovModule;
import com.example.pharma.module.PharmaciesModule;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class getAllDataFromAPI {

    public static ArrayList<PharmaciesModule> pharmacies_list = new ArrayList<>();
    public static ArrayList<GovModule> gov_list = new ArrayList<>();
    public static ArrayList<CitesModule> cites_list = new ArrayList<>();
    public static ArrayList<DrugsModule> drugs_list = new ArrayList<>();
    public static PharmaciesModule currentUser = new PharmaciesModule();


    public static class FetchPharmaciesDetails extends AsyncTask<Void, Void, Void> {

        ArrayList<PharmaciesModule> pharmacies = new ArrayList();
        String LOG_TAG = "FetchPharmaciesDetails";

        @Override
        protected Void doInBackground(Void... params) {

            pharmacies_list = new ArrayList<>();


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String pharmaciesJsonStr = null;

            // get all pharmacies from API
            try {
                String base_url = "http://medprices.c1.biz/api/getPharmacies.php";
                URL url = new URL(base_url);
                Log.d(LOG_TAG, "URL: " + url.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                pharmaciesJsonStr = buffer.toString();
                Log.d(LOG_TAG, "JSON Parsed: " + pharmaciesJsonStr);

                JSONObject main = new JSONObject(pharmaciesJsonStr);

                JSONArray result = main.getJSONArray("Pharmacies");
                JSONObject res;

                for (int i = 0; i < result.length(); i++) {
                    PharmaciesModule pharmacyData = new PharmaciesModule();

                    res = result.getJSONObject(i);
                    pharmacyData.setPharmId(res.getInt("id"));
                    pharmacyData.setPharmName(res.getString("name"));
                    pharmacyData.setPharmPhone(res.getString("phone"));
                    pharmacyData.setPharmAddress(res.getString("address"));
                    pharmacyData.setPharmNear(res.getString("landmark"));
                    pharmacyData.setPharmMail(res.getString("mail"));
                    pharmacyData.setGovId(res.getInt("gov"));
                    pharmacyData.setCityId(res.getInt("city"));
                    pharmacies.add(pharmacyData);
                }


            } catch (Exception e) {
                Log.e(LOG_TAG, "Error", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            // end of getting all pharmacies from API

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            pharmacies_list.clear();
            pharmacies_list = pharmacies;
        }
    }

    public static class FetchGovDetails extends AsyncTask<Void, Void, Void> {

        String LOG_TAG = "FetchGovsDetails";
        ArrayList<GovModule> govs = new ArrayList<>();

        @Override
        protected Void doInBackground(Void... params) {

            gov_list = new ArrayList<>();
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String GovsJsonStr = null;

            // start getting Gov data from API
            try {
                String base_url = "http://medprices.c1.biz/api/getGov.php";
                URL url = new URL(base_url);
                Log.d(LOG_TAG, "URL: " + url.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                GovsJsonStr = buffer.toString();
                Log.d(LOG_TAG, "JSON Parsed: " + GovsJsonStr);

                JSONObject main = new JSONObject(GovsJsonStr);

                JSONArray result = main.getJSONArray("Governerates");
                JSONObject res;

                for (int i = 0; i < result.length(); i++) {
                    GovModule govData = new GovModule();

                    res = result.getJSONObject(i);
                    govData.setId(res.getInt("id"));
                    govData.setGovernorateName(res.getString("governorate_name"));
                    govData.setGovernorateName_en(res.getString("governorate_name_en"));

                    govs.add(govData);
                }


            } catch (Exception e) {
                Log.e(LOG_TAG, "Error", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            // end of getting all Gov data from API
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            gov_list.clear();
            gov_list = govs;
        }
    }

    public static class FetchCitiesDetails extends AsyncTask<Void, Void, Void> {

        String LOG_TAG = "FetchCitiesDetails";
        ArrayList<CitesModule> cities = new ArrayList<>();

        @Override
        protected Void doInBackground(Void... params) {
            cites_list = new ArrayList<>();
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String citiesJsonStr = null;

            // start getting all Cites from API
            try {
                String base_url = "http://medprices.c1.biz/api/getCity.php";
                URL url = new URL(base_url);
                Log.d(LOG_TAG, "URL: " + url.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                citiesJsonStr = buffer.toString();
                Log.d(LOG_TAG, "JSON Parsed: " + citiesJsonStr);

                JSONObject main = new JSONObject(citiesJsonStr);

                JSONArray result = main.getJSONArray("Cities");
                JSONObject res;

                for (int i = 0; i < result.length(); i++) {
                    CitesModule cityData = new CitesModule();
                    res = result.getJSONObject(i);
                    cityData.setId(res.getInt("id"));
                    cityData.setGov_id(res.getInt("gov_id"));
                    cityData.setCityName(res.getString("city_name"));
                    cityData.setCityName_en(res.getString("city_name_en"));

                    cities.add(cityData);
                }


            } catch (Exception e) {
                Log.e(LOG_TAG, "Error", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            cites_list.clear();
            cites_list = cities;
        }
    }

    public static class FetchDrugsDetails extends AsyncTask<Void, Void, Void> {

        String LOG_TAG = "FetchDrugsDetails";
        ArrayList<DrugsModule> drugs = new ArrayList<>();

        @Override
        protected Void doInBackground(Void... params) {
            drugs_list = new ArrayList<>();
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String DrugsJsonStr = null;

            // getting all drugs from API
            try {
                String base_url = "http://medprices.c1.biz/api/getDrugs.php";
                URL url = new URL(base_url);
                Log.d(LOG_TAG, "URL: " + url.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                DrugsJsonStr = buffer.toString();
                Log.d(LOG_TAG, "JSON Parsed: " + DrugsJsonStr);

                JSONObject main = new JSONObject(DrugsJsonStr);

                JSONArray result = main.getJSONArray("Drugs");
                JSONObject res;

                for (int i = 0; i < result.length(); i++) {
                    DrugsModule drugData = new DrugsModule();
                    res = result.getJSONObject(i);
                    drugData.setId(res.getInt("id"));
                    drugData.setName(res.getString("name"));
                    drugData.setPrice(res.getString("price"));
                    drugData.setAvailabilityPharmacies(res.getString("availability"));

                    drugs.add(drugData);
                }


            } catch (Exception e) {
                Log.e(LOG_TAG, "Error", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            // end getting drug data from API
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            drugs_list.clear();
            drugs_list = drugs;
        }
    }

}
