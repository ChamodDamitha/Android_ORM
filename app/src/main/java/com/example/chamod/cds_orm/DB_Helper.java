package com.example.chamod.cds_orm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.chamod.cds_orm.DBModels.Attribute;
import com.example.chamod.cds_orm.DBModels.DBTable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Created by chamod on 4/1/17.
 */

public class DB_Helper extends SQLiteOpenHelper {
    private Context context;
    private final static String db_name="TestDB";
    private final static int db_version=1;

    private static DB_Helper db_helper=null;


    public static DB_Helper getInstance(Context context){
        if(db_helper==null){
            db_helper=new DB_Helper(context);
        }
        return db_helper;
    }


    private DB_Helper(Context context) {
        super(context, db_name, null, db_version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void createTable(DBTable dbTable){
        Log.e("ORM","table creating ..........");

        String query="CREATE TABLE "+dbTable.getName()+"(";

        int i=1,size=dbTable.getAttributes().size();
        for (Attribute a:dbTable.getAttributes()
             ) {
            query+=a.getName()+" "+a.getType();
            if(a.isPrimary()){
                query+=" PRIMARY KEY";
            }
            if(i!=size){
                query+=",";
            }
            i++;
        }
        query+=");";

        SQLiteDatabase db=getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    public void insertRecord(String tableName,ContentValues contentValues){
        SQLiteDatabase db=getWritableDatabase();
        db.insertOrThrow(tableName,null,contentValues);
        db.close();
    }
    public Object readFirstRecord(Class<?> clas, DBTable dbTable){

        try {
            Class<?> c = Class.forName(clas.getName());
            Constructor<?> cons = c.getConstructor(Context.class);

            SQLiteDatabase db = getReadableDatabase();

            String query = "SELECT * FROM " + dbTable.getName() + ";";
            Cursor cursor = db.rawQuery(query, null);

            while (cursor.moveToNext()) {
                try {
                    Object object = cons.newInstance(context);

                    Log.w("ORM",object.getClass().getName());

                    for (Attribute attribute : dbTable.getAttributes()
                            ) {
                        try {
                            Field f=clas.getField(attribute.getName());
//                            check type of the field
                            if(f.getType().equals(String.class)){
                                f.set(object,cursor.getString(cursor.getColumnIndex(AnnotationHandler.getColumnName(f))));
                            }
                            else if(f.getType().equals(int.class) || f.getType().equals(Integer.class)){
                                f.set(object,cursor.getInt(cursor.getColumnIndex(AnnotationHandler.getColumnName(f))));
                            }
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        }
                    }
                    return object;
                }
                catch (InstantiationException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                break;
            }

            db.close();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }



}
