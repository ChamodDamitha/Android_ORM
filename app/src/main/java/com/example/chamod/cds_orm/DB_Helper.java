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
import com.example.chamod.cds_orm.DBModels.DetailModel;
import com.example.chamod.cds_orm.DBModels.ForeignModel;
import com.example.chamod.cds_orm.DBModels.ForeignModelList;
import com.example.chamod.cds_orm.ExampleClasses.Book;

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
        String del_query="DROP TABLE IF EXISTS "+dbTable.getTable_name()+";";

        SQLiteDatabase db=getWritableDatabase();

        db.execSQL(del_query);

        Log.e("ORM","table creating ..........");

        String query="CREATE TABLE "+dbTable.getTable_name()+"(";

        query += dbTable.getPrimary_attribute().getField().getName() + " " +
                dbTable.getPrimary_attribute().getDb_data_type() + " PRIMARY KEY ";

        for (Attribute a:dbTable.getAttributes()
             ) {
            query += "," + a.getField().getName()+" "+a.getDb_data_type();
        }

        query+=");";

        ///////////////////////////////////
        Log.e("ORM",query);

        db.execSQL(query);
        db.close();
    }

    public void addTableColumn(DBTable dbTable,String column_name,String datatype){
        SQLiteDatabase db=getWritableDatabase();
        String query = "ALTER TABLE "+dbTable.getTable_name()+" ADD COLUMN "+column_name+" "+datatype+";";
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

    private void insertRecord(String tableName,ContentValues contentValues){
        SQLiteDatabase db=getWritableDatabase();
        db.insertOrThrow(tableName,null,contentValues);
        db.close();
    }

    private String getValueString(Object value){
        if(value.getClass().equals(boolean.class) || value.getClass().equals(Boolean.class)){
            boolean bool=(boolean)value;
            if(bool){
                return "1";
            }
            else {
                return "0";
            }
        }
        else {
            return value.toString();
        }
    }

    public boolean saveModel(AndroidModel model){
        if(model!=null) {
            DetailModel detailModel = AnnotationHandler.getDetailModel(model.getClass());
            if(detailModel==null){
                return false;
            }

            ContentValues cv = new ContentValues();
            try {

                if(!detailModel.getDbTable().getPrimary_attribute().isAuto_increment()) {
                    cv.put(detailModel.getDbTable().getPrimary_attribute().getField().getName(),
                            getValueString(detailModel.getDbTable().getPrimary_attribute().getField().get(model)));
                }

                for (Attribute a : detailModel.getDbTable().getAttributes()) {
                    cv.put(a.getField().getName(), getValueString(a.getField().get(model)));
                }

                while (true) {
                    try {
                        insertRecord(detailModel.getDbTable().getTable_name(), cv);
                    } catch (SQLiteException e) {
                        createTable(detailModel.getDbTable());
                        continue;
                    }
                    break;
                }

                Object id;
                if (detailModel.getDbTable().getPrimary_attribute().getField().getType().equals(Integer.class) ||
                        detailModel.getDbTable().getPrimary_attribute().getField().getType().equals(int.class)) {
                    id = getMaxId(detailModel.getDbTable().getTable_name(),
                            detailModel.getDbTable().getPrimary_attribute().getField().getName());
                } else {
                    id = detailModel.getDbTable().getPrimary_attribute().getField().get(model);
                }

                detailModel.getDbTable().getPrimary_attribute().getField().set(model,id);

//                set temp_id
                model.getClass().getField(Constants.TEMP_ID).set(model,id);

                for (ForeignModel foreignModel : detailModel.getForeignModels()) {
                    AndroidModel submodel = (AndroidModel) foreignModel.getField().get(model);
                    saveModelWithExtraValue(submodel, foreignModel.getCol_name(), id,AnnotationHandler.getDBDataType(id));
                }
                for (ForeignModelList foreignModelList : detailModel.getForeignModelLists()) {
                    ArrayList<AndroidModel> submodels = (ArrayList<AndroidModel>) foreignModelList.getField().get(model);
                    for (AndroidModel submodel : submodels) {
                        saveModelWithExtraValue(submodel, foreignModelList.getCol_name(), id,AnnotationHandler.getDBDataType(id));
                    }
                }
            } catch (IllegalAccessException e) {
                return false;
            } catch (SQLiteConstraintException e) {
                Log.e("ORM", detailModel.getDbTable().getTable_name() + " - Duplicate entry for same primary key");
                return false;
            }
            catch (NoSuchFieldException e){
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean saveModelWithExtraValue(AndroidModel model, String key, Object value,String key_db_data_type) {
        if(model!=null) {
            DetailModel detailModel = AnnotationHandler.getDetailModel(model.getClass());
            if(detailModel==null){
                return false;
            }

            ContentValues cv = new ContentValues();
            try {
                if(!detailModel.getDbTable().getPrimary_attribute().isAuto_increment()){
                    cv.put(detailModel.getDbTable().getPrimary_attribute().getField().getName(),
                            getValueString(detailModel.getDbTable().getPrimary_attribute().getField().get(model)));
                }
                for (Attribute a : detailModel.getDbTable().getAttributes()) {
                    cv.put(a.getField().getName(), getValueString(a.getField().get(model)));
                }
                cv.put(key,getValueString(value));

                while (true) {
                    try {
                        insertRecord(detailModel.getDbTable().getTable_name(), cv);
                    } catch (SQLiteConstraintException e){
                        Log.e("ORM", detailModel.getDbTable().getTable_name() + " - Duplicate entry for same primary key");
                        return false;
                    }
                    catch (SQLiteException e) {
                        if (e.getMessage().split(":")[0].equals("no such table")) {
                            createTable(detailModel.getDbTable());
                        }
                        else {
                            addTableColumn(detailModel.getDbTable(), key, key_db_data_type);
                        }
                        continue;
                    }

                    break;
                }

                Object id;
                if (detailModel.getDbTable().getPrimary_attribute().getField().getType().equals(Integer.class) ||
                        detailModel.getDbTable().getPrimary_attribute().getField().getType().equals(int.class)) {
                    id = getMaxId(detailModel.getDbTable().getTable_name(),
                            detailModel.getDbTable().getPrimary_attribute().getField().getName());
                } else {
                    id = detailModel.getDbTable().getPrimary_attribute().getField().get(model);
                }


                detailModel.getDbTable().getPrimary_attribute().getField().set(model,id);

//                set temp_id
                model.getClass().getField(Constants.TEMP_ID).set(model,id);


                for (ForeignModel foreignModel : detailModel.getForeignModels()) {
                    AndroidModel submodel = (AndroidModel) foreignModel.getField().get(model);
                    saveModelWithExtraValue(submodel, foreignModel.getCol_name(), id,key_db_data_type);
                }
                for (ForeignModelList foreignModelList : detailModel.getForeignModelLists()) {
                    ArrayList<AndroidModel> submodels = (ArrayList<AndroidModel>) foreignModelList.getField().get(model);
                    for (AndroidModel submodel : submodels) {
                        saveModelWithExtraValue(submodel, foreignModelList.getCol_name(), id,key_db_data_type);
                    }
                }
            } catch (IllegalAccessException e) {
                return false;
            }
            catch (NoSuchFieldException e){
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
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

    private Object getReturnObject(Class<?> claz,DetailModel detailModel,Cursor cursor){
        Object object = getInstanceOf(claz);
//        set attributes
        setObjectFieldValue(object,detailModel.getDbTable().getPrimary_attribute().getField(),cursor);

        for (Attribute attribute : detailModel.getDbTable().getAttributes()
                ) {
            setObjectFieldValue(object,attribute.getField(),cursor);
        }
//      set foreign key refered objects
        try {
            Object id=detailModel.getDbTable().getPrimary_attribute().getField().get(object);

//            set temp_id
            claz.getField(Constants.TEMP_ID).set(object,id);

            for(ForeignModel foreignModel:detailModel.getForeignModels()){
                Object sub_object=AndroidModel.get(foreignModel.getField().getType(),context,
                        foreignModel.getCol_name(),id);

                ArrayList objectList= getObjectList(foreignModel.getField().getType(),(ArrayList) sub_object);
                if(objectList.size()==0){
                    foreignModel.getField().set(object,null);
                }
                else {
                    foreignModel.getField().set(object, objectList.get(0));
                }
            }
            for (ForeignModelList foreignModelList:detailModel.getForeignModelLists()){
                Object sub_object=AndroidModel.get(foreignModelList.getModel_claz(),context,
                        foreignModelList.getCol_name(),id);
                foreignModelList.getField().set(object,getObjectList(foreignModelList.getModel_claz(),
                        (ArrayList)sub_object));
            }

        }
        catch (Exception e) {
            Log.e("ORM","getreturnobj error - "+e.toString());
        }

        return object;
    }

    private <T>ArrayList<T> getObjectList(Class<T> clas, ArrayList object){
        if(object==null){
            return new ArrayList<>();
        }
        return object;
    }

    private void setObjectFieldValue(Object object,Field f,Cursor cursor){
        try {
            if (f.getType().equals(String.class)) {
                f.set(object, cursor.getString(cursor.getColumnIndex(AnnotationHandler.getColumnName(f))));
            } else if (f.getType().equals(int.class) || f.getType().equals(Integer.class)) {
                f.set(object, cursor.getInt(cursor.getColumnIndex(AnnotationHandler.getColumnName(f))));
            }else if (f.getType().equals(float.class) || f.getType().equals(Float.class)) {
                f.set(object, cursor.getFloat(cursor.getColumnIndex(AnnotationHandler.getColumnName(f))));
            }else if (f.getType().equals(double.class) || f.getType().equals(Double.class)) {
                f.set(object, cursor.getDouble(cursor.getColumnIndex(AnnotationHandler.getColumnName(f))));
            }else if (f.getType().equals(boolean.class) || f.getType().equals(Boolean.class)) {
                int bool=cursor.getInt(cursor.getColumnIndex(AnnotationHandler.getColumnName(f)));
                if(bool==0){
                    f.set(object, false);
                }
                else if(bool==1){
                    f.set(object,true);
                }
            }
        }
        catch (IllegalAccessException e){
            Log.e("ORM","setObject field error - "+e.toString());
        }
    }

    public Object readFirstRecord(Class<?> claz){
        SQLiteDatabase db = getReadableDatabase();

        DetailModel detailModel=AnnotationHandler.getDetailModel(claz);

        String query = "SELECT * FROM " + detailModel.getDbTable().getTable_name() + " LIMIT 1;";
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            Object object = getReturnObject(claz,detailModel,cursor);
            return object;
        }
        return null;
    }

    public <T>ArrayList<T> readAllRecords(Class<T> claz){
        ArrayList<T> objects=new ArrayList<>();
        DetailModel detailModel=AnnotationHandler.getDetailModel(claz);
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + detailModel.getDbTable().getTable_name() + ";";
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            Object object = getReturnObject(claz,detailModel,cursor);
            objects.add((T) object);
        }
        db.close();
        return objects;
    }
//
    public <T>ArrayList<T> readRecords(Class<T> claz,String key,Object value){
        DetailModel detailModel=AnnotationHandler.getDetailModel(claz);

        ArrayList<T> objects=new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + detailModel.getDbTable().getTable_name()+" WHERE ";

        if(value.getClass().equals(String.class)){
            query+=key+"='"+value+"' ;";
        }
        else{
            query+=key+"="+value+" ;";
        }
        try {
            Cursor cursor = db.rawQuery(query, null);

            while (cursor.moveToNext()) {
                Object object = getReturnObject(claz,detailModel,cursor);
                objects.add((T) object);
                db.close();
            }
        }
        catch (SQLiteException e){
            return new ArrayList<>();
        }
        return objects;
    }


//
////    ............................update a record...................................................
//
    public boolean updateModel(AndroidModel model){
        if(model!=null){
            if(model.getTemp_id()==null){
                saveModel(model);
            }
            else{
                DetailModel detailModel=AnnotationHandler.getDetailModel(model.getClass());
                ContentValues cv=new ContentValues();
                try {
                    cv.put(detailModel.getDbTable().getPrimary_attribute().getField().getName(),
                            getValueString(detailModel.getDbTable().getPrimary_attribute().getField().get(model)));

                    for (Attribute a : detailModel.getDbTable().getAttributes()){
                        cv.put(a.getField().getName(),getValueString(a.getField().get(model)));
                    }
                    updateRecord(detailModel.getDbTable().getTable_name(),cv,
                            detailModel.getDbTable().getPrimary_attribute().getField().getName(),
                            model.getTemp_id());

                    Object id=detailModel.getDbTable().getPrimary_attribute().getField().get(model);

                    for(ForeignModel foreignModel:detailModel.getForeignModels()){
                        AndroidModel sub_model=(AndroidModel)foreignModel.getField().get(model);
//                            delete existing sub models
                        if(sub_model!=null && sub_model.getTemp_id()==null){
                            deleteModels(sub_model.getClass(),foreignModel.getCol_name(),model.getTemp_id());
                        }
                        updateModelWithExtraValue(sub_model,foreignModel.getCol_name(),id);
                    }

                    for(ForeignModelList foreignModelList:detailModel.getForeignModelLists()){
                        ArrayList<Object> sub_model_ids=new ArrayList<>();
                        boolean newModelAdded=false;
                        DetailModel detailModel1_sub_model=AnnotationHandler.getDetailModel(foreignModelList.
                                getModel_claz());
                        Field prim_field_sub_model=detailModel1_sub_model.getDbTable().getPrimary_attribute().getField();

                        ArrayList<AndroidModel> sub_models=(ArrayList<AndroidModel>)foreignModelList.getField().get(model);

                        for(AndroidModel sub_model:sub_models){
                            if(sub_model!=null && sub_model.getTemp_id()==null){
                                newModelAdded=true;
                            }
                            updateModelWithExtraValue(sub_model,foreignModelList.getCol_name(),id);
                            sub_model_ids.add(prim_field_sub_model.get(sub_model));
                        }
                        if(newModelAdded) {
                            deleteOtherModels(detailModel1_sub_model, sub_model_ids);
                        }
                    }


                    model.setTemp_id(id);
                    return true;
                }
                catch (IllegalAccessException e){
                    e.printStackTrace();
                    return false;
                }
            }
            return true;
        }
        else{
            return false;
        }
    }

    public boolean updateModelWithExtraValue(AndroidModel model,String key,Object value){
        if(model!=null){
            if(model.getTemp_id()==null){
                saveModelWithExtraValue(model,key,value,AnnotationHandler.getDBDataType(value));
            }
            else{
                DetailModel detailModel=AnnotationHandler.getDetailModel(model.getClass());
                ContentValues cv=new ContentValues();
                try {
                    cv.put(detailModel.getDbTable().getPrimary_attribute().getField().getName(),
                            getValueString(detailModel.getDbTable().getPrimary_attribute().getField().get(model)));

                    for (Attribute a : detailModel.getDbTable().getAttributes()){
                        cv.put(a.getField().getName(),getValueString(a.getField().get(model)));
                    }

                    cv.put(key,getValueString(value));

                    updateRecord(detailModel.getDbTable().getTable_name(),cv,
                            detailModel.getDbTable().getPrimary_attribute().getField().getName(),
                            model.getTemp_id());

                    Object id=detailModel.getDbTable().getPrimary_attribute().getField().get(model);

                    for(ForeignModel foreignModel:detailModel.getForeignModels()){
                        AndroidModel sub_model=(AndroidModel)foreignModel.getField().get(model);
//                      delete existing sub models
                        if(sub_model!=null && sub_model.getTemp_id()==null){
                            deleteModels(sub_model.getClass(),foreignModel.getCol_name(),model.getTemp_id());
                        }
                        updateModelWithExtraValue(sub_model,foreignModel.getCol_name(),id);
                    }

                    for(ForeignModelList foreignModelList:detailModel.getForeignModelLists()){
                        ArrayList<Object> sub_model_ids=new ArrayList<>();
                        boolean newModelAdded=false;
                        DetailModel detailModel1_sub_model=AnnotationHandler.getDetailModel(foreignModelList.
                                getModel_claz());
                        Field prim_field_sub_model=detailModel1_sub_model.getDbTable().getPrimary_attribute().getField();

                        ArrayList<AndroidModel> sub_models=(ArrayList<AndroidModel>)foreignModelList.getField().get(model);
                        for(AndroidModel sub_model:sub_models){
                           if(sub_model!=null && sub_model.getTemp_id()==null){
                                newModelAdded=true;
                            }
                            updateModelWithExtraValue(sub_model,foreignModelList.getCol_name(),id);
                            sub_model_ids.add(prim_field_sub_model.get(sub_model));
                        }
                        if(newModelAdded){
                            deleteOtherModels(detailModel1_sub_model,sub_model_ids);
                        }
                    }

                    model.setTemp_id(id);
                    return true;
                }
                catch (IllegalAccessException e){
                    e.printStackTrace();
                    return false;
                }
            }
            return true;
        }
        else{
            return false;
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

//        Log.e("ORM",tableName+" -- "+whereClause);

        db.update(tableName, cv, whereClause, null);
        db.close();
    }

    private void deleteOtherModels(DetailModel detailModel,ArrayList<Object> sub_model_ids) {

        Field primary_field=detailModel.getDbTable().getPrimary_attribute().getField();

        SQLiteDatabase db=getReadableDatabase();

        String query="SELECT "+primary_field.getName()+" FROM "+
                detailModel.getDbTable().getTable_name()+";";


        Cursor cursor=db.rawQuery(query,null);

        Object id=null;

        while (cursor.moveToNext()){

            if(primary_field.getType().equals(String.class)){
                id=(cursor.getString(cursor.getColumnIndex(primary_field.getName())));
            }
            else if(primary_field.getType().equals(Integer.class) || primary_field.getType().equals(int.class)){
                id=(cursor.getInt(cursor.getColumnIndex(primary_field.getName())));
            }
            else if(primary_field.getType().equals(Float.class) || primary_field.getType().equals(float.class)){
                id=(cursor.getFloat(cursor.getColumnIndex(primary_field.getName())));
            }
            else if(primary_field.getType().equals(Double.class) || primary_field.getType().equals(double.class)){
                id=(cursor.getDouble(cursor.getColumnIndex(primary_field.getName())));
            }
            if(sub_model_ids.indexOf(id)==-1) {
                deleteModels(detailModel.getClaz(), primary_field.getName(), id);
            }
        }

    }
//
//////................................delete a record...................................................

    public void deleteModels(Class<?> claz, String key,Object value){
        DetailModel detailModel=AnnotationHandler.getDetailModel(claz);

        ArrayList<Object> ids=deleteRecords(detailModel.getDbTable(),key,value);

        for(Object id:ids) {
            for (ForeignModel foreignModel : detailModel.getForeignModels()) {
                deleteModels(foreignModel.getField().getType(), foreignModel.getCol_name(), id);
            }

            for (ForeignModelList foreignModelList : detailModel.getForeignModelLists()) {
                deleteModels(foreignModelList.getModel_claz(), foreignModelList.getCol_name(), id);
            }
        }

    }

    private ArrayList<Object> deleteRecords(DBTable dbTable, String key, Object value){
        SQLiteDatabase db=getReadableDatabase();
        String query;

        ArrayList<Object> ids=new ArrayList<>();

        Field prim_field=dbTable.getPrimary_attribute().getField();

        if(value.getClass().equals(String.class)) {
            query = "SELECT " + prim_field.getName()+" FROM " + dbTable.getTable_name()
                    + " WHERE " +key + " = '"+value+"' ;";
        }
        else{
            query = "SELECT " + prim_field.getName() +" FROM "+ dbTable.getTable_name()
                    + " WHERE " +key + " = "+value+" ;";
        }
        Cursor cursor=db.rawQuery(query,null);
        while(cursor.moveToNext()){
            if(prim_field.getType().equals(int.class) || prim_field.getType().equals(Integer.class) ||
                    prim_field.getType().equals(boolean.class) || prim_field.getType().equals(Boolean.class)){
                ids.add(cursor.getInt(cursor.getColumnIndex(dbTable.getPrimary_attribute().getField().getName())));
            }
            else if(prim_field.getType().equals(double.class) || prim_field.getType().equals(Double.class)){
                ids.add(cursor.getDouble(cursor.getColumnIndex(dbTable.getPrimary_attribute().getField().getName())));
            }
            else if(prim_field.getType().equals(float.class) || prim_field.getType().equals(Float.class)){
                ids.add(cursor.getFloat(cursor.getColumnIndex(dbTable.getPrimary_attribute().getField().getName())));
            }
            else if(prim_field.getType().equals(String.class)){
                ids.add(cursor.getString(cursor.getColumnIndex(dbTable.getPrimary_attribute().getField().getName())));
            }
        }

        db=getWritableDatabase();

        String whereClause;
        if(value.getClass().equals(String.class)){
            whereClause=key+" = '"+value.toString()+"'";
        }
        else{
            whereClause=key+" = "+value.toString();
        }
        db.delete(dbTable.getTable_name(),whereClause,null);
        db.close();

        return ids;
    }

    public void deleteAllModels(Class<?> claz){
        DetailModel detailModel=AnnotationHandler.getDetailModel(claz);

        ArrayList<Object> ids=deleteAllRecords(detailModel.getDbTable());

        for(Object id:ids) {
            for (ForeignModel foreignModel : detailModel.getForeignModels()) {
                deleteModels(foreignModel.getField().getType(), foreignModel.getCol_name(), id);
            }

            for (ForeignModelList foreignModelList : detailModel.getForeignModelLists()) {
                deleteModels(foreignModelList.getModel_claz(), foreignModelList.getCol_name(), id);
            }
        }

    }

    private ArrayList<Object> deleteAllRecords(DBTable dbTable){
        SQLiteDatabase db=getReadableDatabase();
        String query;

        Field prim_field=dbTable.getPrimary_attribute().getField();

        ArrayList<Object> ids=new ArrayList<>();
         query = "SELECT " + prim_field.getName()+" FROM " + dbTable.getTable_name()+ ";";

        Cursor cursor=db.rawQuery(query,null);
        while(cursor.moveToNext()){
            if(prim_field.getType().equals(int.class) || prim_field.getType().equals(Integer.class) ||
                    prim_field.getType().equals(boolean.class) || prim_field.getType().equals(Boolean.class)){
                ids.add(cursor.getInt(cursor.getColumnIndex(dbTable.getPrimary_attribute().getField().getName())));
            }
            else if(prim_field.getType().equals(double.class) || prim_field.getType().equals(Double.class)){
                ids.add(cursor.getDouble(cursor.getColumnIndex(dbTable.getPrimary_attribute().getField().getName())));
            }
            else if(prim_field.getType().equals(float.class) || prim_field.getType().equals(Float.class)){
                ids.add(cursor.getFloat(cursor.getColumnIndex(dbTable.getPrimary_attribute().getField().getName())));
            }
            else if(prim_field.getType().equals(String.class)){
                ids.add(cursor.getString(cursor.getColumnIndex(dbTable.getPrimary_attribute().getField().getName())));
            }
        }

        db=getWritableDatabase();

        db.delete(dbTable.getTable_name(),null,null);
        db.close();

        return ids;
    }
}
