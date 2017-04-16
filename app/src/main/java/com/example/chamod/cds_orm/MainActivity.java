package com.example.chamod.cds_orm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.chamod.cds_orm.ExampleClasses.Author;
import com.example.chamod.cds_orm.ExampleClasses.Bag;
import com.example.chamod.cds_orm.ExampleClasses.Book;
import com.example.chamod.cds_orm.ExampleClasses.User;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//.......................................insert.......................................................
//        User user=new User(this,"Test567","111","galle");
//
//        ArrayList<Bag> bags=new ArrayList<>();
//        bags.add(new Bag(this,"RR"));
//        bags.add(new Bag(this,"BB"));
//        bags.add(new Bag(this,"GGG"));
//
//        user.setBags(bags);
//
//        Author author=new Author(this,"autttt","Kdfdamal");
//        Book book=new Book(this,"fuckk book",author);
//
//        user.setBook(book);
//
//        user.save();

//      /////////////////////


        User user=User.getFirst(User.class,this);
//Log.e("ORM","size  "+users.size());
////
//        User user=users.get(0);

        Log.e("ORM",user.id+"");
        Log.e("ORM",user.name+"");
        Log.e("ORM",user.address+"");
        Log.e("ORM",user.password+"");

        Log.e("ORM",user.bags.get(2).id+"");
        Log.e("ORM",user.bags.get(2).color+"");

        Log.e("ORM",user.book.id+"");
        Log.e("ORM",user.book.name+"");

        Log.e("ORM",user.book.author.author_id+"");
        Log.e("ORM",user.book.author.name+"");


    }
}
