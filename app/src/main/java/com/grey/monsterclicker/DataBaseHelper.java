package com.grey.monsterclicker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String db_name = "todoList";
    private static final int db_ver=1;
    private static final String db_table = "scores";
    private static final String db_column_name = "name";
    private static final String db_column_score = "score";

    public DataBaseHelper(Context context, String name, int version) {
        super(context, name,null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL, %s NUMBER NOT NULL);",
                       db_table,db_column_name,db_column_score);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = String.format("DELETE TABLE IF EXISTS %s)", db_table);
        db.execSQL(query);
        onCreate(db);
    }
    public void insertData(Player player){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(db_column_name, player.getName());
        value.put(db_column_score, player.getScore());
        int u = db.update(db_table, value, db_column_name + "=?", new String[] {player.getName()});
        if(u == 0) {
           db.insertWithOnConflict(db_table, null, value, SQLiteDatabase.CONFLICT_IGNORE);
        }
        db.close();
    }

    public void deleteData(String task){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(db_table, db_column_name + " = ?", new String[]{task});
        db.close();

    }

    public ArrayList<Player> getAllPlayers(){
        ArrayList<Player> allPlayers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.query(db_table,null, null,null,null,null, db_column_score + " + 0 DESC");
        for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()){
            Player player = new Player(cur.getString(cur.getColumnIndex(db_column_name)), cur.getString(cur.getColumnIndex(db_column_score)));
            allPlayers.add(player);
        }
        cur.close();
        db.close();
        return allPlayers;
    }
}
