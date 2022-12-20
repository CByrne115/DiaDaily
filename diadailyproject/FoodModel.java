package com.example.diadailyproject;

public class FoodModel {

    private int id;
    private String food;
    private String sugar;
    private String time;
    private String date;

    //con

    public FoodModel(int id, String food, String sugar, String time, String date) {
        this.id = id;
        this.food = food;
        this.sugar = sugar;
        this.time = time;
        this.date = date;
    }

    //toString


    @Override
    public String toString() {
        return "FoodModel{" +
                "id=" + id +
                ", food='" + food + '\'' +
                ", sugar='" + sugar + '\'' +
                ", time='" + time + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    //get+set

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getSugar() {
        return sugar;
    }

    public void setSugar(String sugar) {
        this.sugar = sugar;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
