package com.example.chamod.cds_orm;

import android.util.Log;

import com.example.chamod.cds_orm.DBModels.Attribute;
import com.example.chamod.cds_orm.DBModels.DBTable;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by chamod on 4/1/17.
 */

public class AndroidModel {

    private final String Error_TAG="ORM_Exception";
    private final String Success_TAG="ORM";



    public void save(){
        createTable();

//      get all annotated fields
        Field[] fields=this.getClass().getFields();

        for (Field f:fields){
//           a db column
            if(f.getAnnotation(DBAnnotation.DBColumn.class)!=null){
//                Log.e(TAG,f.getName());
            }
        }


    }





    private void createTable(){
        DBTable dbTable;

//      get all annotated fields
        Field[] fields=this.getClass().getFields();
        DBAnnotation.TableName tableName =this.getClass().getAnnotation(DBAnnotation.TableName.class);
        if(tableName!=null) {
//            create a table
            dbTable=new DBTable(tableName.table_name());

            for (Field f:fields){
//           a db column
                if(f.getAnnotation(DBAnnotation.DBColumn.class)!=null){
                    dbTable.addAttribute(new Attribute(getColumnName(f),getDataType(f),isPrimary()));
                }
            }
        }
        else{
            Log.e(Error_TAG,"Please specify a table name using @TableName annotation");
        }
    }


    private String getDataType(Field f){


    }

    private String getColumnName(Field f){

    }

    public boolean isPrimary() {

        return false;
    }
}
