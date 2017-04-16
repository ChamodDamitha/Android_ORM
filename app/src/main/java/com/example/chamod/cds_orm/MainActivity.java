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



   User user=new User(this,"Test2","1234","Matara");

        ArrayList<Bag> bags=new ArrayList<>();
        bags.add(new Bag(this,"R"));
        bags.add(new Bag(this,"B"));
        bags.add(new Bag(this,"G"));

    user.setBags(bags);

        Author author=new Author(this,"auth2","Kamal");
        Book book=new Book(this,"Jungle book",author);

        user.setBook(book);

    user.save();

      /////////////////////





    }
}
