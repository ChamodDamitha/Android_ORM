package com.example.chamod.cds_orm.DBModels;

import java.lang.reflect.Field;

/**
 * Created by chamod on 4/16/17.
 */

public class ForeignModel {
    private Field field;
    private String col_name;

    public ForeignModel(Field field, String col_name) {
        this.field = field;
        this.col_name = col_name;
    }

    public Field getField() {
        return field;
    }

    public String getCol_name() {
        return col_name;
    }
}
