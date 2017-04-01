package com.example.chamod.cds_orm;

import android.util.Log;

import java.lang.reflect.Field;

/**
 * Created by chamod on 4/1/17.
 */

public class AndroidModel {

    private final String Error_TAG="ORM_Exception";
    private final String Success_TAG="ORM";



    public void save(){

        DBAnnotation.TableName tableName =this.getClass().getAnnotation(DBAnnotation.TableName.class);
        if(tableName!=null) {
            Log.w(Success_TAG, tableName.table_name());


            Field[] fields=this.getClass().getFields();

            for (Field f:fields){
                if(f.getAnnotation(DBAnnotation.DBColumn.class)!=null){
//                Log.e(TAG,f.getName());
                }
            }


        }
        else{
            Log.e(Error_TAG,"Please specify a table name using @TableName annotation");
        }

    }
}
