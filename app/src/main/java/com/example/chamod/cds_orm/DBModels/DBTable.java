package com.example.chamod.cds_orm.DBModels;

import java.util.ArrayList;

/**
 * Created by chamod on 4/1/17.
 */

public class DBTable {
    private String name;
    private ArrayList<Attribute> attributes=new ArrayList<>();
    private ArrayList<ForeignKey> foreignKeys=new ArrayList<>();

    public DBTable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    public void addAttribute(Attribute attribute){
        attributes.add(attribute);
    }

    public void addForeignKey(ForeignKey foreignKey){
        foreignKeys.add(foreignKey);
    }

    public ArrayList<ForeignKey> getForeignKeys() {
        return foreignKeys;
    }

    public Attribute getPrimaryAttribute(){
        for (Attribute a:attributes){
            if(a.isPrimary()){
                return a;
            }
        }
        return null;
    }
}
