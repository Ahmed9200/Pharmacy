package com.example.pharma.module;


import com.example.pharma.getAllDataFromAPI;

import java.util.ArrayList;
import java.util.Arrays;

public class DrugsModule {
    int id;
    String name;
    String price;
    String availabilityPharmacies;
    ArrayList<PharmaciesModule> pharmaciesList;

    public ArrayList<PharmaciesModule> getPharmaciesList() {
        if (pharmaciesList == null) {
            pharmaciesList = new ArrayList<>();
            return pharmaciesList;
        } else {
            return pharmaciesList;
        }
    }

    public void setPharmaciesList(ArrayList<PharmaciesModule> pharmaciesList) {
        this.pharmaciesList = pharmaciesList;
    }

    public String getAvailabilityPharmacies() {
        return availabilityPharmacies;
    }

    public void setAvailabilityPharmacies(String availabilityPharmacies) {
        this.availabilityPharmacies = availabilityPharmacies;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNextDrugId() {
        int[] allIDs = new int[getAllDataFromAPI.drugs_list.size()];
        for (int i = 0; i < getAllDataFromAPI.drugs_list.size(); i++) {
            allIDs[i] = getAllDataFromAPI.drugs_list.get(i).getId();
        }
        Arrays.sort(allIDs);
        return allIDs[allIDs.length - 1] + 1;
    }

}
