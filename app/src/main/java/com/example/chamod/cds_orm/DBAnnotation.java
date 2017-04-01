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
    @interface TableName {
        String table_name();
    }

//    to consider as a DB column
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface DBColumn {}



  @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface ColumnName{
        String column_name();
    }

  @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface DataType{
        String data_type();
    }

}
