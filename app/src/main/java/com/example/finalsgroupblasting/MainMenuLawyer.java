package com.example.finalsgroupblasting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.activity.EdgeToEdge;
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

public class MainMenuLawyer extends AppCompatActivity {

    private TextView userNameTextView;
    private ImageView filesButton;
    private ImageView listOfClients;
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
        setContentView(R.layout.activity_main_menu_lawyer);

        appointmentButton = findViewById(R.id.appointment_home_client8);
        filesButton = findViewById(R.id.file_home_client8);
        listOfClients = findViewById(R.id.files_home_client8);
        logoutTextBtn = findViewById(R.id.logoutTextBtn8);
        listView = findViewById(R.id.gayNotifs);

        notificationList = new ArrayList<>();
        notificationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notificationList);
        listView.setAdapter(notificationAdapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userNameTextView = findViewById(R.id.user_name18);
        displayUsername();
        loadClientNotifications();

        appointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainMenuLawyer.this, LawyerAppointment.class);
                startActivity(intent2);
            }
        });

        listOfClients.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuLawyer.this, ListOfClients.class);
                startActivity(intent);
            }
        });

        filesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent3 = new Intent (MainMenuLawyer.this, LawyerFiles.class);
                startActivity(intent3);
            }
        });

        logoutTextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent4 = new Intent(MainMenuLawyer.this, MainActivity.class);
                startActivity(intent4);
                finish();
            }
        });


    }

    private void loadClientNotifications() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        DatabaseReference notifRef = FirebaseDatabase.getInstance(
                        "https://finalsgroupblasting-6eab4d18-default-rtdb.firebaseio.com/"
                ).getReference("appointment")
                .child("Notifications")
                .child("Lawyer-Notifications"); // client notifications intended for lawyer

        notifRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot) {
                notificationList.clear();
                for (com.google.firebase.database.DataSnapshot notifSnapshot : snapshot.getChildren()) {
                    String message = notifSnapshot.child("message").getValue(String.class);
                    if (message != null) {
                        notificationList.add(message);
                    }
                }
                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull com.google.firebase.database.DatabaseError error) {
                Toast.makeText(MainMenuLawyer.this, "Failed to load notifications: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Optional: click to delete
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (position < 0) return;

            notifRef.orderByChild("timestamp").limitToFirst(position + 1)
                    .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot) {
                            for (com.google.firebase.database.DataSnapshot child : snapshot.getChildren()) {
                                child.getRef().removeValue();
                                break; // remove only the clicked one
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull com.google.firebase.database.DatabaseError error) {}
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