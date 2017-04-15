package com.example.chamod.cds_orm.ExampleClasses;

import android.content.Context;

import com.example.chamod.cds_orm.AndroidModel;
import com.example.chamod.cds_orm.DBAnnotation;
import com.example.chamod.cds_orm.ExampleClasses.Bag;

import java.util.ArrayList;

/**
 * Created by chamod on 4/1/17.
 */


public class User extends AndroidModel {

    @DBAnnotation.DBColumn
    @DBAnnotation.PrimaryKey
    public int id;

    @DBAnnotation.DBColumn
    public String name;

    @DBAnnotation.DBColumn
    public String password;

    @DBAnnotation.DBColumn
    public String address;

    @DBAnnotation.DBModel
    public Book book;

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

    public void setBook(Book book) {
        this.book = book;
    }

    public void setBags(ArrayList<Bag> bags) {
        this.bags = bags;
    }
}
