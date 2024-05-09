package com.arth.calorytracker.models;

public class HisCalo {

    String id,date,totalcal;


    public HisCalo() {
    }

    public HisCalo(String id,String date, String totalcal) {
        this.id = id;
        this.date = date;
        this.totalcal = totalcal;
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

    public String getTotalcal() {
        return totalcal;
    }

    public void setTotalcal(String totalcal) {
        this.totalcal = totalcal;
    }
}
