package com.example.chamod.cds_orm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.chamod.cds_orm.ExampleClasses.Author;
import com.example.chamod.cds_orm.ExampleClasses.Book;
import com.example.chamod.cds_orm.ExampleClasses.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//.......................................insert.......................................................
//        User u=new User(this,"Test567","111","galle");
//
//        ArrayList<Bag> bags=new ArrayList<>();
//        bags.add(new Bag(this,"RR"));
//        bags.add(new Bag(this,"BB"));
//        bags.add(new Bag(this,"GGG"));
//
//        u.setBags(bags);

        User user=User.get(User.class,this,"name","Test567").get(0);
//
        Author author=new Author(this,"autttt","Kdfdamal");
        Book book=new Book(this,"fuckk book",author);

        user.setBook(book);
//
//        u.save();

        user.update();
//      /////////////////////

//        User.delete(User.class,this,"id",0);



        User u =User.getFirst(User.class,this);
//Log.e("ORM","size  "+users.size());
////
//        User u=users.get(0);

        Log.e("ORM", u.id+"");
        Log.e("ORM", u.name+"");
        Log.e("ORM", u.address+"");
        Log.e("ORM", u.password+"");

        Log.e("ORM", u.bags.get(2).id+"");
        Log.e("ORM", u.bags.get(2).color+"");

        Log.e("ORM", u.book.id+"");
        Log.e("ORM", u.book.name+"");

        Log.e("ORM", u.book.author.author_id+"");
        Log.e("ORM", u.book.author.name+"");

//        Bag bag=Bag.get(Bag.class,this,"Userid",2).get(0);


//        Car c=new Car(this,true);
//        c.save();

//        Car c=Car.get(Car.class,this,"id",34).get(0);
//        Log.e("ORM",c.id+"");
//        Log.e("ORM",c.isDamaged+"");
//        Log.e("ORM",c.getTemp_id()+"");
//
//        Log.e("ORM","-----------------------------------------------");
//
////        c.id=34;
//        c.isDamaged=true;
//        c.update();
////
//        Log.e("ORM",c.id+"");
//        Log.e("ORM",c.isDamaged+"");
//        Log.e("ORM",c.getTemp_id()+"");


    }
}
