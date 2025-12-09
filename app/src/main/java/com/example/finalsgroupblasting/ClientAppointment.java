package com.example.finalsgroupblasting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ClientAppointment extends AppCompatActivity {
    private ImageView uploadButton;
    private ImageView filesButton;
    private TextView logoutButton;

    private TextView userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_client_appointment);
        uploadButton = findViewById(R.id.upload_home_client4);
        filesButton = findViewById(R.id.files_home_client4);
        logoutButton = findViewById(R.id.logoutTextBtn4);
        userName = findViewById(R.id.user_name5);
        displayUsername();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientAppointment.this, ClientUpload.class);
                startActivity(intent);
                finish();
            }
        });

        filesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ClientAppointment.this, ClientFiles.class);
                startActivity(intent2);
                finish();
            }

        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(ClientAppointment.this, MainActivity.class);
                startActivity(intent3);
                finish();
            }
        });



    }
    public void displayUsername() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String userNameHere = user.getDisplayName();
            if (userNameHere != null && !userNameHere.isEmpty()) {
                userName.setText(userNameHere);
            } else {
                userName.setText("Anonymous");
            }
        } else {
            userName.setText("Anonymous");
        }
    }
}