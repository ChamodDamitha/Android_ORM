package com.example.chamod.cds_orm.ExampleClasses;

import android.content.Context;

import com.example.chamod.cds_orm.DBAnnotation;

/**
 * Created by chamod on 4/16/17.
 */

public class Van extends Car{

    @DBAnnotation.DBColumn
    public float speed;

    public Van(Context context){
        super(context);
    }

    public Van(Context context, boolean isDamaged,float speed) {
        super(context, isDamaged);
        this.speed=speed;
    }


}
