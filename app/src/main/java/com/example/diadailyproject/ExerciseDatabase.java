package com.example.diadailyproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.diadailyproject.ui.exercise.ExerciseFragment;

import java.util.ArrayList;
import java.util.List;

public class ExerciseDatabase extends SQLiteOpenHelper {

    public static final String EXERCISE_TABLE = "EXERCISE_TABLE";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EXERCISE = "exercise";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_CALORIES = "calories";
    public static final String COLUMN_TIME = "time";

    public ExerciseDatabase(@Nullable Context context) { super(context, "EXERCISE_TABLE", null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + EXERCISE_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_EXERCISE + " TEXT NOT NULL, "
                + COLUMN_DURATION + " TEXT NOT NULL, " + COLUMN_CALORIES + " TEXT NOT NULL, " + COLUMN_TIME + " TEXT NOT NULL);";

        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public boolean addOne(ExerciseModel exerciseModel) {

        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues cv = new ContentValues();

            cv.put(COLUMN_EXERCISE, exerciseModel.getExercise());
            cv.put(COLUMN_DURATION, exerciseModel.getDuration());
            cv.put(COLUMN_CALORIES, exerciseModel.getCalories());
            cv.put(COLUMN_TIME, exerciseModel.getTime());

            long insert = db.insert(EXERCISE_TABLE, null, cv);
            if (insert == -1) {
                return false;
            } else {
                return true;
            }
        }
    }
    public boolean deleteExercise(ExerciseModel exerciseModel) {

        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + EXERCISE_TABLE + " WHERE " + COLUMN_ID + " = " + exerciseModel.getId();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            return true;
        }
        else {
            return false;
        }
    }


    public List<ExerciseModel> getExercise() {

        List<ExerciseModel> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + EXERCISE_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String exercise = cursor.getString(1);
                String duration = cursor.getString(2);
                String calories = cursor.getString(3);
                String time = cursor.getString(4);

                ExerciseModel newExercise = new ExerciseModel(id, exercise, duration, calories, time);
                returnList.add(newExercise);
            } while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return returnList;

    }
}
