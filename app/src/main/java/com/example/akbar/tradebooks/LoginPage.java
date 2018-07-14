package com.example.akbar.tradebooks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Akbar on 6/23/2018.
 */
public class LoginPage extends AppCompatActivity {

    AppCompatEditText emailLogIn,passwordLogIn;
    Button logInLogIn;
    FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null)
        {
            Intent intent=new Intent(getApplicationContext(),HomePage.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.login_page);
        emailLogIn=(AppCompatEditText)findViewById(R.id.emailLogIn);
        passwordLogIn=(AppCompatEditText)findViewById(R.id.passwordLogIn);
        logInLogIn=(Button)findViewById(R.id.logInLogIn);
        logInLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=emailLogIn.getText().toString();
                String password=passwordLogIn.getText().toString();
                if (TextUtils.isEmpty(email)||(!email.contains("@")))
                {
                    Toast.makeText(getApplicationContext(),"Please enter proper email id",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(password)||(password.length()<6))
                {
                    Toast.makeText(getApplicationContext(),"Please enter password at least 6 digits",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    auth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(!task.isSuccessful())
                                    {
                                        Toast.makeText(getApplicationContext(),"Login Failed:"+task.getException(),Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        Intent intent=new Intent(getApplicationContext(),HomePage.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                            });
                }
            }
        });

    }

    public void gotoSignUp(View view) {
        Intent intent=new Intent(getApplicationContext(),SignUpPage.class);
        startActivity(intent);
    }

    public void gotoResetPassword(View view) {

        String email=emailLogIn.getText().toString();
        if (TextUtils.isEmpty(email)||(!email.contains("@")))
        {
            Toast.makeText(getApplicationContext(),"Please enter proper email id",Toast.LENGTH_SHORT).show();
        }
        else
        {
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(LoginPage.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(!task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"Failed to send reset email",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Reset instructions sent",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }
}
