package com.example.chamod.cds_orm;

import android.util.Log;

import com.example.chamod.cds_orm.DBModels.Attribute;
import com.example.chamod.cds_orm.DBModels.DBTable;
import com.example.chamod.cds_orm.DBModels.ForeignKey;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by chamod on 4/2/17.
 */

public class AnnotationHandler {

    public static DBTable createTable(Class<?> clas){
        DBTable dbTable;

//      getAll all annotated fields
        Field[] fields=clas.getFields();
        DBAnnotation.TableName tableName =clas.getAnnotation(DBAnnotation.TableName.class);
        if(tableName!=null) {
//            create a table
            dbTable=new DBTable(tableName.table_name());

            for (Field f:fields){
//              if a DBModel
                if(f.getAnnotation(DBAnnotation.DBModel.class)!=null){
                    dbTable.addForeignKey(new ForeignKey(clas,f.getType(),getColumnName(f)));
                }
//                if a DBModellist
                if(f.getAnnotation(DBAnnotation.DBModelList.class)!=null){
                    DBAnnotation.DBModelList dbModelListAnnot=f.getAnnotation(DBAnnotation.DBModelList.class);
                    dbTable.addForeignKey(new ForeignKey(clas,dbModelListAnnot.model_class(),getColumnName(f)));
                }
//              a db column
                if(f.getAnnotation(DBAnnotation.DBColumn.class)!=null){
                    dbTable.addAttribute(new Attribute(getColumnName(f),getDataType(f),isPrimary(f),f.getType()));
                }
            }

            return dbTable;
        }
        else{
            Log.e("ORM","Please specify a table name using @TableName annotation");
            return null;
        }
    }

    public  static String getTableName(Class<?> clas){
        String table_name=clas.getAnnotation(DBAnnotation.TableName.class).table_name();
        if(table_name!=null){
            return table_name;
        }
        return clas.getName();
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

        return  null;
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
