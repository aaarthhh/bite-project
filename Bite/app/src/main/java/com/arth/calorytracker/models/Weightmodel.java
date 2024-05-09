package com.arth.calorytracker.models;

import com.arth.calorytracker.DateI;

import java.util.Comparator;

public class Weightmodel {

    String id,date,weight;

    public Weightmodel() {
    }

    public Weightmodel(String id, String date, String weight) {
        this.id = id;
        this.date = date;
        this.weight = weight;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}

