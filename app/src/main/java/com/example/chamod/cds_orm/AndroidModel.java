package com.example.chamod.cds_orm;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteException;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by chamod on 4/1/17.
 */

public class AndroidModel {
    private static Context context;

    public AndroidModel(Context context){
        this.context=context;
    }



//    inserting a new object to the database
    public void save(){
        AnnotationHandler annotationHandler=AnnotationHandler.getInstance(context);

//      getAll all annotated fields
        Field[] fields=this.getClass().getFields();

        ContentValues cv=new ContentValues();

        for (Field f:fields){
//           a db column
            if(annotationHandler.isAttribute(f)){
                try {
                    cv.put(annotationHandler.getColumnName(f),(f.get(this)).toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        DB_Helper db_helper=DB_Helper.getInstance(context);
        while(true) {
            try {
                db_helper.insertRecord(annotationHandler.getTableName(getClass()), cv);
            }
            catch (SQLiteException e) {
                db_helper.createTable(annotationHandler.createTable(getClass()));
                continue;
            }
            break;
        }
    }


    public static <T>T getFirst(Class<T> clas,Context context){
        return (T)DB_Helper.getInstance(context).readFirstRecord(clas,
                AnnotationHandler.getInstance(context).createTable(clas));

    }

    public static <T>ArrayList<T> getAll(Class<T> clas, Context context){
        return DB_Helper.getInstance(context).readAllRecords(clas,
                AnnotationHandler.getInstance(context).createTable(clas));

    }



}
