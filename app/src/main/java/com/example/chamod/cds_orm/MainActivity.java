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

        User user=new User(this,5,"Damitha","855");
        user.save();


//        test annotations
        ArrayList<User> users=User.getAll(User.class,this);


        for (User user1:users
             ) {
            Log.e("ORM","obj got");
            Log.e("ORM","obj id - "+user1.id);
            Log.e("ORM","obj name - "+user1.name);
            Log.e("ORM","obj password - "+user1.password);
        }

    }
}
