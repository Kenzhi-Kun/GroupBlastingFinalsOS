package com.example.finalsgroupblasting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClientFiles extends AppCompatActivity {

    private TextView userNameTextView;
    private ImageView uploadButton;
    private ImageView filesButton;
    private ImageView appointmentButton;
    private TextView logoutTextBtn;
    private TextView userName;
    private TextView affidavit1, affidavit2, affidavit3;
    private TextView contract1, contract2, contract3;
    private TextView will1, will2, will3;
    private TextView sender1, sender2, sender3, sender4, sender5, sender6, sender7, sender8, sender9;

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

        affidavit1 = findViewById(R.id.affidavitFile1);
        affidavit2 = findViewById(R.id.affidavitFile2);
        affidavit3 = findViewById(R.id.affidavitFile3);

        contract1 = findViewById(R.id.contractFile1);
        contract2 = findViewById(R.id.contractFile2);
        contract3 = findViewById(R.id.contractFile3);

        will1 = findViewById(R.id.willFile1);
        will2 = findViewById(R.id.willFile2);
        will3 = findViewById(R.id.willFile3);

        sender1 = findViewById(R.id.sender1);
        sender2 = findViewById(R.id.sender2);
        sender3 = findViewById(R.id.sender3);
        sender4 = findViewById(R.id.sender4);
        sender5 = findViewById(R.id.sender5);
        sender6 = findViewById(R.id.sender6);
        sender7 = findViewById(R.id.sender7);
        sender8 = findViewById(R.id.sender8);
        sender9 = findViewById(R.id.sender9);



        displayUsername();
        displayUsername();
        fetchClientFiles();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        appointmentButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ClientFiles.this, ClientAppointmentTab.class);
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
    private void fetchClientFiles() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        String clientId = user.getUid();
        DatabaseReference clientRef = FirebaseDatabase.getInstance(
                        "https://finalsgroupblasting-6eab4d18-default-rtdb.firebaseio.com/")
                .getReference("appointment")  // match what ClientUpload uses
                .child("Client-Files")
                .child(clientId);

        clientRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) return;

                // Affidavit
                int affIndex = 0;
                if (snapshot.hasChild("affidavit")) {
                    for (DataSnapshot file : snapshot.child("affidavit").getChildren()) {
                        String fileName = file.child("fileName").getValue(String.class);
                        String email = file.child("uploadedBy").getValue(String.class);
                        if (affIndex == 0) {
                            affidavit1.setText(fileName);
                            sender1.setText(email);
                        }
                        else if (affIndex == 1) {
                            affidavit2.setText(fileName);
                            sender2.setText(email);

                        }
                        else if (affIndex == 2) {
                            affidavit3.setText(fileName);
                            sender3.setText(email);
                        }
                        affIndex++;
                    }
                }

                // Contracts
                int contIndex = 0;
                if (snapshot.hasChild("contracts")) {
                    for (DataSnapshot file : snapshot.child("contracts").getChildren()) {
                        String fileName = file.child("fileName").getValue(String.class);
                        String email = file.child("uploadedBy").getValue(String.class);
                        if (contIndex == 0) {
                            contract1.setText(fileName);
                            sender4.setText(email);
                        }
                        else if (contIndex == 1) {
                            contract2.setText(fileName);
                            sender5.setText(email);
                        }
                        else if (contIndex == 2) {
                            contract3.setText(fileName);
                            sender6.setText(email);
                        }
                        contIndex++;
                    }
                }

                // Wills
                int willIndex = 0;
                if (snapshot.hasChild("will")) {
                    for (DataSnapshot file : snapshot.child("will").getChildren()) {
                        String fileName = file.child("fileName").getValue(String.class);
                        String email = file.child("uploadedBy").getValue(String.class);
                        if (willIndex == 0) {
                            will1.setText(fileName);
                            sender7.setText(email);
                        }
                        else if (willIndex == 1) {
                            will2.setText(fileName);
                            sender8.setText(email);
                        }
                        else if (willIndex == 2) {
                            will3.setText(fileName);
                            sender9.setText(email);
                        }
                        willIndex++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
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