package com.example.chamod.cds_orm;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteException;
import android.util.Log;

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


    public static Field getPrimaryField(Class<?> clas){
        Field[] fields=clas.getFields();
        for(Field f:fields){
            if(AnnotationHandler.isPrimary(f)){
                return f;
            }
        }
        return null;
    }


//    inserting a new object to the database
    public void save(){

        DB_Helper db_helper=DB_Helper.getInstance(context);


        Field primary_field=null,android_model_field=null;
//      getAll all annotated fields
        Field[] fields=this.getClass().getFields();

        ContentValues cv=new ContentValues();

        for (Field f:fields){

//            if a model object is associated
            if(AnnotationHandler.isDBModel(f)){
                android_model_field=f;
            }
//           a db column
            else if(AnnotationHandler.isAttribute(f)){
                try {
                    if(AnnotationHandler.isPrimary(f)){
                        primary_field=f;
                        if(f.getType().equals(int.class) || f.getType().equals(Integer.class)){
                            continue;
                        }
                    }

                    cv.put(AnnotationHandler.getColumnName(f),(f.get(this)).toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        while(true) {
            try {
                db_helper.insertRecord(AnnotationHandler.getTableName(getClass()), cv);
//           if primary key is auto incremented
                int id=db_helper.getMaxId(AnnotationHandler.createTable(this.getClass()).getName(),primary_field.getName());
                if(id!=-1) {
                    primary_field.set(this,id);

                    if(android_model_field!=null){
                        try {
                            AndroidModel androidModel=(AndroidModel) android_model_field.get(this);
                            androidModel.saveWithExtraValue(this.getClass().getSimpleName()+
                                    AndroidModel.getPrimaryField(this.getClass()).getName(),id);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }



                }
            }
            catch (IllegalAccessException  e){

            }
            catch (NullPointerException e){

            }
            catch (SQLiteConstraintException e){
                Log.e("ORM","Duplicate entry for same primary key");
            }
            catch (SQLiteException e){
                e.printStackTrace();
//                if(e.getMessage().split(":")[0].equals("no such table")) {
                    db_helper.createTable(AnnotationHandler.createTable(getClass()));
//                }
                continue;
            }
            break;
        }
    }

    private void saveWithExtraValue(String key, Object value) {

        DB_Helper db_helper=DB_Helper.getInstance(context);


        Field primary_field=null,android_model_field=null;
//      getAll all annotated fields
        Field[] fields=this.getClass().getFields();

        ContentValues cv=new ContentValues();

        for (Field f:fields){

//            if a model object is associated
            if(AnnotationHandler.isDBModel(f)){
                android_model_field=f;
            }

//           a db column
            else if(AnnotationHandler.isAttribute(f)){
                try {
                    if(AnnotationHandler.isPrimary(f)){
                        primary_field=f;
                        if(f.getType().equals(int.class) || f.getType().equals(Integer.class)){
                            continue;
                        }
                    }

                    cv.put(AnnotationHandler.getColumnName(f),(f.get(this)).toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
//      set extra value
        cv.put(key,value.toString());

        while(true) {
            try {
                db_helper.insertRecord(AnnotationHandler.getTableName(getClass()), cv);
//           if primary key is auto incremented
                int id=db_helper.getMaxId(AnnotationHandler.createTable(this.getClass()).getName(),primary_field.getName());
                if(id!=-1) {
                    primary_field.set(this,id);

                    if(android_model_field!=null){
                        try {
                            AndroidModel androidModel=(AndroidModel) android_model_field.get(this);
                            androidModel.saveWithExtraValue(this.getClass().getSimpleName()+
                                    AndroidModel.getPrimaryField(this.getClass()).getName(),id);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
            catch (IllegalAccessException  e){

            }
            catch (NullPointerException e){

            }
            catch (SQLiteConstraintException e){
                Log.e("ORM","Duplicate entry for same primary key");
            }
            catch (SQLiteException e){
                e.printStackTrace();
                if(e.getMessage().split(":")[0].equals("no such table")) {
                    db_helper.createTable(AnnotationHandler.createTable(getClass()));
                }
                else{
                    if(value.getClass().equals(String.class)) {
                        db_helper.addTableColumn(AnnotationHandler.createTable(getClass()), key,"TEXT");
                    }
                    else if(value.getClass().equals(Integer.class) || value.getClass().equals(int.class)) {
                        db_helper.addTableColumn(AnnotationHandler.createTable(getClass()), key,"INTEGER");
                    }
                }
                continue;
            }
            break;
        }
    }


    //    .................updating record.............................................................
    public void update(){
        String key=null;
        Object value=null;
//      getAll all annotated fields
        Field[] fields=this.getClass().getFields();

        ContentValues cv=new ContentValues();

        for (Field f:fields){
//            if a dbmodel
            if(AnnotationHandler.isDBModel(f)){
                try {
                    AndroidModel androidModel=(AndroidModel) f.get(this);
                    Field prim_field=AndroidModel.getPrimaryField(androidModel.getClass());
                    if (androidModel!=null){
                        cv.put(androidModel.getClass().getSimpleName()+prim_field.getName(),
                                prim_field.get(androidModel).toString());
                        androidModel.update();
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

//           a db column
            if(AnnotationHandler.isAttribute(f)){
                try {
                    cv.put(AnnotationHandler.getColumnName(f),(f.get(this)).toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                if(AnnotationHandler.isPrimary(f)){
                    try {
                        key = f.getName();
                        value = f.get(this);
                    }
                    catch(IllegalAccessException e){

                    }
                }
            }
        }
        DB_Helper db_helper=DB_Helper.getInstance(context);



        while(true) {
            try {
                db_helper.updateRecord(AnnotationHandler.getTableName(getClass()), cv,key,value);
            }
            catch (SQLiteConstraintException e){
                e.printStackTrace();
//                Log.e("ORM","Duplicate entry for same primary key");
            }
            catch (SQLiteException e){
                e.printStackTrace();
//                if(e.getMessage().split(":")[0].equals("no such table")) {
//                    db_helper.createTable(annotationHandler.createTable(getClass()));
//                    continue;
//                }
            }
            break;
        }
    }



//..................retrieving data back from database.............................................
    public static <T>T getFirst(Class<T> clas,Context context){
        return (T)DB_Helper.getInstance(context).readFirstRecord(clas,
                AnnotationHandler.createTable(clas));

    }

    public static <T>ArrayList<T> getAll(Class<T> clas, Context context){
        return DB_Helper.getInstance(context).readAllRecords(clas,
                AnnotationHandler.createTable(clas));

    }
    public static <T>ArrayList<T> get(Class<T> clas, Context context,String key,Object value){
        return DB_Helper.getInstance(context).readRecords(clas,
                AnnotationHandler.createTable(clas),key,value);
    }

//...................delete a model................................................................
    public static void delete(Class<?> clas, Context context,String key,Object value){
        DB_Helper.getInstance(context).
                deleteRecords(AnnotationHandler.createTable(clas).getName(),key,value);
    }

}
