package com.example.finalsgroupblasting;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.FinalOSBlasting.R;

public class LogIn extends AppCompatActivity {
    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editTextUsername = findViewById(R.id.username_input);
        editTextEmail = findViewById(R.id.email_input);
        editTextPassword = findViewById(R.id.password_input);

        setContentView(com.example.FinalOSBlasting.R.layout.login_ui);

        Button login = findViewById(R.id.login_btn);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LogIn.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LogIn.this, "Login successful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
