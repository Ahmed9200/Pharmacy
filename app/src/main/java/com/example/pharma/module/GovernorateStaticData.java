package com.example.pharma.module;

import java.util.ArrayList;

public class GovernorateStaticData {

    String govName_ar;
    String govName_en;
    ArrayList<String> citiesName_ar;
    ArrayList<String> citiesName_en;

    public GovernorateStaticData(String govName_ar, String govName_en, ArrayList<String> citiesName_ar, ArrayList<String> citiesName_en) {
        this.govName_ar = govName_ar;
        this.govName_en = govName_en;
        this.citiesName_ar = citiesName_ar;
        this.citiesName_en = citiesName_en;
    }

    public String getGovName_ar() {
        return govName_ar;
    }

    public void setGovName_ar(String govName_ar) {
        this.govName_ar = govName_ar;
    }

    public String getGovName_en() {
        return govName_en;
    }

    public void setGovName_en(String govName_en) {
        this.govName_en = govName_en;
    }

    public ArrayList<String> getCitiesName_ar() {
        return citiesName_ar;
    }

    public void setCitiesName_ar(ArrayList<String> citiesName_ar) {
        this.citiesName_ar = citiesName_ar;
    }

    public ArrayList<String> getCitiesName_en() {
        return citiesName_en;
    }

    public void setCitiesName_en(ArrayList<String> citiesName_en) {
        this.citiesName_en = citiesName_en;
    }
}
