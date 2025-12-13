package com.example.finalsgroupblasting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class LawyerAppointment extends AppCompatActivity {

    private TextView userNameTextView;
    private ImageView uploadButton;
    private ImageView listOfClients;
    private TextView logoutTextBtn;
    private Button appointmentButton;
    private Button rejectButton;
    private ListView appointmentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_lawyer_appointment);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        uploadButton = findViewById(R.id.file_home_client9);
        listOfClients = findViewById(R.id.files_home_client9);
        logoutTextBtn = findViewById(R.id.logoutTextBtn9);
        appointmentButton = findViewById(R.id.accept_button);
        rejectButton = findViewById(R.id.reject_btn);
        userNameTextView = findViewById(R.id.user_name8);
        appointmentList = findViewById(R.id.appointment_list);
        displayUsername();


        uploadButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent3 = new Intent(LawyerAppointment.this, LawyerFiles.class);
                startActivity(intent3);
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


        ArrayList<String> list = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        appointmentList.setAdapter(adapter);

        DatabaseReference database = FirebaseDatabase.getInstance("https://finalsgroupblasting-6eab4d18-default-rtdb.firebaseio.com/").getReference("appointment").child("Appointment-Folder");
        DatabaseReference database2 = FirebaseDatabase.getInstance("https://finalsgroupblasting-6eab4d18-default-rtdb.firebaseio.com/").getReference("appointment").child("Accepted-Appointment");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Appointment appointment = snapshot1.getValue(Appointment.class);
                    if (appointment != null) {
                        String txt = "Name: " + appointment.getUser() + "\nDate: " + appointment.getDate() + "\nTime: " + appointment.getTime();
                        list.add(txt);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LawyerAppointment.this, "Failed to load appointments: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        appointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.isEmpty()) {
                    Toast.makeText(LawyerAppointment.this, "No appointments to accept.", Toast.LENGTH_SHORT).show();
                    return;
                }


                database.orderByKey().limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            Toast.makeText(LawyerAppointment.this, "No appointments found in database.", Toast.LENGTH_SHORT).show();
                            return;
                        }


                        DataSnapshot firstAppointmentSnapshot = snapshot.getChildren().iterator().next();
                        Appointment appointment = firstAppointmentSnapshot.getValue(Appointment.class);
                        String appointmentKey = firstAppointmentSnapshot.getKey();

                        if (appointment != null && appointmentKey != null) {

                            database2.child(appointmentKey).setValue(appointment).addOnSuccessListener(aVoid -> {

                                firstAppointmentSnapshot.getRef().removeValue().addOnSuccessListener(aVoid1 -> {
                                    Toast.makeText(LawyerAppointment.this, "Appointment Accepted", Toast.LENGTH_SHORT).show();
                                }).addOnFailureListener(e -> {
                                    Toast.makeText(LawyerAppointment.this, "Failed to remove original appointment.", Toast.LENGTH_SHORT).show();
                                    database2.child(appointmentKey).removeValue();
                                });
                            }).addOnFailureListener(e -> {
                                Toast.makeText(LawyerAppointment.this, "Failed to accept appointment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LawyerAppointment.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.isEmpty()) {
                    Toast.makeText(LawyerAppointment.this, "No appointments to reject.", Toast.LENGTH_SHORT).show();
                    return;
                }

                database.orderByKey().limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            Toast.makeText(LawyerAppointment.this, "No appointments found in database.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        DataSnapshot firstAppointmentSnapshot = snapshot.getChildren().iterator().next();
                        firstAppointmentSnapshot.getRef().removeValue()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(LawyerAppointment.this, "Appointment Rejected", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(LawyerAppointment.this, "Failed to reject appointment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LawyerAppointment.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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