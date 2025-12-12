package com.example.weight_tracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

//THIS IS THE DATABASE HELPER SQLITE TO STORE USERS ON USERNAME AND PASSWORD
public class DatabaseHelper extends SQLiteOpenHelper {

    //Strings that the database will use, private
    private static final String DATABASE_NAME = "WeightTracker.db";
    private static final String TABLE_NAME = "users";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    //create db
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_USERNAME + " TEXT PRIMARY KEY, " +
                COL_PASSWORD + " TEXT)";
        db.execSQL(createTable);
        //create a goal table that will reference username to retrieve goal weights
        String createGoalTable = "CREATE TABLE IF NOT EXISTS GoalWeight (" +
                "username TEXT PRIMARY KEY, " +
                "goal_weight REAL, " +
                "FOREIGN KEY (username) REFERENCES users(username))";
        db.execSQL(createGoalTable);
        //create a weight history table with auto-incrementing ids for each entry
        String createWeightHistoryTable = "CREATE TABLE IF NOT EXISTS WeightHistory (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "weight REAL, " +
                "date TEXT, " +
                "FOREIGN KEY (username) REFERENCES users(username))";
        db.execSQL(createWeightHistoryTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //method to insert a new user into db after creation
    public boolean insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_USERNAME, username);
        contentValues.put(COL_PASSWORD, password);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }
    //method to check valid credentials passed
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " +
                COL_USERNAME + "=? AND " + COL_PASSWORD + "=?", new String[]{username, password});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }
    //check if a username already exists, if it does error
    public boolean checkUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " +
                COL_USERNAME + "=?", new String[]{username});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }
    //method to save goal weight
    public void saveGoalWeight(String username, double goalWeight){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("goal_weight", goalWeight);

        db.insertWithOnConflict("GoalWeight", null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }
    public double getGoalWeight(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT goal_weight FROM GoalWeight WHERE username = ?", new String[]{username});
        if (cursor.moveToFirst()){
            double weight = cursor.getDouble(0);
            cursor.close();
            return weight;
        }
        cursor.close();
        return 0.0;
    }
    //method to insert a new weight entry to db
    public void insertWeightEntry(String username, double weight, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("weight", weight);
        values.put("date", date);
        db.insert("WeightHistory", null, values);
    }
    //method to grab user weight history
    public ArrayList<WeightData> getWeightHistoryForUser(String username) {
        ArrayList<WeightData> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT id, weight, date FROM WeightHistory WHERE username = ? ORDER BY id DESC", new String[]{username});
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0); //get ID from result
            String weight = String.valueOf(cursor.getDouble(1));
            String date = cursor.getString(2);
            list.add(new WeightData(id, weight, date)); //call the correct constructor
        }
        cursor.close();
        return list;
    }

    //method to delete entry
    public void deleteWeightEntry(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("WeightHistory", "id = ?", new String[]{String.valueOf(id)});
    }
    public void updateWeightEntry(int id, double newWeight, String newDate){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("weight", newWeight);
        values.put("date", newDate);
        db.update("WeightHistory", values, "id = ?", new String[]{String.valueOf(id)});
    }

}
