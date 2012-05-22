/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.juick.android;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 * @author kerrigan
 */
public class DbHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private SQLiteDatabase db;
    private SQLiteDatabase dbw;
    private SQLiteDatabase dbr;
    
    public DbHandler(Context context) {
        super(context, "juickdb", null, DB_VERSION);
    }
    
        
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE threads(mid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + "json TEXT NOT NULL)");
        db.execSQL("CREATE TABLE digests(type INTEGER NOT NULL, json TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        db.execSQL("DROP TABLE IF EXISTS threads;");
        db.execSQL("DROP TABLE IF EXISTS digests;");
        onCreate(db);
    }
    
    public void upsertThread(int mid, String json){
        dbw = getWritableDatabase();
        json = json.trim();
        Object data[] = {mid, json};
        Object data1[] = {json, mid};
        dbw.execSQL("INSERT OR IGNORE INTO threads (mid,json) VALUES(?,?)", data);
        dbw.execSQL("UPDATE threads set json = ? WHERE mid = ?", data1);
        dbw.close();
    }
    
    public String getThread(int mid){
        dbr = getReadableDatabase();
        try{
        Cursor cursor = dbr.rawQuery("SELECT json FROM threads WHERE mid = " + mid, null);
        cursor.moveToFirst();
        String json = cursor.getString(0);
        return json;
        } catch(Exception e){
        return "[]";            
        } finally {
            dbr.close();
        }

    }    
}
