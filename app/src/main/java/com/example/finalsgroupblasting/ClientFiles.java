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

public class ClientFiles extends AppCompatActivity {

    private TextView userNameTextView;
    private ImageView uploadButton;
    private ImageView filesButton;
    private ImageView appointmentButton;
    private TextView logoutTextBtn;
    private TextView userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_client_files);

        appointmentButton = findViewById(R.id.appointment_home_client6);
        uploadButton = findViewById(R.id.upload_home_client6);
        filesButton = findViewById(R.id.files_home_client6);
        logoutTextBtn = findViewById(R.id.logoutTextBtn6);
        userName = findViewById(R.id.user_name13);

        displayUsername();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        appointmentButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ClientFiles.this, ClientAppointment.class);
                startActivity(intent);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent2 = new Intent(ClientFiles.this, ClientUpload.class);
                startActivity(intent2);
            }
        });

        logoutTextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent4 = new Intent(ClientFiles.this, MainActivity.class);
                startActivity(intent4);
                finish();
            }
        });
    }

    //Methods here TwT

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