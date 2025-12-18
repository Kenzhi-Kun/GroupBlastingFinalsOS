package com.example.finalsgroupblasting;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private TextView userNameTextView;
    private ImageView uploadButton;
    private ImageView filesButton;
    private ImageView appointmentButton;
    private TextView logoutTextBtn;
    private ListView listView;

    private ArrayList<String> notificationList;
    private ArrayAdapter<String> notificationAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home);

        appointmentButton = findViewById(R.id.appointment_home_client);
        uploadButton = findViewById(R.id.upload_home_client);
        filesButton = findViewById(R.id.files_home_client);
        logoutTextBtn = findViewById(R.id.logoutTextBtn);
        listView = findViewById(R.id.gayNotification);

        notificationList = new ArrayList<>();
        notificationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notificationList);
        listView.setAdapter(notificationAdapter);

        loadNotifications();

        userNameTextView = findViewById(R.id.user_name);

        displayUsername();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        appointmentButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ClientAppointmentTab.class);
                startActivity(intent);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent2 = new Intent(HomeActivity.this, ClientUpload.class);
                startActivity(intent2);
            }
        });

        filesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent3 = new Intent(HomeActivity.this, ClientFiles.class);
                startActivity(intent3);
            }
        });

        logoutTextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent4 = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent4);
                finish();
            }
        });
        }

    private void loadNotifications() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        String uid = user.getUid();

        DatabaseReference notifRef = FirebaseDatabase.getInstance(
                        "https://finalsgroupblasting-6eab4d18-default-rtdb.firebaseio.com/"
                ).getReference("appointment")
                .child("Notifications")
                .child(uid); // <--- read notifications specific to this client

        notifRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notificationList.clear();
                for (DataSnapshot notifSnapshot : snapshot.getChildren()) {
                    String title = notifSnapshot.child("title").getValue(String.class);
                    String message = notifSnapshot.child("message").getValue(String.class);

                    if (title != null && message != null) {
                        notificationList.add(title + "\n" + message);
                    }
                }
                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Failed to load notifications: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Optional: click to delete a notification
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (position < 0) return;

            notifRef.orderByChild("timestamp").limitToFirst(position + 1)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                child.getRef().removeValue();
                                break; // remove only the clicked one
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
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