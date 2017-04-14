package com.example.chamod.cds_orm.DBModels;

/**
 * Created by chamod on 4/1/17.
 */

public class Attribute {
    private String name;
    private String type;
    private boolean isPrimary;


    public Attribute(String name, String type, boolean isPrimary) {
        this.name = name;
        this.type = type;
        this.isPrimary = isPrimary;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isPrimary() {
        return isPrimary;
    }



}
