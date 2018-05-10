package com.tkachuk.tpris_3.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tkachuk.tpris_3.R;
import com.tkachuk.tpris_3.data.FirebaseManager;
import com.tkachuk.tpris_3.data.model.Book;
import com.tkachuk.tpris_3.data.IFirebaseManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private RecyclerView list_rv;
    private FloatingActionButton add_fab;

    private SwipeRefreshLayout swipeContainer;

    private FirebaseManager firebaseManager;
    private ArrayList<Book> bookArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();

        firebaseManager = new FirebaseManager();
    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseManager.loadList(new IFirebaseManager.loadListCallBack() {
            @Override
            public void onLoad(ArrayList<Book> bookArrayList) {
                loadList(bookArrayList);
                Log.i("draxvel", "onLoad");

                showMessage("List updated!");

                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(String msg) {
                showMessage("Firebase error!");
                Log.i("draxvel", "onFailure");

                bookArrayList = new ArrayList<>();
            }
        });
    }
    private void loadList(ArrayList<Book> bookArrayList){
        BookAdapter bookAdapter = new BookAdapter(getApplicationContext(), bookArrayList);
        list_rv.setAdapter(bookAdapter);

        bookAdapter.notifyDataSetChanged();
    }


    private void initView() {
        list_rv = findViewById(R.id.list_rv);
        list_rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        add_fab = findViewById(R.id.add_fab);


        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onStart();
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void initListener() {
        add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Book book = (Book) data.getExtras().getSerializable("Book");

                firebaseManager.addBook(book, new IFirebaseManager.addBookCallBack() {
                    @Override
                    public void onAdded() {
                        showMessage("Added!");
                    }

                    @Override
                    public void onFailure(String msg) {
                        showMessage("Firebase error!");
                    }
                });
            }
        }
    }

    private void showMessage(final String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sort_action) {
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle("Sort");
            String[] types = {"by date", "by price", "by name"};
            b.setItems(types, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    switch (which) {
                        case 0:
                            sortByDate();
                            break;
                        case 1:
                            sortByPrice();
                            break;
                        case 2:
                            sortByName();
                            break;
                    }
                }});
            b.show();
        }

        if(item.getItemId()==R.id.search_action){
            final EditText editText = new EditText(this);

            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle("Enter name:");
            b.setView(editText);
            b.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String name = String.valueOf(editText.getText());
                    search(name);
                }
            });
                    b.setNegativeButton("Cancel", null)
                    .create();
            b.show();
        }

        return true;
    }

    private void search(String name) {
        firebaseManager.searchBook(name, new IFirebaseManager.searchBookCallBack() {

            @Override
            public void onFind(ArrayList<Book> bookArrayList) {
                loadList(bookArrayList);
                showMessage("Find!");
            }

            @Override
            public void onFailure(String msg) {
                showMessage("firebase error");
            }
        });
    }

    private void sortByName() {
        firebaseManager.sortListByName(new IFirebaseManager.sortListByNameCallBack() {
            @Override
            public void onSorted(ArrayList<Book> bookArrayList) {
                loadList(bookArrayList);
                showMessage("Sorted!");
            }

            @Override
            public void onFailure(String msg) {
                showMessage("firebase erroe!");
            }
        });
    }

    private void sortByPrice() {
        firebaseManager.sortListByPrice(new IFirebaseManager.sortListByPriceCallBack() {
            @Override
            public void onSorted(ArrayList<Book> bookArrayList) {
                loadList(bookArrayList);
                showMessage("Sorted!");
            }

            @Override
            public void onFailure(String msg) {
                showMessage("firebase erroe!");
            }
        });
    }

    private void sortByDate() {
        firebaseManager.loadList(new IFirebaseManager.loadListCallBack() {
            @Override
            public void onLoad(ArrayList<Book> bookArrayList) {
                loadList(bookArrayList);
                showMessage("Sorted!");
            }

            @Override
            public void onFailure(String msg) {
                showMessage("firebase erroe!");
            }
        });
    }
}
