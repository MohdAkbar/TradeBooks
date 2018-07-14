package com.example.akbar.tradebooks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Akbar on 6/23/2018.
 */
public class SignUpPage extends AppCompatActivity {

    AppCompatEditText firstNameSignUp,lastNameSignUp,phoneSignUp,emailSignUp,passwordSignUp;
    Button registerSignUp;
    ProgressBar progressSignUp;
    FirebaseAuth auth;
    Toolbar signUpToolbar;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);
        firstNameSignUp=(AppCompatEditText)findViewById(R.id.firstNameSignUp);
        lastNameSignUp=(AppCompatEditText)findViewById(R.id.lastNameSignUp);
        phoneSignUp=(AppCompatEditText)findViewById(R.id.phoneSignUp);
        emailSignUp=(AppCompatEditText)findViewById(R.id.emailSignUp);
        passwordSignUp=(AppCompatEditText)findViewById(R.id.passwordSignUp);
        registerSignUp=(Button)findViewById(R.id.registerSignUp);
        progressSignUp=(ProgressBar)findViewById(R.id.progressSignUp);
        signUpToolbar=(Toolbar)findViewById(R.id.signUpToolbar);
        setSupportActionBar(signUpToolbar);
        auth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        registerSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String firstName=firstNameSignUp.getText().toString().trim();
                final String lastName=lastNameSignUp.getText().toString().trim();
                final String phone=phoneSignUp.getText().toString();
                final String email=emailSignUp.getText().toString();
                String password=passwordSignUp.getText().toString();
                if(TextUtils.isEmpty(firstName)||(TextUtils.isEmpty(lastName)))
                {
                    Toast.makeText(getApplicationContext(),"Please enter proper name",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(phone)||(phone.length()!=10))
                {
                    Toast.makeText(getApplicationContext(),"Please enter proper phoneNumber",Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(email)||(!email.contains("@")))
                {
                    Toast.makeText(getApplicationContext(),"Please enter proper email id",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(password)||(password.length()<6))
                {
                    Toast.makeText(getApplicationContext(),"Please enter password at least 6 digits",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    auth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(SignUpPage.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(SignUpPage.this,"Task Complete:"+task.isSuccessful(),Toast.LENGTH_SHORT).show();
                                    progressSignUp.setVisibility(View.GONE);
                                    if(!task.isSuccessful())
                                    {
                                        Toast.makeText(SignUpPage.this,"Fail to Register "+task.getException(),Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        String userId=auth.getCurrentUser().getUid();
                                        databaseReference=firebaseDatabase.getReference().child("Users").child(userId);
                                        User ob=new User(firstName+" "+lastName,email,phone);
                                        databaseReference.push().setValue(ob);
                                        startActivity(new Intent(SignUpPage.this,LoginPage.class));
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }


}




