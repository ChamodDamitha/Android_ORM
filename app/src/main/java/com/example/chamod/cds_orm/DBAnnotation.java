package com.example.chamod.cds_orm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by chamod on 4/1/17.
 */

public class DBAnnotation {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface TableName {
        String table_name();
    }

//    to consider as a DB column
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface DBColumn {}


//      to specify the column name
  @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface ColumnName{
        String column_name();
    }

//    to specify the data type
  @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface DataType{
        String data_type();
    }

//    to specify as a primary key
  @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface PrimaryKey{}

//  to specify a AndroidModel object reference which needs to be saved in the db as well
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface DBModel{}

//  to specify a AndroidModel object list reference which needs to be saved in the db as well
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface DBModelList{}

}
