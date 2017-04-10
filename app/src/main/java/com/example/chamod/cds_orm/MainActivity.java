package com.example.chamod.cds_orm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        User.delete(User.class, this, "id", 4);

        ArrayList<User> users=User.getAll(User.class,this);

        for(User user:users) {
            Log.e("ORM", user.id + "");
            Log.e("ORM", user.name);
            Log.e("ORM", user.password);
        }


    }
}
