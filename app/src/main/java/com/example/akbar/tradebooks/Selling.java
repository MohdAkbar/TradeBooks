package com.example.akbar.tradebooks;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Akbar on 6/30/2018.
 */
public class Selling extends AppCompatActivity {

    Toolbar sellingToolbar;
    AppCompatEditText nameSelling,authorNameSelling,priceSelling,yearSelling,publicationSelling,descriptionSelling;
    ImageView bookPicSelling;
    Button sellSelling;
    Spinner genreSelling;
    String name,author,genre,year,publication,description,price,picUrl;
    int flagPic=0;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Bitmap bitmap;
    Uri selectedImage,picUri;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selling);
        firebaseDatabase=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        sellingToolbar=(Toolbar)findViewById(R.id.sellingToolbar);
        setSupportActionBar(sellingToolbar);
        addItemsToSpinner();
        nameSelling=(AppCompatEditText)findViewById(R.id.nameSelling);
        authorNameSelling=(AppCompatEditText)findViewById(R.id.authorNameSelling);
        priceSelling=(AppCompatEditText)findViewById(R.id.priceSelling);
        yearSelling=(AppCompatEditText)findViewById(R.id.yearSelling);
        publicationSelling=(AppCompatEditText)findViewById(R.id.publicationSelling);
        descriptionSelling=(AppCompatEditText)findViewById(R.id.descriptionSelling);
        bookPicSelling=(ImageView)findViewById(R.id.bookPicSelling);
        bookPicSelling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI),123);
            }
        });
        sellSelling=(Button)findViewById(R.id.sellSelling);
        sellSelling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=nameSelling.getText().toString();
                author=authorNameSelling.getText().toString();
                year=yearSelling.getText().toString();
                publication=publicationSelling.getText().toString();
                description=descriptionSelling.getText().toString();
                price=priceSelling.getText().toString();
                if((TextUtils.isEmpty(name))||(TextUtils.isEmpty(author))||(TextUtils.isEmpty(year))||(TextUtils.isEmpty(publication))||(TextUtils.isEmpty(description))||(TextUtils.isEmpty(price)))
                {
                    Toast.makeText(getApplicationContext(),"Fields are Empty",Toast.LENGTH_SHORT).show();
                }
                else if(year.length()!=4)
                {
                    Toast.makeText(getApplicationContext(),"Wrong Year",Toast.LENGTH_SHORT).show();
                }
                else if(description.length()<30)
                {
                    Toast.makeText(getApplicationContext(),"Description must contain at least 30 characters",Toast.LENGTH_SHORT).show();
                }
                else if(flagPic==0)
                {
                    Toast.makeText(getApplicationContext(),"Please Upload photo",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Accepted",Toast.LENGTH_SHORT).show();
                    storageReference=firebaseStorage.getReference().child("BookCovers").child(selectedImage.getLastPathSegment());
                    storageReference.putFile(selectedImage)
                    .addOnSuccessListener(Selling.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            picUri=taskSnapshot.getDownloadUrl();
                            if (picUri != null) {
                                picUrl=picUri.toString();
                                Book ob=new Book(name,author,year,description,publication,genre,picUrl,Integer.parseInt(price),auth.getCurrentUser().getUid());
                                databaseReference=firebaseDatabase.getReference().child("Books");
                                databaseReference.push().setValue(ob);
                                Toast.makeText(getApplicationContext(),"Book Sold",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(Selling.this,HomePage.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Selling Failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void addItemsToSpinner() {
        genreSelling=(Spinner)findViewById(R.id.genreSelling);
        final ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(Selling.this,R.array.genreArray,android.R.layout.simple_spinner_item);
        genreSelling.setAdapter(adapter);
        genreSelling.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                genre=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getApplicationContext(),genre,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==123)
        {
            if(resultCode==Activity.RESULT_OK)
            {
                selectedImage=data.getData();
                bitmap=null;
                try {
                    bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                    bookPicSelling.setImageBitmap(bitmap);
                    flagPic=1;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Unable to add photo! Retry",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
