package com.example.chamod.cds_orm.ExampleClasses;

import android.content.Context;

import com.example.chamod.cds_orm.AndroidModel;
import com.example.chamod.cds_orm.DBAnnotation;

/**
 * Created by chamod on 4/16/17.
 */

public class Car extends AndroidModel{

    @DBAnnotation.PrimaryKey
    @DBAnnotation.DBColumn
    @DBAnnotation.AutoIncrement
    public int id;

    @DBAnnotation.DBColumn
    public boolean isDamaged;


    public Car(Context context) {
        super(context);
    }

    public Car(Context context,boolean isDamaged) {
        super(context);
        this.isDamaged = isDamaged;
    }
}
