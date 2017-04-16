package com.example.chamod.cds_orm;

import android.util.Log;

import com.example.chamod.cds_orm.DBModels.Attribute;
import com.example.chamod.cds_orm.DBModels.DBTable;
import com.example.chamod.cds_orm.DBModels.DetailModel;
import com.example.chamod.cds_orm.DBModels.ForeignModel;
import com.example.chamod.cds_orm.DBModels.ForeignModelList;

import java.lang.reflect.Field;

/**
 * Created by chamod on 4/2/17.
 */

public class AnnotationHandler {

    public static DetailModel getDetailModel(Class<?> claz){
        DetailModel detailModel=new DetailModel(claz);

        DBTable dbTable=new DBTable(getTableName(claz));

        Field[] fields=claz.getFields();

        boolean primary_key_available=false,have_columns=false;

        for (Field f:fields){
//            if a DBModel
            if(f.getAnnotation(DBAnnotation.DBModel.class)!=null){
                detailModel.addForeignModel(new ForeignModel(f,getForeignColName(claz)));
            }
//            if a DBModellist
            if(f.getAnnotation(DBAnnotation.DBModelList.class)!=null){
                DBAnnotation.DBModelList dbModelListAnnot=f.getAnnotation(DBAnnotation.DBModelList.class);

                detailModel.addForeignModelList(new ForeignModelList(dbModelListAnnot.model_class(),
                        f,getForeignColName(claz)));
            }
//              a db column
            if(f.getAnnotation(DBAnnotation.DBColumn.class)!=null){
                have_columns=true;
                if(f.getAnnotation(DBAnnotation.PrimaryKey.class)!=null){
                    primary_key_available=true;
                    if(isAutoIncrement(f)) {
                        dbTable.setPrimary_attribute(new Attribute(f, getDataType(f),true));
                    }
                    else{
                        dbTable.setPrimary_attribute(new Attribute(f, getDataType(f),false));
                    }
                }
                else {
                    dbTable.addAttribute(new Attribute(f, getDataType(f),false));
                }
            }
        }

        detailModel.setDbTable(dbTable);

        if(!have_columns){
            Log.e("ORM","Please specify DBColumn fields for class - "+claz.getSimpleName());
            return null;
        }
        if(!primary_key_available){
            Log.e("ORM","Please specify a primary key field for class - "+claz.getSimpleName());
            return null;
        }
        return detailModel;

    }


    private static String getForeignColName(Class<?> claz){
        return claz.getSimpleName()+getPrimaryField(claz).getName();
    }

    private static Field getPrimaryField(Class<?> claz){
        Field[] fields=claz.getFields();
        for(Field f:fields) {
            if (isPrimary(f)) {
                return f;
            }
        }
        return null;
    }



    public static String getTableName(Class<?> clas){
        DBAnnotation.TableName table_name=clas.getAnnotation(DBAnnotation.TableName.class);
        if(table_name!=null){
            return table_name.table_name();
        }
        return clas.getSimpleName();
    }

    public static boolean isAttribute(Field f){
        if(f.getAnnotation(DBAnnotation.DBColumn.class)!=null){
            return true;
        }
        return false;
    }

    public static String getDataType(Field f){
        DBAnnotation.DataType dataType=f.getAnnotation(DBAnnotation.DataType.class);
        if(dataType!=null){
            return dataType.data_type();
        }
        if(f.getType().equals(int.class) || f.getType().equals(Integer.class)){
            return "INTEGER";
        }
        if(f.getType().equals(String.class)){
            return "TEXT";
        }
        if(f.getType().equals(Float.class) || f.getType().equals(float.class)){
            return "FLOAT";
        }
        if(f.getType().equals(double.class) || f.getType().equals(Double.class)){
            return "DOUBLE";
        }
        if(f.getType().equals(boolean.class) || f.getType().equals(Boolean.class)){
            return "INT";
        }
        return  null;
    }

    public static String getDBDataType(Object value){
        if(value.getClass().equals(int.class) || value.getClass().equals(Integer.class)){
            return "INTEGER";
        }
        if(value.getClass().equals(String.class)){
            return "TEXT";
        }
        if(value.getClass().equals(Float.class) || value.getClass().equals(float.class)){
            return "FLOAT";
        }
        if(value.getClass().equals(double.class) || value.getClass().equals(Double.class)){
            return "DOUBLE";
        }
        if(value.getClass().equals(boolean.class) || value.getClass().equals(Boolean.class)){
            return "INT";
        }
        return  null;
    }

    public static boolean isAutoIncrement(Field f){
        if(f.getAnnotation(DBAnnotation.AutoIncrement.class)!=null){
            if(getDataType(f).equals(Constants.DB_INTEGER_TYPE)){
                return true;
            }
            return false;
        }
        return false;
    }

    public static String getColumnName(Field f){
        DBAnnotation.ColumnName columnName=f.getAnnotation(DBAnnotation.ColumnName.class);
        if(columnName!=null){
            return columnName.column_name();
        }
        return f.getName();
    }

    public static boolean isPrimary(Field f) {
        if(f.getAnnotation(DBAnnotation.PrimaryKey.class)!=null){
            return true;
        }
        return false;
    }

    public static boolean isDBModel(Field f){
        if(f.getAnnotation(DBAnnotation.DBModel.class)!=null){
            return true;
        }
        return false;
    }

    public static boolean isDBModelList(Field f){
        if(f.getAnnotation(DBAnnotation.DBModelList.class)!=null){
            return true;
        }
        return false;
    }

}
