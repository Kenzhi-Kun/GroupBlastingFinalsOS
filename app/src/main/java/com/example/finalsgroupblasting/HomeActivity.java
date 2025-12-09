package com.example.finalsgroupblasting;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalsgroupblasting.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private TextView userNameTextView;
    private ImageView uploadButton;
    private ImageView filesButton;
    private ImageView appointmentButton;

    private TextView logoutTextBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        appointmentButton = findViewById(R.id.appointment_home_client);
        uploadButton = findViewById(R.id.upload_home_client);
        filesButton = findViewById(R.id.files_home_client);
        logoutTextBtn = findViewById(R.id.logoutTextBtn);

        userNameTextView = findViewById(R.id.user_name);

        displayUsername();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        appointmentButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "This feature is not currently available.", Toast.LENGTH_SHORT).show();

            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ClientUpload.class);
            }
        });

        filesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "This feature is not currently available.", Toast.LENGTH_SHORT).show();
            }
        });

        logoutTextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        }

    private void displayUsername() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userName = user.getDisplayName();
            if (userName != null && !userName.isEmpty()) {
                userNameTextView.setText(userName);
            } else {
                userNameTextView.setText("Anonymous");
            }
        } else {
            userNameTextView.setText("Anonymous");
        }
    }
}