package com.tkachuk.tpris_3.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tkachuk.tpris_3.R;
import com.tkachuk.tpris_3.data.FirebaseManager;
import com.tkachuk.tpris_3.data.IFirebaseManager;
import com.tkachuk.tpris_3.data.model.Book;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder>{

    private ArrayList<Book> bookArrayList;
    private Context context;

    public BookAdapter(Context context, ArrayList<Book> bookArrayList) {
        this.context = context;
        this.bookArrayList = bookArrayList;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);

        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookViewHolder holder, final int position) {
        holder.name_of_book_tv.setText(bookArrayList.get(position).getName());
        holder.author_of_book_tv.setText(bookArrayList.get(position).getAuthor());
        holder.price_of_book_tv.setText(String.valueOf(bookArrayList.get(position).getPrice())+" uah");

        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseManager firebaseManager = new FirebaseManager();
                firebaseManager.deleteBook(bookArrayList.get(position).getTimestamp(), new IFirebaseManager.deleteBookCallBack() {
                    @Override
                    public void onDelete() {
                        Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String msg) {
                        Toast.makeText(context, "Firebase error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        holder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myactivity = new Intent(context, EditActivity.class);
                myactivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myactivity.putExtra("book", bookArrayList.get(position));
                context.getApplicationContext().startActivity(myactivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookArrayList.size();
    }


    public class BookViewHolder extends RecyclerView.ViewHolder{

        private TextView name_of_book_tv;
        private TextView price_of_book_tv;
        private TextView author_of_book_tv;

        private Button edit_btn;
        private Button delete_btn;

        public BookViewHolder(View itemView) {
            super(itemView);

            name_of_book_tv = itemView.findViewById(R.id.name_of_book_tv);
            price_of_book_tv = itemView.findViewById(R.id.price_of_book_tv);
            author_of_book_tv = itemView.findViewById(R.id.author_of_book_tv);

            edit_btn = itemView.findViewById(R.id.edit_btn);
            delete_btn = itemView.findViewById(R.id.delete_btn);
        }
    }
}
