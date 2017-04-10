package com.example.chamod.cds_orm;

import android.content.Context;
import android.util.Log;

import com.example.chamod.cds_orm.DBModels.Attribute;
import com.example.chamod.cds_orm.DBModels.DBTable;

import java.lang.reflect.Field;

/**
 * Created by chamod on 4/2/17.
 */

public class AnnotationHandler {

    private final String Error_TAG="ORM_Exception";
    private final String Success_TAG="ORM";

    private static AnnotationHandler annotationHandler=null;

    private DB_Helper db_helper;

    private AnnotationHandler(Context context){
        db_helper=DB_Helper.getInstance(context);
    }

    public static AnnotationHandler getInstance(Context context){
        if(annotationHandler==null){
            annotationHandler=new AnnotationHandler(context);
        }
        return annotationHandler;
    }

    public DBTable createTable(Class<?> clas){
        DBTable dbTable;

//      getAll all annotated fields
        Field[] fields=clas.getFields();
        DBAnnotation.TableName tableName =clas.getAnnotation(DBAnnotation.TableName.class);
        if(tableName!=null) {
//            create a table
            dbTable=new DBTable(tableName.table_name());

            for (Field f:fields){
//           a db column
                if(f.getAnnotation(DBAnnotation.DBColumn.class)!=null){
                    dbTable.addAttribute(new Attribute(getColumnName(f),getDataType(f),isPrimary(f)));
                }
            }

            return dbTable;
        }
        else{
            Log.e(Error_TAG,"Please specify a table name using @TableName annotation");
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
}
