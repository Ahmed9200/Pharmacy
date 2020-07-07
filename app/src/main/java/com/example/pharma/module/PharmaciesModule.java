package com.example.pharma.module;

import com.example.pharma.getAllDataFromAPI;

import java.util.ArrayList;

public class PharmaciesModule {
    int pharmId;
    String pharmName;
    String pharmPhone;
    String pharmAddress;
    String pharmNear;
    String pharmMail;
    String pharmPass;
    int govId;
    int cityId;
    ArrayList<DrugsModule> drugList;

    public ArrayList<DrugsModule> getDrugList() {
        drugList = new ArrayList<>();
        for (int i=0;i<getAllDataFromAPI.drugs_list.size();i++){
            for (int j=0 ; j<getAllDataFromAPI.drugs_list.get(i).getPharmaciesList().size();j++){
                if (getAllDataFromAPI.drugs_list.get(i).getPharmaciesList().get(j).getPharmMail().equals(pharmMail)){
                    drugList.add(getAllDataFromAPI.drugs_list.get(i));
                }
            }
        }
        return drugList;
    }

    public PharmaciesModule getPharmacyByUsername_pw(String mail , String pass){
        PharmaciesModule ph = new PharmaciesModule();
        for (int i=0; i< getAllDataFromAPI.pharmacies_list.size();i++){
            if (getAllDataFromAPI.pharmacies_list.get(i).getPharmMail().equals(mail)){
                ph.setCityId(getAllDataFromAPI.pharmacies_list.get(i).getCityId());
                ph.setGovId(getAllDataFromAPI.pharmacies_list.get(i).getGovId());
                ph.setPharmAddress(getAllDataFromAPI.pharmacies_list.get(i).getPharmAddress());
                ph.setPharmId(getAllDataFromAPI.pharmacies_list.get(i).getPharmId());
                ph.setPharmMail(getAllDataFromAPI.pharmacies_list.get(i).getPharmMail());
                ph.setPharmName(getAllDataFromAPI.pharmacies_list.get(i).getPharmName());
                ph.setPharmNear(getAllDataFromAPI.pharmacies_list.get(i).getPharmNear());
                ph.setPharmPass(pass);
                ph.setPharmPhone(getAllDataFromAPI.pharmacies_list.get(i).getPharmPhone());
            }
        }
        return ph;
    }

    public int getPharmId() {
        return pharmId;
    }

    public void setPharmId(int pharmId) {
        this.pharmId = pharmId;
    }

    public int getGovId() {
        return govId;
    }

    public void setGovId(int govId) {
        this.govId = govId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getPharmName() {
        return pharmName;
    }

    public void setPharmName(String pharmName) {
        this.pharmName = pharmName;
    }

    public String getPharmPhone() {
        return pharmPhone;
    }

    public void setPharmPhone(String pharmPhone) {
        this.pharmPhone = pharmPhone;
    }

    public String getPharmAddress() {
        return pharmAddress;
    }

    public void setPharmAddress(String pharmAddress) {
        this.pharmAddress = pharmAddress;
    }

    public String getPharmNear() {
        return pharmNear;
    }

    public void setPharmNear(String pharmNear) {
        this.pharmNear = pharmNear;
    }

    public String getPharmMail() {
        return pharmMail;
    }

    public void setPharmMail(String pharmMail) {
        this.pharmMail = pharmMail;
    }

    public String getPharmPass() {
        return pharmPass;
    }

    public void setPharmPass(String pharmPass) {
        this.pharmPass = pharmPass;
    }

    public String getGovName(){
        String name="";
        for (int i=0 ; i<getAllDataFromAPI.gov_list.size();i++){
            if (getAllDataFromAPI.gov_list.get(i).getId() == govId){
                name = getAllDataFromAPI.gov_list.get(i).getGovernorateName();
            }
        }
        return name;
    }

    public String getCityName(){
        String name = "";
        for (int i=0 ; i<getAllDataFromAPI.cites_list.size();i++){
            if (getAllDataFromAPI.cites_list.get(i).getId() == cityId){
                name = getAllDataFromAPI.cites_list.get(i).getCityName();
            }
        }
        return name;
    }

    public int getCityId_fromName(String name){
        int id =0;
        for (int i=0 ; i<getAllDataFromAPI.cites_list.size();i++){
            if (getAllDataFromAPI.cites_list.get(i).getCityName().equals(name)){
                id = getAllDataFromAPI.cites_list.get(i).getId();
            }
        }
        return id;
    }
    public int getGovId_fromName(String name){
        int id = 0;
        for (int i=0 ; i<getAllDataFromAPI.gov_list.size();i++){
            if (getAllDataFromAPI.gov_list.get(i).getGovernorateName().equals(name)){
                id = getAllDataFromAPI.gov_list.get(i).getId();
            }
        }
        return id;
    }
}
