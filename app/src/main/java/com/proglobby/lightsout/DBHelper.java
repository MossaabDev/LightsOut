package com.proglobby.lightsout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context) {
        super(context, "lightsout.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE scores (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, team TEXT, img TEXT, time TEXT)";
        db.execSQL(createTable);
        String createTable2 = "CREATE TABLE user (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, team TEXT, img TEXT, time TEXT, email TEXT)";
        db.execSQL(createTable2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS scores");
        onCreate(db);
    }

    public void addDriver(Driver driver){
        SQLiteDatabase db = this.getWritableDatabase();
        String insert = "INSERT INTO scores (name, team, img, time) VALUES ('"+driver.getName()+"', '"+driver.getTeam()+"', '"+driver.getImg()+"', '"+driver.getTime()+"')";
        db.execSQL(insert);
    }

    public void deleteDriver(Driver driver){
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM scores WHERE name = '"+driver.getName()+"'";
        db.execSQL(delete);
    }

    public void updateDriver(Driver driver){
        SQLiteDatabase db = this.getWritableDatabase();
        String update = "UPDATE scores SET team = '"+driver.getTeam()+"', img = '"+driver.getImg()+"', time = '"+driver.getTime()+"' WHERE name = '"+driver.getName()+"'";
        db.execSQL(update);
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM scores";
        db.execSQL(delete);
    }

    public void close(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.close();
    }

    public List<Driver> getDrivers(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM scores", null);
        List<Driver> drivers = new ArrayList<>();
        while (cursor.moveToNext()){
            String name = cursor.getString(1);
            String team = cursor.getString(2);
            String img = cursor.getString(3);
            String time = cursor.getString(4);
            drivers.add(new Driver(name, team, img, time));
        }
        return drivers;
    }

    public void initUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id", 0);
        cv.put("name", user.getName());
        cv.put("team", user.getTeam());
        cv .put("time", user.getTime());
        cv.put("img", user.getImg());
        cv.put("email", user.getEmail());
        db.insert("user", null, cv);
    }

    public User getUserInfos(){
       SQLiteDatabase db = this.getReadableDatabase();
       Cursor cursor = db.rawQuery("SELECT * FROM user", null);
       while (cursor.moveToNext()){
           String name = cursor.getString(1);
           String team = cursor.getString(2);
           String img = cursor.getString(3);
           String time = cursor.getString(4);
           String email = cursor.getString(5);
           return new User(name, team, img, time, email);
       }
       return new User("", "", "", "");
    }

    public void initScoreTable(List<Driver> drivers){
        SQLiteDatabase db = this.getWritableDatabase();
        for (Driver driver: drivers){
            ContentValues cv = new ContentValues();
            cv.put("name", driver.getName());
            cv.put("team", driver.getTeam());
            cv .put("time", driver.getTime());
            cv.put("img", driver.getImg());
            db.insert("score", null, cv);
        }
    }


}
