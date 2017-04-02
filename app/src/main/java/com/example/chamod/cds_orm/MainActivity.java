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


//        test annotations
        ArrayList<User> users=User.getAll(User.class,this);

//        User user=User.getFirst(User.class,this);

        for (User user:users
             ) {
            Log.w("ORM","obj got");
            Log.w("ORM","obj id - "+user.id);
            Log.w("ORM","obj name - "+user.name);
            Log.w("ORM","obj password - "+user.password);
        }

    }
}
