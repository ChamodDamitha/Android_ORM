package com.example.chamod.cds_orm.ExampleClasses;

import android.content.Context;

import com.example.chamod.cds_orm.AndroidModel;
import com.example.chamod.cds_orm.DBAnnotation;

/**
 * Created by chamod on 4/15/17.
 */

public class Author extends AndroidModel {

    @DBAnnotation.PrimaryKey
    @DBAnnotation.DBColumn
    public String author_id;

    @DBAnnotation.DBColumn
    public String name;

    public Author(Context context) {
        super(context);
    }

    public Author(Context context,String author_id,String name) {
        super(context);
        this.author_id=author_id;
        this.name=name;
    }

}
