package com.example.chamod.cds_orm.DBModels;

import com.example.chamod.cds_orm.AnnotationHandler;

/**
 * Created by chamod on 4/14/17.
 */

public class ForeignKey{
    private String field_name;
    private Class<?> ref_class;
    private AnnotationHandler annotationHandler;

    public ForeignKey(Class<?> ref_class,String field_name) {
        this.ref_class=ref_class;
        this.field_name=field_name;
        this.annotationHandler=AnnotationHandler.getInstance();
    }


    public String getName() {
        DBTable dbTable=annotationHandler.createTable(ref_class);
        return dbTable.getName()+dbTable.getPrimaryAttribute().getName();
    }

    public String getType() {
        DBTable dbTable=annotationHandler.createTable(ref_class);
        return dbTable.getPrimaryAttribute().getType();
    }

    public Class<?> getRef_class() {
        return ref_class;
    }

    public String getField_name() {
        return field_name;
    }
}
