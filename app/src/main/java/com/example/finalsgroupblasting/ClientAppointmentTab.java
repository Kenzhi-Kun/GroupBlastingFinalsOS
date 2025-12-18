package com.example.finalsgroupblasting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClientAppointmentTab extends ComponentActivity {
    private TextView userNameTextView;
    private TextView uploadButton;
    private TextView logoutTextBtn;
    private Button appointmentButton;
    private ListView appointmentList;
    private TextView filesButton;

    private ArrayList<String> appointmentDisplayList;
    private ArrayAdapter<String> adapter;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_client_appointment_tab);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        uploadButton = findViewById(R.id.uploadIconText);
        logoutTextBtn = findViewById(R.id.logoutTextBtn9);
        appointmentButton = findViewById(R.id.appointmentButton);
        userNameTextView = findViewById(R.id.user_name8);
        appointmentList = findViewById(R.id.userListOfAppointment);
        filesButton = findViewById(R.id.filesIconText);

        appointmentDisplayList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, appointmentDisplayList);
        appointmentList.setAdapter(adapter);

        database = FirebaseDatabase.getInstance("https://finalsgroupblasting-6eab4d18-default-rtdb.firebaseio.com/")
                .getReference("appointment")
                .child("Appointment-Folder");

        fetchUserAppointments();

        appointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientAppointmentTab.this, ClientAppointment.class);
                startActivity(intent);
            }
        });

        displayUsername();

        uploadButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent3 = new Intent(ClientAppointmentTab.this, ClientUpload.class);
                startActivity(intent3);
            }
        });

        filesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent3 = new Intent(ClientAppointmentTab.this, ClientFiles.class);
                startActivity(intent3);
            }
        });

        logoutTextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent4 = new Intent(ClientAppointmentTab.this, MainActivity.class);
                startActivity(intent4);
                finish();
            }
        });
    }

    private void fetchUserAppointments() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        final String userEmail = currentUser.getEmail();

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointmentDisplayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String apptUser = dataSnapshot.child("user").getValue(String.class);
                    String date = dataSnapshot.child("date").getValue(String.class);
                    String time = dataSnapshot.child("time").getValue(String.class);
                    String reason = dataSnapshot.child("reason").getValue(String.class);

                    if (apptUser != null && userEmail != null && apptUser.equalsIgnoreCase(userEmail)) {
                        String displayString = "Date: " + date + "\nTime: " + time + "\nReason: " + (reason != null ? reason : "N/A");
                        appointmentDisplayList.add(displayString);
                    }
                }
                adapter.notifyDataSetChanged();
                if (appointmentDisplayList.isEmpty()) {
                    Toast.makeText(ClientAppointmentTab.this, "No appointments found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ClientAppointmentTab.this, "Failed to load: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
