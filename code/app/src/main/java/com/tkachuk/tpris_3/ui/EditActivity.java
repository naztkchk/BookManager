package com.tkachuk.tpris_3.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tkachuk.tpris_3.R;
import com.tkachuk.tpris_3.data.FirebaseManager;
import com.tkachuk.tpris_3.data.IFirebaseManager;
import com.tkachuk.tpris_3.data.model.Book;

public class EditActivity extends AppCompatActivity {

    private EditText ename_et;
    private EditText eauthor_et;
    private EditText eprice_et;
    private EditText eyear_et;
    private EditText ecode_et;
    private EditText ecount_et;

    private Button edit_edit_btn;

    private Book book;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        initView();

        Bundle bundle = getIntent().getExtras();
        book = (Book) bundle.getSerializable("book");

        Log.i("draxvel", book.getAuthor());

        ename_et.setText(book.getName());

        eauthor_et.setText(book.getAuthor());

        eprice_et.setText(String.valueOf(book.getPrice()));
        eyear_et.setText(String.valueOf(book.getYear()));
        ecode_et.setText(book.getCode());
        ecount_et.setText(String.valueOf(book.getCount()));


        edit_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText[] fiedls = {ename_et, eauthor_et, eprice_et, eyear_et, ecode_et, ecount_et};

                if (validate(fiedls)) {

                    Book book_edited = new Book(
                            encodeString(book.getTimestamp()),
                            ename_et.getText().toString(),
                            eauthor_et.getText().toString(),
                            Double.valueOf(eprice_et.getText().toString()),
                            Integer.valueOf(eyear_et.getText().toString()),
                            ecode_et.getText().toString(),
                            Integer.valueOf(ecount_et.getText().toString()));


                    FirebaseManager firebaseManager = new FirebaseManager();

                    firebaseManager.editBook(book_edited, new IFirebaseManager.editBookCallBack() {
                        @Override
                        public void onEdit() {
                            showMessage("edited!");
                            finish();
                        }

                        @Override
                        public void onFailure(String msg) {
                            showMessage("Firebase error");
                            finish();
                        }
                    });

                } else {
                    Toast.makeText(EditActivity.this, "Empty fileds!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        ename_et = findViewById(R.id.ename_et);
        eauthor_et = findViewById(R.id.eauthor_et);
        eprice_et = findViewById(R.id.eprice_et);
        eyear_et = findViewById(R.id.eyear_et);
        ecode_et = findViewById(R.id.ecode_et);
        ecount_et = findViewById(R.id.ecount_et);

        edit_edit_btn = findViewById(R.id.edit_edit_btn);
    }


    private boolean validate(EditText[] fields) {
        for (int i = 0; i < fields.length; i++) {
            EditText currentField = fields[i];
            if (currentField.getText().toString().length() <= 0) {
                return false;
            }
        }
        return true;
    }

    public static String encodeString(String string) {
        return string.replace(".", ",");
    }

    private void showMessage(final String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
