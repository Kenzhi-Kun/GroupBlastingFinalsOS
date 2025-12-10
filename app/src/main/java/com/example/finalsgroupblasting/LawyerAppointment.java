package com.example.finalsgroupblasting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LawyerAppointment extends AppCompatActivity {

    private TextView userNameTextView;
    private ImageView uploadButton;
    private ImageView listOfClients;
    private ImageView appointmentButton;
    private TextView logoutTextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lawyer_appointment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        uploadButton = findViewById(R.id.upload_home_client9);
        listOfClients = findViewById(R.id.files_home_client9);
        logoutTextBtn = findViewById(R.id.logoutTextBtn9);
        userNameTextView = findViewById(R.id.user_name8);
        displayUsername();


        uploadButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(LawyerAppointment.this, "This feature is not currently available.", Toast.LENGTH_SHORT).show();
            }
        });

        listOfClients.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LawyerAppointment.this, ListOfClients.class);
                startActivity(intent);
            }
        });

        logoutTextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent4 = new Intent(LawyerAppointment.this, MainActivity.class);
                startActivity(intent4);
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