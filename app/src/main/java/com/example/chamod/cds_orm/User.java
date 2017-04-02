package com.example.chamod.cds_orm;

import android.content.Context;

/**
 * Created by chamod on 4/1/17.
 */


@DBAnnotation.TableName(table_name = "Users")
public class User extends AndroidModel {

    @DBAnnotation.DBColumn
    protected int id;

    @DBAnnotation.DBColumn
    protected String name="chamod";

    @DBAnnotation.DBColumn
    protected String password="pass";


    public User(Context context) {
        super(context);
    }
}
