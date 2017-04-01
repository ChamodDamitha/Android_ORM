package com.example.chamod.cds_orm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chamod on 4/1/17.
 */

public class DB_Helper extends SQLiteOpenHelper {
    private final static String db_name="TestDB";
    private final static int db_version=1;

    private static DB_Helper db_helper=null;

    public DB_Helper getInstance(Context context){
        if(db_helper==null){
            db_helper=new DB_Helper(context);
        }
        return db_helper;
    }


    private DB_Helper(Context context) {
        super(context, db_name, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
