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
                    dbTable.addAttribute(new Attribute(getColumnName(f),getDataType(f),isPrimary(f)));
                }
            }
        }
        else{
            Log.e(Error_TAG,"Please specify a table name using @TableName annotation");
        }
    }


    private String getDataType(Field f){
        DBAnnotation.DataType dataType=f.getAnnotation(DBAnnotation.DataType.class);
        if(dataType!=null){
            return dataType.data_type();
        }
        if(f.getType().equals(int.class) || f.getType().equals(Integer.class)){
            return "INT";
        }
        if(f.getType().equals(String.class)){
            return "TEXT";
        }

        return  null;
    }

    private String getColumnName(Field f){
        DBAnnotation.ColumnName columnName=f.getAnnotation(DBAnnotation.ColumnName.class);
        if(columnName!=null){
            return columnName.column_name();
        }
        return f.getName();
    }

    public boolean isPrimary(Field f) {
        if(f.getAnnotation(DBAnnotation.PrimaryKey.class)!=null){
            return true;
        }
        return false;
    }
}
