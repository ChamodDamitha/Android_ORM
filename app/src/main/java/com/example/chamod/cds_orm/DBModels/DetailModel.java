package com.example.chamod.cds_orm.DBModels;

import java.util.ArrayList;

/**
 * Created by chamod on 4/16/17.
 */

public class DetailModel {
    private Class<?> claz;
    private DBTable dbTable;
    private ArrayList<ForeignModel> foreignModels;
    private ArrayList<ForeignModelList> foreignModelLists;


    public DetailModel(Class<?> claz) {
        this.claz = claz;
        this.foreignModels=new ArrayList<>();
        this.foreignModelLists=new ArrayList<>();
    }

    public Class<?> getClaz() {
        return claz;
    }

    public void setClaz(Class<?> claz) {
        this.claz = claz;
    }

    public DBTable getDbTable() {
        return dbTable;
    }

    public void setDbTable(DBTable dbTable) {
        this.dbTable = dbTable;
    }

    public ArrayList<ForeignModel> getForeignModels() {
        return foreignModels;
    }

    public void addForeignModel(ForeignModel foreignModel) {
        this.foreignModels.add(foreignModel);
    }

    public ArrayList<ForeignModelList> getForeignModelLists() {
        return foreignModelLists;
    }

    public void addForeignModelList(ForeignModelList foreignModelList) {
        this.foreignModelLists.add(foreignModelList);
    }
}
