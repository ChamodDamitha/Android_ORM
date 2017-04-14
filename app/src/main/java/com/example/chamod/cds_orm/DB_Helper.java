package com.example.chamod.cds_orm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.chamod.cds_orm.DBModels.Attribute;
import com.example.chamod.cds_orm.DBModels.DBTable;
import com.example.chamod.cds_orm.DBModels.ForeignKey;

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
    private final static int db_version=2;

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
        Log.e("ORM","table deleting ..........");
        String del_query="DROP TABLE IF EXISTS "+dbTable.getName()+";";

        SQLiteDatabase db=getWritableDatabase();

        db.execSQL(del_query);

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

        db.execSQL(query);
        db.close();
    }

    public int getMaxId(String tableName,String column_name){
        SQLiteDatabase db=getReadableDatabase();
        String query="SELECT MAX("+column_name+") as maxx FROM "+tableName+";";
        Cursor c=db.rawQuery(query,null);
        while (c.moveToNext()){
            return c.getInt(c.getColumnIndex("maxx"));
        }
        return -1;
    }


    public void insertRecord(String tableName,ContentValues contentValues){
        SQLiteDatabase db=getWritableDatabase();
        db.insertOrThrow(tableName,null,contentValues);
        db.close();
    }

    private Object getInstanceOf(Class<?> clas){
        try {
            Class<?> c = Class.forName(clas.getName());
            Constructor<?> cons = c.getConstructor(Context.class);
            return cons.newInstance(context);
        } catch (NoSuchMethodException|ClassNotFoundException|IllegalAccessException
                |InstantiationException|InvocationTargetException e) {
            return null;
        }
    }


    public Object readFirstRecord(Class<?> clas, DBTable dbTable){
            SQLiteDatabase db = getReadableDatabase();

            String query = "SELECT * FROM " + dbTable.getName() + " LIMIT 1;";
            Cursor cursor = db.rawQuery(query, null);

            while (cursor.moveToNext()) {

                    Object object = getInstanceOf(clas);
//                  set attributes
                    for (Attribute attribute : dbTable.getAttributes()
                            ) {
                            try {
                                Field f = clas.getField(attribute.getName());
//                            check type of the field
                                if (f.getType().equals(String.class)) {
                                    f.set(object, cursor.getString(cursor.getColumnIndex(AnnotationHandler.getColumnName(f))));
                                } else if (f.getType().equals(int.class) || f.getType().equals(Integer.class)) {
                                    f.set(object, cursor.getInt(cursor.getColumnIndex(AnnotationHandler.getColumnName(f))));
                                }
                            } catch (NoSuchFieldException|IllegalAccessException e) {
                                e.printStackTrace();
                            }

                    }

//                set foreign key refered objects
                    for (ForeignKey foreignKey:dbTable.getForeignKeys()){
                        Object sub_object=null;
                        if (foreignKey.getType().equals(String.class)) {
                            sub_object=readRecords(foreignKey.getRef_class(),AnnotationHandler.createTable(foreignKey.getRef_class()),
                                    foreignKey.getField_name(),cursor.getString(cursor.getColumnIndex(foreignKey.getName())));
                        } else if (foreignKey.getType().equals(int.class) || foreignKey.getType().equals(Integer.class)) {
                            sub_object=readRecords(foreignKey.getRef_class(),AnnotationHandler.createTable(foreignKey.getRef_class()),
                                    foreignKey.getField_name(),cursor.getInt(cursor.getColumnIndex(foreignKey.getName())));
                        }
                        try {
                            Field f = clas.getField(foreignKey.getField_name());
                            f.set(object,sub_object);
                        } catch (NoSuchFieldException|IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    return object;
        }
        return null;
    }


    public <T>ArrayList<T> readAllRecords(Class<T> clas, DBTable dbTable){
        ArrayList<T> objects=new ArrayList<>();

        try {
            Class<?> c = Class.forName(clas.getName());
            Constructor<?> cons = c.getConstructor(Context.class);

            SQLiteDatabase db = getReadableDatabase();

            String query = "SELECT * FROM " + dbTable.getName() + ";";
            Cursor cursor = db.rawQuery(query, null);

            while (cursor.moveToNext()) {
                try {
                    Object object = cons.newInstance(context);

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
                    objects.add((T) object);
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
            }

            db.close();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return objects;
    }



    public <T>ArrayList<T> readRecords(Class<T> clas, DBTable dbTable,String key,Object value){
        ArrayList<T> objects=new ArrayList<>();

        try {
            Class<?> c = Class.forName(clas.getName());
            Constructor<?> cons = c.getConstructor(Context.class);

            SQLiteDatabase db = getReadableDatabase();

            String query = "SELECT * FROM " + dbTable.getName()+" WHERE ";

            if(value.getClass().equals(String.class)){
                query+=key+"='"+value+"' ;";
            }
            else{
                query+=key+"="+value+" ;";
            }

            Cursor cursor = db.rawQuery(query, null);

            while (cursor.moveToNext()) {
                try {
                    Object object = cons.newInstance(context);

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
                    objects.add((T) object);
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
            }

            db.close();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return objects;
    }

    public void updateRecord(String tableName,ContentValues cv,String key,Object value){
        SQLiteDatabase db=getWritableDatabase();

        String whereClause;
        if(value.getClass().equals(String.class)){
            whereClause=key+" = '"+value.toString()+"'";
        }
        else{
            whereClause=key+" = "+value.toString();
        }
        db.update(tableName,cv,whereClause,null);
        db.close();
    }


    public void deleteRecords(String tableName, String key, Object value){
        SQLiteDatabase db=getWritableDatabase();

        String whereClause;
        if(value.getClass().equals(String.class)){
            whereClause=key+" = '"+value.toString()+"'";
        }
        else{
            whereClause=key+" = "+value.toString();
        }
        db.delete(tableName,whereClause,null);
        db.close();
    }
}
