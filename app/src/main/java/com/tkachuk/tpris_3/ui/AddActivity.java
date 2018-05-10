package com.tkachuk.tpris_3.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tkachuk.tpris_3.R;
import com.tkachuk.tpris_3.data.model.Book;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

    private EditText name_et;
    private EditText author_et;
    private EditText price_et;
    private EditText year_et;
    private EditText code_et;
    private EditText count_et;

    private Button add_btn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        initView();

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText [] fiedls = {name_et, author_et, price_et, year_et, code_et, count_et};

                if(validate(fiedls)){
                    Date date = new Date();
                    Timestamp timestamp  = new Timestamp(date.getTime());

                    Book book = new Book(
                            encodeString(timestamp.toString()),
                            name_et.getText().toString(),
                            author_et.getText().toString(),
                            Double.valueOf(price_et.getText().toString()),
                            Integer.valueOf(year_et.getText().toString()),
                            code_et.getText().toString(),
                            Integer.valueOf(count_et.getText().toString()));


                    Intent intent = new Intent(AddActivity.this, MainActivity.class);
                    intent.putExtra("Book",  (Serializable) book);
                    setResult(RESULT_OK, intent);
                    finish();
                }else {
                    Toast.makeText(AddActivity.this, "Empty fileds!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void initView() {
        name_et = findViewById(R.id.name_et);
        author_et = findViewById(R.id.author_et);
        price_et = findViewById(R.id.price_et);
        year_et = findViewById(R.id.year_et);
        code_et = findViewById(R.id.code_et);
        count_et = findViewById(R.id.count_et);

        add_btn = findViewById(R.id.add_btn);
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
}
