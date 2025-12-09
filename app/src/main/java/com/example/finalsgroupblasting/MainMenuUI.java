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

import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainMenuUI extends AppCompatActivity {
    private TextView userLawTextView;

    private ImageView appointmentButton;
    private ImageView uploadButton;
    private ImageView filesButton;
    private TextView logoutButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_ui);



        userLawTextView = findViewById(R.id.user_law);

        uploadButton = findViewById(R.id.upload_menu_lawyer);
        filesButton = findViewById(R.id.files_menu_lawyer);
        appointmentButton = findViewById(R.id.appointment_menu_lawyer);
        logoutButton = findViewById(R.id.logoutButton);

        displayUsername();

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuUI.this, ClientUpload.class);
                startActivity(intent);
                finish();
            }
        });

        filesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainMenuUI.this, "This feature is currently not available", Toast.LENGTH_SHORT).show();
            }
        });


        appointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(MainMenuUI.this, LawyerAppointment.class);
                startActivity(intent3);
                finish();

            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent4 = new Intent(MainMenuUI.this, MainActivity.class);
                startActivity(intent4);
                finish();
            }
        });
    }

    public void displayUsername() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String userName = user.getDisplayName();
            if (userName != null && !userName.isEmpty()) {
                userLawTextView.setText(userName);
            } else {
                userLawTextView.setText("Anonymous");
            }
        } else {
            userLawTextView.setText("Anonymous");
        }
    }

}
