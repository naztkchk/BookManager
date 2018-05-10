package com.tkachuk.tpris_3.data;

import com.tkachuk.tpris_3.data.model.Book;

import java.util.ArrayList;

public interface IFirebaseManager {

    interface errorCallBack{
        void onFailure(final String msg);
    }

    interface addBookCallBack extends errorCallBack{
        void onAdded();
    }

    interface editBookCallBack extends errorCallBack{
        void onEdit();
    }

    interface deleteBookCallBack extends errorCallBack{
        void onDelete();
    }

    interface loadListCallBack extends errorCallBack{
        void onLoad( ArrayList<Book> bookArrayList);
    }

    interface sortListByNameCallBack extends errorCallBack{
        void onSorted(ArrayList<Book> bookArrayList);
    }

    interface sortListByPriceCallBack extends errorCallBack{
        void onSorted(ArrayList<Book> bookArrayList);
    }

    interface searchBookCallBack extends errorCallBack{
        void onFind(ArrayList<Book> bookArrayList);
    }

    void addBook(Book book, addBookCallBack addBookCallBack);
    void editBook(Book book, editBookCallBack editBookCallBack);
    void deleteBook(String timestamp, deleteBookCallBack deleteBookCallBack);
    void loadList(loadListCallBack loadListCallBack);

    void sortListByName(sortListByNameCallBack sortListByNameCallBack);
    void sortListByPrice(sortListByPriceCallBack sortListByPriceCallBack);

    void searchBook(String name, searchBookCallBack searchBookCallBack);
}
