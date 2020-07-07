package com.example.pharma.module;

import com.example.pharma.getAllDataFromAPI;

public class CitesModule {
    int id;
    int gov_id;
    String cityName;
    String cityName_en;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGov_id() {
        return gov_id;
    }

    public void setGov_id(int gov_id) {
        this.gov_id = gov_id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName_en() {
        return cityName_en;
    }

    public void setCityName_en(String cityName_en) {
        this.cityName_en = cityName_en;
    }

    public int getIdFromName(String name){
        int id=0;
        for (int i=0;i< getAllDataFromAPI.cites_list.size();i++){
            if (getAllDataFromAPI.cites_list.get(i).getCityName().equals(name)){
                id = getAllDataFromAPI.cites_list.get(i).getId();
            }
        }
        return id;
    }
}
