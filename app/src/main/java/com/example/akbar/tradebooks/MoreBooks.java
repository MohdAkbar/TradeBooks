package com.example.akbar.tradebooks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akbar on 7/2/2018.
 */
public class MoreBooks extends AppCompatActivity {

    FirebaseAuth auth;
    RecyclerView moreBooksRecyclerView;
    LinearLayout moreBooksLayout;
    MoreBooksAdapter adapter;
    List<Book> bookList=new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Toolbar moreBooksToolbar;
    String requestCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_books);
        auth=FirebaseAuth.getInstance();
        Intent i=getIntent();
        requestCode=i.getStringExtra("requestCode");
        moreBooksToolbar=(Toolbar)findViewById(R.id.moreBooksToolbar);
        setSupportActionBar(moreBooksToolbar);
        moreBooksLayout=(LinearLayout)findViewById(R.id.moreBooksLayout);
        firebaseDatabase=FirebaseDatabase.getInstance();
        moreBooksRecyclerView=(RecyclerView)findViewById(R.id.moreBooksRecyclerView);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        moreBooksRecyclerView.setLayoutManager(manager);
        adapter=new MoreBooksAdapter(bookList,MoreBooks.this);
        moreBooksRecyclerView.setAdapter(adapter);
        if(requestCode.equals("R")) {
            setBooksRecommended();
        }
        else if(requestCode.equals("F"))
        {
            setBooksFreshDeals();
        }
        else if(requestCode.equals("T"))
        {
            setBooksTrending();
        }
        else if(requestCode.equals("TH"))
        {
            setBooksTradeBooks();
        }
        else if(requestCode.equals("D"))
        {
            setBooksTopDeals();
        }
        else if(requestCode.equals("GE"))
        {
            setBookGenre("Education");
        }
        else if(requestCode.equals("GK"))
        {
            setBookGenre("Kids");
        }
        else if(requestCode.equals("GT"))
        {
            setBookGenre("Thriller");
        }
        else if(requestCode.equals("GM"))
        {
            setBookGenre("Mystery");
        }
        else if(requestCode.equals("GB"))
        {
            setBookGenre("Biography");
        }
        else if(requestCode.equals("GS"))
        {
            setBookGenre("Story");
        }
        else if(requestCode.equals("GO"))
        {
            setBookGenre("Others");
        }
        else if(requestCode.equals("CART"))
        {
            showUserCart();
        }
        else if(requestCode.equals("SOLD"))
        {
            showUserSoldBooks();
        }
        else if(requestCode.equals("BOUGHT"))
        {
            showUserBoughtBooks();
        }
        else if(requestCode.equals("ORDERS"))
        {
            showUserSoldOrders();
        }
        adapter.notifyDataSetChanged();
    }

    private void setBooksTradeBooks() {

        databaseReference=firebaseDatabase.getReference().child("Books");
        databaseReference.orderByChild("sellerId").equalTo("O2vEnUPeZtQzWCI6S8kmQT6ugWD2").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Book ob=dataSnapshot.getValue(Book.class);
                if(ob!=null)
                {
                    if(!ob.isSold()) {
                        ob.setBookId(dataSnapshot.getKey());
                        bookList.add(ob);
                        adapter.notifyDataSetChanged();
                    }
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
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(bookList.size()==0)
                {
                    Snackbar.make(moreBooksLayout,"No books to show",Snackbar.LENGTH_INDEFINITE).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void showUserSoldOrders() {

        databaseReference=firebaseDatabase.getReference().child("Books");
        databaseReference.orderByChild("sellerId").equalTo(auth.getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Book ob=dataSnapshot.getValue(Book.class);
                if(ob!=null)
                {
                    if(ob.isSold()) {
                        ob.setBookId(dataSnapshot.getKey());
                        bookList.add(ob);
                        adapter.notifyDataSetChanged();
                    }
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
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(bookList.size()==0)
                {
                    Snackbar.make(moreBooksLayout,"No books to show",Snackbar.LENGTH_INDEFINITE).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void showUserBoughtBooks() {

        databaseReference=firebaseDatabase.getReference().child("Books");
        databaseReference.orderByChild("buyerId").equalTo(auth.getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Book ob=dataSnapshot.getValue(Book.class);
                if(ob!=null)
                {
                    if(ob.isSold()) {
                        ob.setBookId(dataSnapshot.getKey());
                        bookList.add(ob);
                        adapter.notifyDataSetChanged();
                    }
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
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(bookList.size()==0)
                {
                    Snackbar.make(moreBooksLayout,"No books to show",Snackbar.LENGTH_INDEFINITE).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void showUserSoldBooks() {

        databaseReference=firebaseDatabase.getReference().child("Books");
        databaseReference.orderByChild("sellerId").equalTo(auth.getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Book ob=dataSnapshot.getValue(Book.class);
                if(ob!=null)
                {
                    if(!ob.isSold()) {
                        ob.setBookId(dataSnapshot.getKey());
                        bookList.add(ob);
                        adapter.notifyDataSetChanged();
                    }
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
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(bookList.size()==0)
                {
                    Snackbar.make(moreBooksLayout,"No books to show",Snackbar.LENGTH_INDEFINITE).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void showUserCart() {

        databaseReference=firebaseDatabase.getReference().child("Cart").child(auth.getCurrentUser().getUid());
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Book ob=dataSnapshot.getValue(Book.class);
                if(ob!=null)
                {
                    if(!ob.isSold()) {
                        ob.setBookId(dataSnapshot.getKey());
                        bookList.add(ob);
                        adapter.notifyDataSetChanged();
                    }
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
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(bookList.size()==0)
                {
                    Snackbar.make(moreBooksLayout,"No books to show",Snackbar.LENGTH_INDEFINITE).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void setBookGenre(String genre) {

        databaseReference=firebaseDatabase.getReference().child("Books");
        databaseReference.orderByChild("genre").equalTo(genre).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Book ob=dataSnapshot.getValue(Book.class);
                if(ob!=null)
                {
                    if(!ob.isSold()) {
                        ob.setBookId(dataSnapshot.getKey());
                        bookList.add(ob);
                        adapter.notifyDataSetChanged();
                    }
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
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(bookList.size()==0)
                {
                    Snackbar.make(moreBooksLayout,"No books to show",Snackbar.LENGTH_INDEFINITE).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void setBooksTopDeals()
    {
        databaseReference=firebaseDatabase.getReference().child("Books");
        databaseReference.orderByChild("price").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Book ob=dataSnapshot.getValue(Book.class);
                if(ob!=null)
                {
                    if(!ob.isSold()) {
                        ob.setBookId(dataSnapshot.getKey());
                        bookList.add(ob);
                        adapter.notifyDataSetChanged();
                    }
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
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(bookList.size()==0)
                {
                    Snackbar.make(moreBooksLayout,"No books to show",Snackbar.LENGTH_INDEFINITE).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void setBooksTrending()
    {
        databaseReference=firebaseDatabase.getReference().child("Books");
        databaseReference.orderByChild("views").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Book ob=dataSnapshot.getValue(Book.class);
                if(ob!=null)
                {
                    if(!ob.isSold()) {
                        ob.setBookId(dataSnapshot.getKey());
                        bookList.add(ob);
                        adapter.notifyDataSetChanged();
                    }
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
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(bookList.size()==0)
                {
                    Snackbar.make(moreBooksLayout,"No books to show",Snackbar.LENGTH_INDEFINITE).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void setBooksFreshDeals()
    {
        databaseReference=firebaseDatabase.getReference().child("Books");
        databaseReference.orderByKey().limitToLast(10).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Book ob=dataSnapshot.getValue(Book.class);
                if(ob!=null)
                {
                    if(!ob.isSold()) {
                        ob.setBookId(dataSnapshot.getKey());
                        bookList.add(ob);
                        adapter.notifyDataSetChanged();
                    }
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
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(bookList.size()==0)
                {
                    Snackbar.make(moreBooksLayout,"No books to show",Snackbar.LENGTH_INDEFINITE).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void setBooksRecommended()
    {
        databaseReference=firebaseDatabase.getReference().child("Books");
        databaseReference.orderByChild("genre").equalTo("Mystery").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Book ob=dataSnapshot.getValue(Book.class);
                if(ob!=null)
                {
                    if(!ob.isSold()) {
                        ob.setBookId(dataSnapshot.getKey());
                        bookList.add(ob);
                        adapter.notifyDataSetChanged();
                    }
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
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(bookList.size()==0)
                {
                    Snackbar.make(moreBooksLayout,"No books to show",Snackbar.LENGTH_INDEFINITE).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
