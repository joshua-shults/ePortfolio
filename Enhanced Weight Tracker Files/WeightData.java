package com.example.weight_tracker;

// Weight class to use in setting weights/retrieving
public class WeightData {
    private int id;            //id from database
    private String weight;     //current displayed weight
    private String date;       //date associated with displayed weight

    // Constructor for existing user data
    public WeightData(int id, String weight, String date) {
        this.id = id;
        this.weight = weight;
        this.date = date;
    }

    //default constructor
    public WeightData(String weight, String date) {
        this.weight = weight;
        this.date = date;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getWeight() {
        return weight;
    }

    public String getDate() {
        return date;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
