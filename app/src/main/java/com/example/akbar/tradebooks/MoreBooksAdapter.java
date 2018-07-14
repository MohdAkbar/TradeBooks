package com.example.akbar.tradebooks;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Akbar on 7/3/2018.
 */
public class MoreBooksAdapter extends RecyclerView.Adapter<MoreBooksAdapter.MoreViewHolder>
{
    public List<Book>  bookList;
    Context context;

    public MoreBooksAdapter(List<Book> bookList,Context context)
    {
        this.bookList = bookList;
        this.context=context;
    }

    @Override
    public MoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        int layoutId=R.layout.more_books_row;
        LayoutInflater inflater=LayoutInflater.from(context);
        boolean shouldAttachImmediately=false;
        View myView=inflater.inflate(layoutId,parent,shouldAttachImmediately);
        MoreViewHolder viewHolder=new MoreViewHolder(myView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MoreViewHolder holder, int position) {
        holder.bind(bookList.get(position));
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class MoreViewHolder extends RecyclerView.ViewHolder
    {
        public AppCompatTextView rowBookPrice,rowBookName,rowBookDescription;
        public ImageView rowBookPic;
        LinearLayout containerRow;//

        public MoreViewHolder(View itemView) {
            super(itemView);
            rowBookPrice=(AppCompatTextView)itemView.findViewById(R.id.rowBookPrice);
            rowBookDescription=(AppCompatTextView)itemView.findViewById(R.id.rowBookDescription);
            rowBookName=(AppCompatTextView)itemView.findViewById(R.id.rowBookName);
            rowBookPic=(ImageView) itemView.findViewById(R.id.rowBookPic);
            containerRow=itemView.findViewById(R.id.containerRow);//
        }

        void bind(final Book ob)
        {
            rowBookPrice.setText("Rs."+String.valueOf(ob.getPrice()));
            rowBookDescription.setText(ob.getDescription());
            rowBookName.setText(ob.getName());
            Glide.with(context).load(ob.getPhotoUrl())
                    .into(rowBookPic);
            containerRow.setOnClickListener(new View.OnClickListener() {//
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context,BookPreview.class);
                    intent.putExtra("bookId",ob.getBookId());
                    context.startActivity(intent);
                }
            });//
        }
    }
}
