package com.example.chamod.cds_orm;

import android.content.Context;

/**
 * Created by chamod on 4/1/17.
 */


@DBAnnotation.TableName(table_name = "Users")
public class User extends AndroidModel {

    @DBAnnotation.DBColumn
    @DBAnnotation.PrimaryKey
    protected int id;

    @DBAnnotation.DBColumn
    protected String name;

    @DBAnnotation.DBColumn
    protected String password;


    public User(Context context){
        super(context);
    }

    public User(Context context,int id,String name,String password) {
        super(context);
        this.id=id;
        this.name=name;
        this.password=password;
    }
}
