package com.example.finalsgroupblasting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ClientUpload extends AppCompatActivity {

    private ImageView appointmentButton;
    private ImageView uploadButton;
    private ImageView filesButton;
    private TextView logoutButton;
    private TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_client_upload);

        appointmentButton = findViewById(R.id.appointment_home_client3);
        uploadButton = findViewById(R.id.upload_home_client3);
        filesButton = findViewById(R.id.files_home_client3);
        logoutButton = findViewById(R.id.logoutTextBtn3);
        userName = findViewById(R.id.user_name7);
        displayUsername();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        filesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ClientUpload.this, ClientFiles.class);
                startActivity(intent2);
                finish();
            }

        });

        appointmentButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ClientUpload.this, ClientAppointment.class);
                startActivity(intent);
            }
        });




        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent3 = new Intent(ClientUpload.this, MainActivity.class);
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