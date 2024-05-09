package com.arth.calorytracker.models;

public class Food {

    String id,name,pro,fat,carbs,calories,etqty,imageurl;

    public Food() {
    }

    public Food(String id, String name, String pro, String fat, String carbs, String calories,String etqty, String imageurl) {
        this.id = id;
        this.name = name;
        this.pro = pro;
        this.fat = fat;
        this.carbs = carbs;
        this.calories = calories;
        this.etqty = etqty;
        this.imageurl = imageurl;
    }

    public String getEtqty() {
        return etqty;
    }

    public void setEtqty(String etqty) {
        this.etqty = etqty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPro() {
        return pro;
    }

    public void setPro(String pro) {
        this.pro = pro;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getCarbs() {
        return carbs;
    }

    public void setCarbs(String carbs) {
        this.carbs = carbs;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
