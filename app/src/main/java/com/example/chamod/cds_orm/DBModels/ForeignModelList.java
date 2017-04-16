package com.example.chamod.cds_orm.DBModels;

import java.lang.reflect.Field;

/**
 * Created by chamod on 4/16/17.
 */

public class ForeignModelList {
    private Class<?> model_claz;
    private Field field;
    private String col_name;

    public ForeignModelList(Class<?> model_claz, Field field, String col_name) {
        this.model_claz = model_claz;
        this.field = field;
        this.col_name = col_name;
    }

    public Class<?> getModel_claz() {
        return model_claz;
    }

    public Field getField() {
        return field;
    }

    public String getCol_name() {
        return col_name;
    }
}
