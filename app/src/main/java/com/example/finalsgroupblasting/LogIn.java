package com.example.finalsgroupblasting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.FinalOSBlasting.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editTextEmail = findViewById(R.id.email_input);
        editTextPassword = findViewById(R.id.password_input);
        Button login = findViewById(R.id.login_button);
        auth = FirebaseAuth.getInstance();

        setContentView(com.example.FinalOSBlasting.R.layout.log_in);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                loginUser(email, password);

            }
        });
    }

    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LogIn.this, "Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LogIn.this, MainMenuUI.class));
                finish();
            }
        });
    }
}
