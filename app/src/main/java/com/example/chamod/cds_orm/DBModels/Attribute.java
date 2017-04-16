package com.example.chamod.cds_orm.DBModels;

import java.lang.reflect.Field;

/**
 * Created by chamod on 4/1/17.
 */

public class Attribute {
    private Field field;
    private String db_data_type;
    private boolean auto_increment;

    public Attribute(Field field, String db_data_type,boolean auto_increment) {
        this.field = field;
        this.db_data_type = db_data_type;
        this.auto_increment=auto_increment;
    }

    public Field getField() {
        return field;
    }

    public String getDb_data_type() {
        return db_data_type;
    }

    public boolean isAuto_increment() {
        return auto_increment;
    }
}
