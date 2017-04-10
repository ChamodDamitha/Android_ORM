package com.example.chamod.cds_orm.DBModels;

import android.content.Context;

import com.example.chamod.cds_orm.AndroidModel;
import com.example.chamod.cds_orm.DBAnnotation;

/**
 * Created by chamod on 4/10/17.
 */

@DBAnnotation.TableName(table_name = "bags")
public class Bag extends AndroidModel {

    @DBAnnotation.PrimaryKey
    @DBAnnotation.DBColumn
    public int id;

    @DBAnnotation.DBColumn
    public String color;

    public Bag(Context context) {
        super(context);
    }


}
