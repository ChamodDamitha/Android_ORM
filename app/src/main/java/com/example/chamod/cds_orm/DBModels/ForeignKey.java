package com.example.chamod.cds_orm.DBModels;

import com.example.chamod.cds_orm.AnnotationHandler;

/**
 * Created by chamod on 4/14/17.
 */

public class ForeignKey{
    private Class<?> ref_class;
    private String ref_name;

    public ForeignKey(Class<?> ref_class,String ref_name) {
        this.ref_class=ref_class;
        this.ref_name=ref_name;
    }


    public String getName() {
        DBTable dbTable=AnnotationHandler.createTable(ref_class);
        return ref_class.getSimpleName()+dbTable.getPrimaryAttribute().getName();
    }

    public String getType() {
        DBTable dbTable=AnnotationHandler.createTable(ref_class);
        return dbTable.getPrimaryAttribute().getType();
    }

    public Class<?> getRef_Data_Class() {
        DBTable dbTable=AnnotationHandler.createTable(ref_class);
        return dbTable.getPrimaryAttribute().getClaz();
    }

    public String getField_name() {
        DBTable dbTable=AnnotationHandler.createTable(ref_class);
        return dbTable.getPrimaryAttribute().getName();
    }

    public String getRef_name(){
        return this.ref_name;
    }

    public Class<?> getRef_class() {
        return ref_class;
    }
}
