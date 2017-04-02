package com.example.chamod.cds_orm;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.chamod.cds_orm.DBModels.Attribute;
import com.example.chamod.cds_orm.DBModels.DBTable;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by chamod on 4/1/17.
 */

public class AndroidModel {
    private Context context;

    public AndroidModel(Context context){
        this.context=context;
    }



    public void save(){
        AnnotationHandler annotationHandler=AnnotationHandler.getInstance(context);

//      get all annotated fields
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
                annotationHandler.createTable(getClass());
                continue;
            }
            break;
        }
    }






}
