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

    @DBAnnotation.DBColumn
    protected String address;


    @DBAnnotation.DBModel
    public Bag bag;


    public User(Context context){
        super(context);
    }

    public User(Context context,String name,String password,String address) {
        super(context);
        this.name=name;
        this.password=password;
        this.address=address;
    }

    public void setBag(Bag bag){
        this.bag=bag;
    }
}
