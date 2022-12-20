package com.example.diadailyproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FoodDatabase extends SQLiteOpenHelper {

    public static final String FOOD_TABLE = "FOOD_TABLE";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FOOD = "food";
    public static final String COLUMN_SUGAR = "sugar";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_DATE = "date";

    public FoodDatabase(@Nullable Context context) {
        super(context, "FOOD_TABLE", null, 1);
    }


    //database creation
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + FOOD_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_FOOD + " TEXT NOT NULL, "
                + COLUMN_SUGAR + " TEXT NOT NULL, " + COLUMN_TIME + " TEXT NOT NULL, " + COLUMN_DATE + " TEXT NOT NULL);";

        db.execSQL(query);

    }


    //database updates
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }




    public boolean addOne(FoodModel foodModel) {

        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();

            cv.put(COLUMN_FOOD, foodModel.getFood());
            cv.put(COLUMN_SUGAR, foodModel.getSugar());
            cv.put(COLUMN_TIME, foodModel.getTime());
            cv.put(COLUMN_DATE, foodModel.getDate());

            long insert = db.insert(FOOD_TABLE, null, cv);
            if (insert == -1) {
                return false;
            } else {
                return true;
            }
        }


        }
        //delete food function

    public boolean deleteFood(FoodModel foodModel) {

        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + FOOD_TABLE + " WHERE " + COLUMN_ID + " = " + foodModel.getId();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            return true;
        }
        else {
            return false;
        }

    }
        //display food in db

        public List<FoodModel> getFood() {

        List<FoodModel> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + FOOD_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);


       if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String food = cursor.getString(1);
                String sugar = cursor.getString(2);
                String time = cursor.getString(3);
                String date = cursor.getString(4);

                FoodModel newFood = new FoodModel(id, food, sugar, time, date);
                returnList.add(newFood);



            } while (cursor.moveToNext());


        }



            cursor.close();
            db.close();
            return returnList;


        }


}

