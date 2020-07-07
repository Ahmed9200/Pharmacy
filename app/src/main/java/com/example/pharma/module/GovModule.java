package com.example.pharma.module;

import com.example.pharma.getAllDataFromAPI;

public class GovModule {
    int id;
    String governorateName;
    String governorateName_en;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGovernorateName() {
        return governorateName;
    }

    public void setGovernorateName(String governorateName) {
        this.governorateName = governorateName;
    }

    public String getGovernorateName_en() {
        return governorateName_en;
    }

    public void setGovernorateName_en(String governorateName_en) {
        this.governorateName_en = governorateName_en;
    }

    public int getIdFromName(String name){
        int id=0;
        for (int i = 0; i< getAllDataFromAPI.gov_list.size(); i++){
            if (getAllDataFromAPI.gov_list.get(i).getGovernorateName().equals(name)){
                id = getAllDataFromAPI.gov_list.get(i).getId();
            }
        }
        return id;
    }
}
