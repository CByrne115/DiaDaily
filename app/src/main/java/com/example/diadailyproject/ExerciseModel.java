package com.example.diadailyproject;

public class ExerciseModel {

    private int id;
    private String exercise;
    private String duration;
    private String calories;
    private String time;

    public ExerciseModel(int id, String exercise, String duration, String calories, String time) {
        this.id = id;
        this.exercise = exercise;
        this.duration = duration;
        this.calories = calories;
        this.time = time;
    }
    @Override
    public String toString() {
        return exercise + ",  " + calories + "cal,  " + duration +  " min, " + time;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getExercise() {
        return exercise;
    }
    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCalories() {
        return calories;
    }
    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

}