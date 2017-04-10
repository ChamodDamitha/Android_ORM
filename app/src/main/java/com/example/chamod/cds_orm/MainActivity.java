package com.example.chamod.cds_orm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User user=User.get(User.class,this,"id",5).get(0);

        Log.e("ORM",user.id+"");
        Log.e("ORM",user.name);
        Log.e("ORM",user.password);


        user.name="C.D.Samarajeewa";
        user.password="566";

        user.update();

        Log.e("ORM",user.id+"");
        Log.e("ORM",user.name);
        Log.e("ORM",user.password);
    }
}
