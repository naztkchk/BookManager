package com.tkachuk.tpris_3.data;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tkachuk.tpris_3.data.model.Book;

import java.util.ArrayList;

public class FirebaseManager implements IFirebaseManager {

    DatabaseReference database;

    public FirebaseManager() {
        database = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void addBook(Book book, final addBookCallBack addBookCallBack) {
        database.child("books").child(book.getTimestamp()).setValue(book).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    addBookCallBack.onAdded();
                }else {
                    addBookCallBack.onFailure(task.getException().getMessage().toString());
                }
            }
        });
    }

    @Override
    public void editBook(Book book, final editBookCallBack editBookCallBack) {
        database.child("books").child(book.getTimestamp()).setValue(book).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    editBookCallBack.onEdit();
                }else editBookCallBack.onFailure(task.getException().getMessage().toString());
            }
        });
    }

    @Override
    public void deleteBook(String timestamp, final deleteBookCallBack deleteBookCallBack) {
        database.child("books").child(timestamp).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    deleteBookCallBack.onDelete();
                }else deleteBookCallBack.onFailure(task.getException().getMessage().toString());
            }
        });
    }

    @Override
    public void loadList(final loadListCallBack loadListCallBack) {
        database.child("books").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Book> bookArrayList = new ArrayList<>();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    Book book = snapshot.getValue(Book.class);
                    bookArrayList.add(book);
                }
                loadListCallBack.onLoad(bookArrayList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                loadListCallBack.onFailure(databaseError.getMessage().toString());
            }
        });
    }

    @Override
    public void sortListByName(final sortListByNameCallBack sortListByNameCallBack) {
        database.child("books").orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Book> bookArrayList = new ArrayList<>();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    Book book = snapshot.getValue(Book.class);
                    bookArrayList.add(book);
                }
                sortListByNameCallBack.onSorted(bookArrayList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                sortListByNameCallBack.onFailure(databaseError.getMessage().toString());
            }
        });
    }

    @Override
    public void sortListByPrice(final sortListByPriceCallBack sortListByPriceCallBack) {
        database.child("books").orderByChild("price").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Book> bookArrayList = new ArrayList<>();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    Book book = snapshot.getValue(Book.class);
                    bookArrayList.add(book);
                }
                sortListByPriceCallBack.onSorted(bookArrayList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                sortListByPriceCallBack.onFailure(databaseError.getMessage().toString());
            }
        });
    }

    @Override
    public void searchBook(String name, final searchBookCallBack searchBookCallBack) {
        database.child("books").orderByChild("name").equalTo(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Book> bookArrayList = new ArrayList<>();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    Book book = snapshot.getValue(Book.class);
                    bookArrayList.add(book);
                }
                searchBookCallBack.onFind(bookArrayList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                searchBookCallBack.onFailure(databaseError.getMessage().toString());
            }
        });
    }
}
