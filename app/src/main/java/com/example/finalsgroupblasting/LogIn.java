package com.example.finalsgroupblasting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LogIn extends AppCompatActivity {
    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;
    private EditText userNameInput;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        mAuth = FirebaseAuth.getInstance();

        userNameInput = findViewById(R.id.username_input);
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.login_progress_bar);

        loginButton.setOnClickListener(v -> {
            validateAndLogin();
        });
    }

    private void validateAndLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        String username = userNameInput.getText().toString().trim();

        if (username.isEmpty()) {
            userNameInput.setError("Username is required");
            userNameInput.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            emailInput.setError("Email is required");
            emailInput.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Please enter a valid email");
            emailInput.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordInput.setError("Password is required");
            passwordInput.requestFocus();
            return;
        }

        loginUser(email, password);
    }

    private void loginUser(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);
                    loginButton.setEnabled(true);

                    if (task.isSuccessful()) {
                        String lawPass = "lawgiver069";
                        String southPass = "Timog";

                        if (password.equals(lawPass)) {
                            Toast.makeText(LogIn.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LogIn.this, MainMenuUI.class);
                            startActivity(intent);
                            finish();
                        } else if (password.equals(southPass)) {
                            Toast.makeText(LogIn.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            Intent intent2 = new Intent(LogIn.this, HomeActivity.class);
                            startActivity(intent2);
                            finish();
                        } else {
                            Toast.makeText(LogIn.this, "Login successful, but role not recognized.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(LogIn.this, "Authentication Failed: " + Objects.requireNonNull(task.getException()).getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
