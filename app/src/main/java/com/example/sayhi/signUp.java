package com.example.sayhi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class signUp extends AppCompatActivity {

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        CardView signUpButton = findViewById(R.id.signUp_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText emailID = findViewById(R.id.emailEditText);
                EditText password1 = findViewById(R.id.password);
                EditText password2 = findViewById(R.id.passwordReCheck);
                EditText name = findViewById(R.id.takeNameEditText);
                final Information information = new Information();
                if (emailID.getText().toString().trim().equals("")) {
                    Toast.makeText(signUp.this, "email ID cannot be blank", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!emailID.getText().toString().contains("@") && !(emailID.getText().toString().contains(".com") || emailID.getText().toString().contains(".in")
                        || emailID.getText().toString().contains(".edu"))) {
                    Toast.makeText(signUp.this, "Please enter a valid e-mail id", Toast.LENGTH_LONG).show();
                    return;
                }
                if (password1.getText().toString().equals("")) {
                    Toast.makeText(signUp.this, "Password cannot be blank", Toast.LENGTH_LONG).show();
                    return;
                }
                if (password1.getText().toString().length() <= 5) {
                    Toast.makeText(signUp.this, "Please enter at least 6 digit password", Toast.LENGTH_LONG).show();
                    return;
                }
                if (password2.getText().toString().equals("")) {
                    Toast.makeText(signUp.this, "Password didn't match\nPlease re enter", Toast.LENGTH_LONG).show();
                    return;
                }
                if (password1.getText().toString().equals(password2.getText().toString())) {

                    information.name = name.getText().toString();
                    information.email = emailID.getText().toString();
                    information.verified = "no";
                    firebaseAuth.createUserWithEmailAndPassword(emailID.getText().toString(), password1.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        firebaseUser = firebaseAuth.getCurrentUser();
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                        databaseReference.child("User").child(firebaseUser.getUid()).setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Intent intent = new Intent(signUp.this,MainActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(signUp.this, "Email already used!", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(signUp.this, "Password didn't match\nPlease re enter", Toast.LENGTH_LONG).show();
                    try {
                        password1.setText("");
                        password2.setText("");
                    } catch (NullPointerException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        TextView signIN = findViewById(R.id.signInText);
        signIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signUp.this, signIn.class);
                startActivity(intent);
            }
        });

    }
}
