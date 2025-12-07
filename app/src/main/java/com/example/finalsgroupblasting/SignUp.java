package com.example.finalsgroupblasting;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalsgroupblasting.R;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextEmail;
    private Button signUpButton;

    private EditText editTextPassword;
    private FirebaseAuth mAuth;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_ui);
        editTextUsername = findViewById(R.id.userNameSignUpInput);
        editTextEmail = findViewById(R.id.emailSignUpInput);
        editTextPassword = findViewById(R.id.passwordSignUpInput);
        signUpButton = findViewById(R.id.button);
        mAuth = FirebaseAuth.getInstance();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = editTextUsername.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if(userName.isEmpty() || email.isEmpty() || password.isEmpty()){
                    Toast.makeText(SignUp.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }

                if(userName.length() < 4){
                    Toast.makeText(SignUp.this, "Username must be at least 4 characters", Toast.LENGTH_SHORT).show();
                }

                if(!email.contains("@")){
                    Toast.makeText(SignUp.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                }

                if(password.length() < 8){
                    Toast.makeText(SignUp.this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                }
            }
        });
     }
}
