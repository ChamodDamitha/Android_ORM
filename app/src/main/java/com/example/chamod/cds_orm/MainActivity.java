package com.example.chamod.cds_orm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.chamod.cds_orm.ExampleClasses.Bag;
import com.example.chamod.cds_orm.ExampleClasses.User;

import java.util.ArrayList;

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
//        bag.saveModel();
//
//        for(Bag bag:Bag.getAll(Bag.class,this)) {
//            Log.e("ORM", bag.id + "");
//            Log.e("ORM", bag.color + "");
//        }

//
//   User user=new User(this,"Test","24124235","Colombo");
//
//        ArrayList<Bag> bags=new ArrayList<>();
//        bags.add(new Bag(this,"RED"));
//        bags.add(new Bag(this,"BLUE"));
//        bags.add(new Bag(this,"GREEN"));
//
//    user.setBags(bags);
//
//    user.save();
//
//
//        User u=User.get(User.class,this,"name","Test").get(1);
//
//        Log.e("ORM",u.id+"");
//        Log.e("ORM",u.name+"");
//        Log.e("ORM",u.password+"");
//        Log.e("ORM",u.address+"");
//
//        Bag b=u.bags.get(1);
//
//        Log.e("ORM",b.id+"");
//        Log.e("ORM",b.color+"");

//        u.id=7;
//
//        u.update();
////
//        b.color="changedagain";

//        User.delete(User.class,this,"id",2);


//        User u=User.get(User.class,this,"id",2).get(0);
//
//        Log.e("ORM",b.id+"");
//        Log.e("ORM",b.color+"");
//        Bag bag= Bag.get(Bag.class,this,"id",4).get(0);
////
//        Log.e("ORM",bag.color);

    }
}
