package com.example.akbar.tradebooks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Akbar on 6/29/2018.
 */
public class BookPreview extends AppCompatActivity {

    Toolbar previewPageToolbar;
    AppCompatTextView namePreview,authorPreview,descriptionPreview,genrePreview,pricePreview,publicationPreview,sellerPreview;
    ImageView picPreview;
    Button buyPreview,addToCartPreview;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,reference2;
    String bookId,sellerId;
    Book currentBook=new Book();
    int flag=0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_preview);
        previewPageToolbar=(Toolbar)findViewById(R.id.previewPageToolbar);
        previewPageToolbar.setTitle("Book Preview");
        setSupportActionBar(previewPageToolbar);
        auth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        Intent i=getIntent();
        bookId=i.getStringExtra("bookId");
        namePreview=(AppCompatTextView)findViewById(R.id.namePreview);
        authorPreview=(AppCompatTextView)findViewById(R.id.authorPreview);
        descriptionPreview=(AppCompatTextView)findViewById(R.id.descriptionPreview);
        genrePreview=(AppCompatTextView)findViewById(R.id.genrePreview);
        pricePreview=(AppCompatTextView)findViewById(R.id.pricePreview);
        publicationPreview=(AppCompatTextView)findViewById(R.id.publicationPreview);
        sellerPreview=(AppCompatTextView)findViewById(R.id.sellerPreview);
        picPreview=(ImageView)findViewById(R.id.picPreview);
        addToCartPreview=(Button)findViewById(R.id.addToCartPreview);
        buyPreview=(Button)findViewById(R.id.buyPreview);
        buyPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentBook.setHits(currentBook.getHits()-1);
                databaseReference=firebaseDatabase.getReference().child("Books").child(bookId);
                databaseReference.setValue(currentBook);
                Intent intent=new Intent(BookPreview.this,ContactSeller.class);
                intent.putExtra("seller",currentBook.getSellerId());
                intent.putExtra("bookId",bookId);
                startActivity(intent);
            }
        });
        addToCartPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentBook.setAdded(currentBook.getAdded()-1);
                databaseReference=firebaseDatabase.getReference().child("Cart").child(auth.getCurrentUser().getUid()).child(bookId);
                databaseReference.setValue(currentBook);
                Toast.makeText(getApplicationContext(),"Book Added to your Cart",Toast.LENGTH_LONG).show();
                databaseReference=firebaseDatabase.getReference().child("Books").child(bookId);
                databaseReference.setValue(currentBook);
            }
        });
        showBookPreview();

    }

    private void checkStatus() {
        if(currentBook.getSellerId().equals(auth.getCurrentUser().getUid()))
        {
            buyPreview.setVisibility(View.INVISIBLE);
            addToCartPreview.setVisibility(View.INVISIBLE);
        }
        else if(currentBook.getBuyerId().equals(auth.getCurrentUser().getUid()))
        {
            buyPreview.setVisibility(View.INVISIBLE);
            addToCartPreview.setVisibility(View.INVISIBLE);
        }
    }

    private void updateBookViews() {
        databaseReference=firebaseDatabase.getReference().child("Books").child(bookId);
        currentBook.setViews(currentBook.getViews()-1);
        databaseReference.setValue(currentBook);
    }

    private void showSellerInformation() {

        reference2=firebaseDatabase.getReference().child("Users").child(sellerId);
        reference2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User ob=dataSnapshot.getValue(User.class);
                sellerPreview.setText("This book is sold by Mr."+ob.getName());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



    }


    private void showBookPreview() {
        databaseReference=firebaseDatabase.getReference().child("Books");
        databaseReference.orderByKey().equalTo(bookId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot child : dataSnapshot.getChildren())
                {
                    if(child!=null)
                    {
                        if (child.getKey().equals("name")) {
                            namePreview.setText(child.getValue().toString());
                            currentBook.setName(child.getValue().toString());
                            flag++;
                        } else if (child.getKey().equals("author")) {
                            authorPreview.setText("Written By: "+child.getValue().toString());
                            currentBook.setAuthor(child.getValue().toString());
                            flag++;
                        } else if (child.getKey().equals("description")) {
                            descriptionPreview.setText(child.getValue().toString());
                            currentBook.setDescription(child.getValue().toString());
                            flag++;
                        } else if (child.getKey().equals("publication")) {
                            publicationPreview.setText("Published By: "+child.getValue().toString());
                            currentBook.setPublication(child.getValue().toString());
                            flag++;
                        } else if (child.getKey().equals("genre")) {
                            genrePreview.setText(child.getValue().toString());
                            currentBook.setGenre(child.getValue().toString());
                            flag++;
                        } else if (child.getKey().equals("price")) {
                            pricePreview.setText("Rs."+child.getValue().toString());
                            currentBook.setPrice(Integer.parseInt(child.getValue().toString()));
                            flag++;
                        } else if (child.getKey().equals("photoUrl")) {
                            currentBook.setPhotoUrl(child.getValue().toString());
                            Glide.with(BookPreview.this).load(child.getValue().toString())
                                    .into(picPreview);
                            flag++;
                        } else if (child.getKey().equals("sellerId")) {
                            sellerId=child.getValue().toString();
                            flag++;
                            currentBook.setSellerId(child.getValue().toString());
                            showSellerInformation();

                        }
                        else if(child.getKey().equals("publishedYear"))
                        {
                            currentBook.setPublishedYear(child.getValue().toString());
                            flag++;
                        }
                        else if(child.getKey().equals("hits"))
                        {
                            currentBook.setHits(Integer.parseInt(child.getValue().toString()));
                            flag++;
                        }
                        else if(child.getKey().equals("views"))
                        {
                            currentBook.setViews(Integer.parseInt(child.getValue().toString()));
                            flag++;
                        }
                        else if(child.getKey().equals("added"))
                        {
                            currentBook.setAdded(Integer.parseInt(child.getValue().toString()));
                            flag++;
                        }
                    }
                }
                if(flag==12)
                {
                    updateBookViews();
                    checkStatus();
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }
}
