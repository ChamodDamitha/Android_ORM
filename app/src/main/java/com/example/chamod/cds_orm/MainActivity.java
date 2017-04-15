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



   User user=new User(this,"Test","24124235","Colombo");

        ArrayList<Bag> bags=new ArrayList<>();
        bags.add(new Bag(this,"RED"));
        bags.add(new Bag(this,"BLUE"));
        bags.add(new Bag(this,"GREEN"));

    user.setBags(bags);

        Author author=new Author(this,"auth2","K" +
                "amal");
        Book book=new Book(this,"Jungle book",author);

        user.setBook(book);

    user.save();

//      /////////////////////


        User u=User.get(User.class,this,"id",user.id).get(0);

        Log.e("ORM","user id - "+u.id);
        Log.e("ORM","user name - "+u.name);
        Log.e("ORM","user password - "+u.password);
        Log.e("ORM","user address - "+u.address);


        Log.e("ORM","bag2 id - "+u.bags.get(1).id);
        Log.e("ORM","bag2 color - "+u.bags.get(1).color);

        Book b=u.book;
        Log.e("ORM","book id - "+b.id);
        Log.e("ORM","book name - "+b.name);

        Author a=b.author;
        if(a!=null) {
            Log.e("ORM", "Author id - " + a.author_id);
            Log.e("ORM", "Author name - " + a.name);
        }




    }
}
