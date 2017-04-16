package com.example.chamod.cds_orm.DBModels;

import java.util.ArrayList;

/**
 * Created by chamod on 4/1/17.
 */

public class DBTable {
    private String table_name;
    private ArrayList<Attribute> attributes=new ArrayList<>();
    private Attribute primary_attribute;

    public DBTable(String table_name) {
        this.table_name = table_name;
        this.attributes=new ArrayList<>();
    }

    public String getTable_name() {
        return table_name;
    }

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    public void addAttribute(Attribute attribute){
        attributes.add(attribute);
    }

    public Attribute getPrimary_attribute() {
        return primary_attribute;
    }

    public void setPrimary_attribute(Attribute primary_attribute) {
        this.primary_attribute = primary_attribute;
    }
}
