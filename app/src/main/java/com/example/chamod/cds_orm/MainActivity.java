package com.example.chamod.cds_orm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        User.delete(User.class, this, "id", 4);
//
//        ArrayList<User> users=User.getAll(User.class,this);
//
//        for(User user:users) {
//            Log.e("ORM", user.id + "");
//            Log.e("ORM", user.name);
//            Log.e("ORM", user.password);
//        }
//
//        Bag bag=new Bag(this);
//        bag.color="BLUE";
//        bag.save();
//
//        for(Bag bag:Bag.getAll(Bag.class,this)) {
//            Log.e("ORM", bag.id + "");
//            Log.e("ORM", bag.color + "");
//        }


    User user=new User(this,"Damitha","123","Matara");

    user.setBag(new Bag(this,"RED"));

    user.save();


//        User u=User.getAll(User.class,this).get(1);
//
//        Log.e("ORM",u.id+"");
//        Log.e("ORM",u.name+"");
//        Log.e("ORM",u.password+"");
//        Log.e("ORM",u.address+"");

    }
}
