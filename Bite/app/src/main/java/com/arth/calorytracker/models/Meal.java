package com.arth.calorytracker.models;

public class Meal {

    String id,name,pro,fat,carbs,calories,etqty,imageurl,mealname;

    public Meal() {
    }


    public Meal(String id, String name, String pro, String fat, String carbs, String calories, String etqty, String imageurl, String mealname) {
        this.id = id;
        this.name = name;
        this.pro = pro;
        this.fat = fat;
        this.carbs = carbs;
        this.calories = calories;
        this.etqty = etqty;
        this.imageurl = imageurl;
        this.mealname = mealname;
    }

    public String getMealname() {
        return mealname;
    }

    public void setMealname(String mealname) {
        this.mealname = mealname;
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
