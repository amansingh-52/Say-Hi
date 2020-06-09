package com.example.sayhi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signIn extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        CardView signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText emailID = findViewById(R.id.emailSignIn);
                final EditText password1 = findViewById(R.id.passwordSignIn);
                if (emailID.getText().toString().trim().equals("")) {
                    Toast.makeText(signIn.this, "email ID cannot be blank", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!emailID.getText().toString().contains("@") && !(emailID.getText().toString().contains(".com") || emailID.getText().toString().contains(".in")
                        || emailID.getText().toString().contains(".edu"))) {
                    Toast.makeText(signIn.this, "Please enter a valid e-mail id", Toast.LENGTH_LONG).show();
                    return;
                }
                if (password1.getText().toString().trim().equals("")) {
                    Toast.makeText(signIn.this, "Password cannot be blank", Toast.LENGTH_LONG).show();
                    return;
                }
                if (password1.getText().toString().length() <= 5) {
                    Toast.makeText(signIn.this, "Please enter at least 6 digit password", Toast.LENGTH_LONG).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.signInWithEmailAndPassword(emailID.getText().toString(), password1.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isComplete()) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(signIn.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(signIn.this, "Login not successful\nPlease check email and password", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        });

        TextView signUpText = findViewById(R.id.SignUpTextView);
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signIn.this,signUp.class);
                startActivity(intent);
            }
        });

    }
}
