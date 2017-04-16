package com.example.chamod.cds_orm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.chamod.cds_orm.ExampleClasses.Author;
import com.example.chamod.cds_orm.ExampleClasses.Bag;
import com.example.chamod.cds_orm.ExampleClasses.Book;
import com.example.chamod.cds_orm.ExampleClasses.User;
import com.example.chamod.cds_orm.ExampleClasses.Van;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//.......................................insert.......................................................
//        User uu=new User(this,"fuck","111","galle");
//
//        ArrayList<Bag> bags=new ArrayList<>();
//        bags.add(new Bag(this,"Red"));
//        bags.add(new Bag(this,"Blu"));
//        bags.add(new Bag(this,"Gren"));
//
//        uu.setBags(bags);
////
//        uu.save();
////        User user=User.get(User.class,this,"name","Test567").get(0);
//////
////        Author author=new Author(this,"sss","ccc");
////        Book book=new Book(this,"dfd book",author);
////
////        user.setBook(book);
////
//        uu.update();
////
////        user.update();
////      /////////////////////
//
////        User.delete(User.class,this,"id",0);
//
//
//
        Log.e("ORM","-----------------------------------------------");

//        User uu =User.get(User.class,this,"name","fuck").get(0);
////////Log.e("ORM","size  "+users.size());
//
//        ArrayList<Bag> bags=new ArrayList<>();
//        bags.add(new Bag(this,"tt"));
//        bags.add(new Bag(this,"ttttttt"));
//
//        uu.setBags(bags);
////
//        uu.update();
////////
//

//        User uu =User.get(User.class,this,"name","fuck").get(0);
//
//        uu.bags.get(0).color="changedcolor";
//        uu.update();
//
//        User u =User.get(User.class,this,"name","fuck").get(0);
//
//        Log.e("ORM", u.id+"");
//        Log.e("ORM", u.name+"");
//        Log.e("ORM", u.address+"");
//        Log.e("ORM", u.password+"");
//
//        Log.e("ORM", u.bags.size()+" bags");
//        Log.e("ORM", u.bags.get(0).color+"");
//        Log.e("ORM", u.bags.get(0).id+"");
////
////        Log.e("ORM", u.book.id+"");
////        Log.e("ORM", u.book.name+"");
////
////        Log.e("ORM", u.book.author.author_id+"");
////        Log.e("ORM", u.book.author.name+"");
//
////        Bag bag=Bag.get(Bag.class,this,"Userid",2).get(0);
//
//
////        Car c=new Car(this,true);
////        c.save();
//
////        Car c=Car.get(Car.class,this,"id",34).get(0);
////        Log.e("ORM",c.id+"");
////        Log.e("ORM",c.isDamaged+"");
////        Log.e("ORM",c.getTemp_id()+"");
////
////        Log.e("ORM","-----------------------------------------------");
////
//////        c.id=34;
////        c.isDamaged=true;
////        c.update();
//////
////        Log.e("ORM",c.id+"");
////        Log.e("ORM",c.isDamaged+"");
////        Log.e("ORM",c.getTemp_id()+"");
//
//
////        ArrayList<Book> books=Book.getAll(Book.class,this);
////
////        Log.e("ORM",books.size()+" size");
//


        Van van=new Van(this,false,49.78f);

        van.save();


        Van v=Van.getFirst(Van.class,this);

        Log.e("ORM",v.id+"");
        Log.e("ORM",v.isDamaged+"");
        Log.e("ORM",v.speed+"");

    }
}
