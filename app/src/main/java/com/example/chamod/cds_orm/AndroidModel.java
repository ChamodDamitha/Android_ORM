package com.example.chamod.cds_orm;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.chamod.cds_orm.DBModels.DBTable;

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
        DB_Helper.getInstance(context).saveModel(this);
    }
//    //    .................updating record.............................................................
//    public boolean update(){
//        try {
//            DB_Helper.getInstance(context).updateModel(this);
//        }
//        catch (SQLiteException e){
//            Log.e("ORM","fuckkkk");
//            return false;
//        }
//        return true;
//    }
//
//
////..................retrieving data back from database.............................................
    public static <T>T getFirst(Class<T> clas,Context context){
        return (T)DB_Helper.getInstance(context).readFirstRecord(clas);

    }
    public static <T>ArrayList<T> getAll(Class<T> clas, Context context){
        return DB_Helper.getInstance(context).readAllRecords(clas);

    }
    public static <T>ArrayList<T> get(Class<T> claz, Context context,String key,Object value){
        return DB_Helper.getInstance(context).readRecords(claz,key,value);
    }

////...................delete a model................................................................
//    public static void delete(Class<?> clas, Context context,String key,Object value){
//        DBTable dbTable=AnnotationHandler.createTable(clas);
//        for(ForeignKey foreignKey:dbTable.getForeignKeys()){
//            DB_Helper.getInstance(context).
//                deleteRecords(AnnotationHandler.createTable(foreignKey.getRef_class()).getTable_name(),
//                        foreignKey.getField_name(),value);
//        }
//
//        DB_Helper.getInstance(context).
//                deleteRecords(AnnotationHandler.createTable(clas).getTable_name(),key,value);
//    }

}
