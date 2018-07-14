package com.example.akbar.tradebooks;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Akbar on 7/4/2018.
 */
public class ContactSeller extends AppCompatActivity {

    Button callContactSeller,messageContactSeller;
    Toolbar contactSellerToolbar;
    String userId,bookId;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth auth;
    DatabaseReference databaseReference,reference2;
    String name,phone,email;
    AppCompatEditText messageToSend;
    Book currentBook;
    int flag=0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_seller);
        contactSellerToolbar=(Toolbar)findViewById(R.id.contactSellerToolbar);
        contactSellerToolbar.setTitle("Contact the Seller");
        setSupportActionBar(contactSellerToolbar);
        currentBook=new Book();
        firebaseDatabase=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        callContactSeller=(Button)findViewById(R.id.callContactSeller);
        messageContactSeller=(Button)findViewById(R.id.messageContactSeller);
        Intent i=getIntent();
        userId=i.getStringExtra("seller");
        bookId=i.getStringExtra("bookId");
        Toast.makeText(getApplicationContext(),bookId,Toast.LENGTH_LONG).show();
        getUserInfo();
        updateBookInfo();
        callContactSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+phone));
                if(intent.resolveActivity(getPackageManager())!=null)
                {
                    startActivity(intent);
                }
            }
        });
        messageContactSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmsManager manager=SmsManager.getDefault();
                manager.sendTextMessage(phone,null,messageToSend.getText().toString(),null,null);
                Toast.makeText(getApplicationContext(),"Message Sent",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateBookInfo() {
        databaseReference=firebaseDatabase.getReference().child("Books");
        databaseReference.orderByKey().equalTo(bookId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot child : dataSnapshot.getChildren())
                {
                    if(child!=null)
                    {
                        if (child.getKey().equals("name")) {
                            currentBook.setName(child.getValue().toString());
                            flag++;
                        } else if (child.getKey().equals("author")) {
                            currentBook.setAuthor(child.getValue().toString());
                            flag++;
                        } else if (child.getKey().equals("description")) {
                            currentBook.setDescription(child.getValue().toString());
                            flag++;
                        } else if (child.getKey().equals("publication")) {
                            currentBook.setPublication(child.getValue().toString());
                            flag++;
                        } else if (child.getKey().equals("genre")) {
                            currentBook.setGenre(child.getValue().toString());
                            flag++;
                        } else if (child.getKey().equals("price")) {
                            currentBook.setPrice(Integer.parseInt(child.getValue().toString()));
                            flag++;
                        } else if (child.getKey().equals("photoUrl")) {
                            currentBook.setPhotoUrl(child.getValue().toString());
                            flag++;
                        } else if (child.getKey().equals("sellerId")) {
                            flag++;
                            currentBook.setSellerId(child.getValue().toString());


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
                    setSoldToTrue();
                    Toast.makeText(getApplicationContext(),"FLAG=12",Toast.LENGTH_LONG).show();
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

    private void setSoldToTrue() {
        reference2=firebaseDatabase.getReference().child("Books").child(bookId);
        currentBook.setSold(true);
        currentBook.setBuyerId(auth.getCurrentUser().getUid());
        reference2.setValue(currentBook);
    }


    @Override
    public void onBackPressed() {
        Intent intent=new Intent(ContactSeller.this,HomePage.class);
        startActivity(intent);
    }

    private void getUserInfo() {
        databaseReference=firebaseDatabase.getReference().child("Users").child(userId);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User ob=dataSnapshot.getValue(User.class);
                if(ob!=null)
                {
                    name=ob.getName();
                    email=ob.getEmail();
                    phone=ob.getPhoneNumber();
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
