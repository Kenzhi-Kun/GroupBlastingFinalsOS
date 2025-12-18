package com.example.finalsgroupblasting;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.HashMap;

public class ListOfClients extends AppCompatActivity {

    private TextView userNameTextView;
    private ImageView fileButton;
    private ImageView listOfClients;
    private ImageView appointmentButton;
    private TextView logoutTextBtn;
    private ListView appointmentList;
    private Button removeTopClientButton;

    // Appointments
    private DatabaseReference acceptedAppointmentsRef;
    private ArrayList<String> appointmentListStrings;
    private ArrayList<String> appointmentKeys;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_list_of_clients);

        // UI elements
        appointmentButton = findViewById(R.id.appointment_home_client7);
        fileButton = findViewById(R.id.file_home_client7);
        listOfClients = findViewById(R.id.files_home_client7);
        logoutTextBtn = findViewById(R.id.logoutTextBtn7);
        appointmentList = findViewById(R.id.accepted_appointment_list);
        removeTopClientButton = findViewById(R.id.done_appointment);
        userNameTextView = findViewById(R.id.user_name15);
        displayUsername();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Navigation
        appointmentButton.setOnClickListener(v -> startActivity(new Intent(ListOfClients.this, LawyerAppointment.class)));
        fileButton.setOnClickListener(v -> startActivity(new Intent(ListOfClients.this, LawyerFiles.class)));
        logoutTextBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ListOfClients.this, MainActivity.class));
            finish();
        });

        // Firebase reference to accepted appointments
        acceptedAppointmentsRef = FirebaseDatabase.getInstance(
                        "https://finalsgroupblasting-6eab4d18-default-rtdb.firebaseio.com/")
                .getReference("appointment").child("Accepted-Appointment");

        // List setup
        appointmentListStrings = new ArrayList<>();
        appointmentKeys = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, appointmentListStrings);
        appointmentList.setAdapter(adapter);

        // Load accepted appointments
        acceptedAppointmentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointmentListStrings.clear();
                appointmentKeys.clear();

                for (DataSnapshot snap : snapshot.getChildren()) {
                    Appointment appointment = snap.getValue(Appointment.class);
                    if (appointment != null) {
                        String txt = appointment.getUser() +
                                "\nDate: " + appointment.getDate() +
                                "\nTime: " + appointment.getTime();
                        appointmentListStrings.add(txt);
                        appointmentKeys.add(snap.getKey());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ListOfClients.this, "Failed to load appointments: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Complete button → remove appointments and send notifications
        removeTopClientButton.setOnClickListener(v -> {
            if (appointmentKeys.isEmpty()) {
                Toast.makeText(ListOfClients.this, "No appointments to complete.", Toast.LENGTH_SHORT).show();
                return;
            }

            for (int i = 0; i < appointmentKeys.size(); i++) {
                String key = appointmentKeys.get(i);
                String clientEmail = appointmentListStrings.get(i).split("\n")[0]; // extract user/email

                // Remove appointment
                acceptedAppointmentsRef.child(key).removeValue();

                // Send notification
                sendNotification(clientEmail, "Appointment Completed",
                        "Your appointment has been completed. Thank you!");
            }

            // Clear local list
            appointmentListStrings.clear();
            appointmentKeys.clear();
            adapter.notifyDataSetChanged();

            Toast.makeText(ListOfClients.this, "All appointments completed and clients notified.", Toast.LENGTH_SHORT).show();
        });
    }

    private void sendNotification(String clientEmail, String title, String message) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance()
                .getReference("users");

        usersRef.orderByChild("email").equalTo(clientEmail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Get the UID of the client
                            String uid = snapshot.getChildren().iterator().next().getKey();
                            if (uid != null) {
                                // Push the notification under the client's UID
                                DatabaseReference notifRef = FirebaseDatabase.getInstance()
                                        .getReference("appointment")
                                        .child("Notifications")
                                        .child("Client-Notifications")
                                        .child(uid) // each client gets their own folder
                                        .push();

                                HashMap<String, Object> notif = new HashMap<>();
                                notif.put("title", title);
                                notif.put("message", message);
                                notif.put("seen", false);
                                notif.put("timestamp", System.currentTimeMillis());

                                notifRef.setValue(notif)
                                        .addOnSuccessListener(aVoid -> Toast.makeText(ListOfClients.this, "Client notified!", Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e -> Toast.makeText(ListOfClients.this, "Failed to notify client: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ListOfClients.this,
                                "Error sending notification: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
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
