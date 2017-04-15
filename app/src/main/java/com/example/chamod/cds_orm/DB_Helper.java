package com.example.chamod.cds_orm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
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

        int i=1;
        for (Attribute a:dbTable.getAttributes()
             ) {
            if(i!=1){
                query+=",";
            }
            query+=a.getName()+" "+a.getType();
            if(a.isPrimary()){
                query+=" PRIMARY KEY";
            }
            i++;
        }

//        for(ForeignKey foreignKey:dbTable.getForeignKeys()){
//            query+=","+foreignKey.getName()+" "+foreignKey.getType();
//        }

        query+=");";

        ///////////////////////////////////
        Log.e("ORM",query);

        db.execSQL(query);
        db.close();
    }

    public void addTableColumn(DBTable dbTable,String column_name,String datatype){
        SQLiteDatabase db=getWritableDatabase();
        String query = "ALTER TABLE "+dbTable.getName()+" ADD COLUMN "+column_name+" "+datatype+";";
        db.execSQL(query);
        ////////////////////////////////
        Log.e("ORM",query);
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

//    ........................Insert a record.......................................................

    public void insertRecord(String tableName,ContentValues contentValues){
        SQLiteDatabase db=getWritableDatabase();
        db.insertOrThrow(tableName,null,contentValues);
        db.close();
    }

    public void saveModel(AndroidModel model){

        DB_Helper db_helper=DB_Helper.getInstance(context);


        Field primary_field=null,android_model_field=null,android_modellist_field=null;
//      getAll all annotated fields
        Field[] fields=model.getClass().getFields();

        ContentValues cv=new ContentValues();

        for (Field f:fields){

//            if a model object is associated
            if(AnnotationHandler.isDBModel(f)){
                android_model_field=f;
            }
//            if a modellist
            else if (AnnotationHandler.isDBModelList(f)){
                android_modellist_field=f;
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

                    cv.put(AnnotationHandler.getColumnName(f),(f.get(model)).toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        while(true) {
            try {
                db_helper.insertRecord(AnnotationHandler.getTableName(model.getClass()), cv);

//           if primary key is auto incremented
                int id=db_helper.getMaxId(AnnotationHandler.createTable(model.getClass()).getName(),primary_field.getName());

                if(id!=-1) {
                    primary_field.set(model,id);

                    if(android_model_field!=null){
                        try {
                            AndroidModel androidModel=(AndroidModel) android_model_field.get(model);
                            saveModelWithExtraValue(androidModel,model.getClass().getSimpleName()+
                                    AndroidModel.getPrimaryField(model.getClass()).getName(),id);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    if(android_modellist_field!=null){

                        ArrayList<AndroidModel> androidModels =(ArrayList<AndroidModel>)android_modellist_field.get(model);
                        for (AndroidModel androidModel:androidModels){
                            saveModelWithExtraValue(androidModel,model.getClass().getSimpleName()+
                                    AndroidModel.getPrimaryField(model.getClass()).getName(),id);
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
                db_helper.createTable(AnnotationHandler.createTable(model.getClass()));
//                }
                continue;
            }
            break;
        }
    }

    private void saveModelWithExtraValue(AndroidModel model, String key, Object value) {

        DB_Helper db_helper=DB_Helper.getInstance(context);


        Field primary_field=null,android_model_field=null,android_modellist_field=null;
//      getAll all annotated fields
        Field[] fields=model.getClass().getFields();

        ContentValues cv=new ContentValues();

        for (Field f:fields){

//            if a model object is associated
            if(AnnotationHandler.isDBModel(f)){
                android_model_field=f;
            }
//            if a modellist
            else if (AnnotationHandler.isDBModelList(f)){
                android_modellist_field=f;
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

                    cv.put(AnnotationHandler.getColumnName(f),(f.get(model)).toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
//      set extra value
        cv.put(key,value.toString());


        while(true) {
            try {
                db_helper.insertRecord(AnnotationHandler.getTableName(model.getClass()), cv);
//           if primary key is auto incremented
                int id=db_helper.getMaxId(AnnotationHandler.createTable(model.getClass()).getName(),primary_field.getName());
                if(id!=-1) {
                    primary_field.set(model,id);

                    if(android_model_field!=null){
                        try {
                            AndroidModel androidModel=(AndroidModel) android_model_field.get(model);
                            saveModelWithExtraValue(androidModel,model.getClass().getSimpleName()+
                                    AndroidModel.getPrimaryField(model.getClass()).getName(),id);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    if(android_modellist_field!=null){
                        ArrayList<AndroidModel> androidModels =(ArrayList<AndroidModel>)android_modellist_field.get(model);
                        for (AndroidModel androidModel:androidModels){
                            saveModelWithExtraValue(androidModel,model.getClass().getSimpleName()+
                                    AndroidModel.getPrimaryField(model.getClass()).getName(),id);
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
                    db_helper.createTable(AnnotationHandler.createTable(model.getClass()));
                }
                else{
                    if(value.getClass().equals(String.class)) {
                        db_helper.addTableColumn(AnnotationHandler.createTable(model.getClass()), key,"TEXT");
                    }
                    else if(value.getClass().equals(Integer.class) || value.getClass().equals(int.class)) {
                        db_helper.addTableColumn(AnnotationHandler.createTable(model.getClass()), key,"INTEGER");
                    }
                }
                continue;
            }
            break;
        }
    }




    //    ..........................Read records........................................................
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

    private Object getReturnObject(Class<?> clas,DBTable dbTable,Cursor cursor){
        Object object = getInstanceOf(clas);
//        set attributes
        for (Attribute attribute : dbTable.getAttributes()
                ) {
            try {
                Field f = clas.getField(attribute.getName());
                setObjectFieldValue(object,f,cursor);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

        }

//                set foreign key refered objects
        for (ForeignKey foreignKey:dbTable.getForeignKeys()){
            Object sub_object=null;

//            if (foreignKey.getRef_Data_Class().equals(String.class)) {
//                sub_object=readRecords(foreignKey.getRef_class(),AnnotationHandler.createTable(foreignKey.getRef_class()),
//                        foreignKey.getField_name(),cursor.getString(cursor.getColumnIndex(foreignKey.getName())));
//            } else if (foreignKey.getRef_Data_Class().equals(Integer.class) || foreignKey.getRef_Data_Class().equals(int.class)) {
//                sub_object=readRecords(foreignKey.getRef_class(),AnnotationHandler.createTable(foreignKey.getRef_class()),
//                        foreignKey.getField_name(),cursor.getInt(cursor.getColumnIndex(foreignKey.getName())));
//            }
//
              Field prim_field=AndroidModel.getPrimaryField(clas);


              if(prim_field.getType().equals(String.class)){
                  sub_object=AndroidModel.get(foreignKey.getRef_class(),context,
                          foreignKey.getField_name(),cursor.getString(cursor.getColumnIndex(prim_field.getName())));
              }
              else if(prim_field.getType().equals(Integer.class) || prim_field.getType().equals(int.class)){
                  sub_object=AndroidModel.get(foreignKey.getRef_class(),context,
                          foreignKey.getField_name(),cursor.getInt(cursor.getColumnIndex(prim_field.getName())));
              }

            try {

                Field f = clas.getField(foreignKey.getRef_name());
                if(f.getType().equals(ArrayList.class)){
                    f.set(object, getObjectList(foreignKey.getRef_class(), (ArrayList) sub_object));
                }
                else {
                    f.set(object, getObjectList(foreignKey.getRef_class(), (ArrayList) sub_object).get(0));
                }
            } catch (NoSuchFieldException|IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    private <T>ArrayList<T> getObjectList(Class<T> clas, ArrayList object){
        return object;
    }

    private void setObjectFieldValue(Object object,Field f,Cursor cursor){
        try {
            if (f.getType().equals(String.class)) {
                f.set(object, cursor.getString(cursor.getColumnIndex(AnnotationHandler.getColumnName(f))));
            } else if (f.getType().equals(int.class) || f.getType().equals(Integer.class)) {
                f.set(object, cursor.getInt(cursor.getColumnIndex(AnnotationHandler.getColumnName(f))));
            }
        }
        catch (IllegalAccessException e){

        }
    }

    public Object readFirstRecord(Class<?> clas, DBTable dbTable){
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + dbTable.getName() + " LIMIT 1;";
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            Object object = getReturnObject(clas,dbTable,cursor);
            return object;
        }
        return null;
    }

    public <T>ArrayList<T> readAllRecords(Class<T> clas, DBTable dbTable){
        ArrayList<T> objects=new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + dbTable.getName() + ";";
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            Object object = getReturnObject(clas,dbTable,cursor);
            objects.add((T) object);
            db.close();
        }
        return objects;
    }

    public <T>ArrayList<T> readRecords(Class<T> clas, DBTable dbTable,String key,Object value){
        ArrayList<T> objects=new ArrayList<>();

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
            Object object = getReturnObject(clas,dbTable,cursor);
            objects.add((T) object);
            db.close();
        }
        return objects;
    }



//    ............................update a record...................................................

    public void updateModel(AndroidModel model){
        String key=null;
        Object value=null;
//      getAll all annotated fields
        Field[] fields=model.getClass().getFields();

        ContentValues cv=new ContentValues();

        for (Field f:fields){
//            if a dbmodel
            if(AnnotationHandler.isDBModel(f)){
                try {
                    AndroidModel androidModel=(AndroidModel) f.get(model);
                    Field prim_field=AndroidModel.getPrimaryField(androidModel.getClass());
                    if (androidModel!=null){
//                        cv.put(androidModel.getClass().getSimpleName()+prim_field.getName(),
//                                prim_field.get(androidModel).toString());
                        androidModel.update();
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
//            if a dbmodel list
            if(AnnotationHandler.isDBModelList(f)){
                try {
                    ArrayList<AndroidModel> androidModels =(ArrayList<AndroidModel>)f.get(model);
                    for (AndroidModel androidModel:androidModels){
                        androidModel.update();
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

//           a db column
            if(AnnotationHandler.isAttribute(f)){
                try {
                    cv.put(AnnotationHandler.getColumnName(f),(f.get(model)).toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                if(AnnotationHandler.isPrimary(f)){
                    try {
                        key = f.getName();
                        value = f.get(model);
                    }
                    catch(IllegalAccessException e){

                    }
                }
            }
        }

        while(true) {
            try {
                updateRecord(AnnotationHandler.getTableName(model.getClass()), cv,key,value);
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


    private void updateRecord(String tableName,ContentValues cv,String key,Object value){
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

//................................delete a record...................................................

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
