package com.example.chamod.cds_orm.ExampleClasses;

import android.content.Context;

import com.example.chamod.cds_orm.AndroidModel;
import com.example.chamod.cds_orm.DBAnnotation;

/**
 * Created by chamod on 4/15/17.
 */

public class Book extends AndroidModel{
    @DBAnnotation.PrimaryKey
    @DBAnnotation.DBColumn
    @DBAnnotation.AutoIncrement
    public int id;

    @DBAnnotation.DBColumn
    public String name;

    @DBAnnotation.DBModel
    public Author author;

    public Book(Context context){
        super(context);
    }
    public Book(Context context,String name,Author author){
        super(context);
        this.name=name;
        this.author=author;
    }
}
