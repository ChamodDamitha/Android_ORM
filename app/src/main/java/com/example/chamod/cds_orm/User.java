package com.example.chamod.cds_orm;

import android.content.Context;

import java.util.ArrayList;

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


    @DBAnnotation.DBModelList(model_class = Bag.class)
    public ArrayList<Bag> bags;


    public User(Context context){
        super(context);
    }

    public User(Context context,String name,String password,String address) {
        super(context);
        this.name=name;
        this.password=password;
        this.address=address;
    }

//    public void setBag(Bag bag){
//        this.bag=bag;
//    }


    public void setBags(ArrayList<Bag> bags) {
        this.bags = bags;
    }
}
