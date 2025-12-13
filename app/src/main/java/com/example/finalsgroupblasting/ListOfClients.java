package com.example.finalsgroupblasting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;import java.util.Date;
import java.util.Locale;

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

public class ListOfClients extends AppCompatActivity {

    private TextView userNameTextView;
    private ImageView uploadButton;
    private ImageView listOfClients;
    private ImageView appointmentButton;
    private TextView logoutTextBtn;
    private ListView appointmentList;
    private Button removeTopClientButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_list_of_clients);

        appointmentButton = findViewById(R.id.appointment_home_client7);
        uploadButton = findViewById(R.id.upload_home_client7);
        listOfClients = findViewById(R.id.files_home_client7);
        logoutTextBtn = findViewById(R.id.logoutTextBtn7);
        appointmentList = findViewById(R.id.accepted_appointment_list);
        removeTopClientButton = findViewById(R.id.done_appointment);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userNameTextView = findViewById(R.id.user_name15);
        displayUsername();

        appointmentButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ListOfClients.this, LawyerAppointment.class);
                startActivity(intent);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(ListOfClients.this, "This feature is not currently available.", Toast.LENGTH_SHORT).show();
            }
        });

        logoutTextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent4 = new Intent(ListOfClients.this, MainActivity.class);
                startActivity(intent4);
                finish();
            }
        });

        ArrayList<String> displayList = new ArrayList<>();
        ArrayList<Appointment> appointments = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, displayList);
        appointmentList.setAdapter(adapter);
        appointmentList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        ArrayList<String> selectedAppointmentKeys = new ArrayList<>();

        DatabaseReference database = FirebaseDatabase.getInstance("https://finalsgroupblasting-6eab4d18-default-rtdb.firebaseio.com/").getReference("appointment").child("Accepted-Appointment");


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointments.clear();
                displayList.clear();

                for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                    Appointment appointment = appointmentSnapshot.getValue(Appointment.class);
                    if (appointment != null) {
                        appointment.setKey(appointmentSnapshot.getKey());
                        appointments.add(appointment);
                    }
                }


                Collections.sort(appointments, (a1, a2) -> {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy hh:mm a", Locale.US);
                    try {
                        Date d1 = sdf.parse(a1.getDate() + " " + a1.getTime());
                        Date d2 = sdf.parse(a2.getDate() + " " + a2.getTime());
                        return d1.compareTo(d2);
                    } catch (ParseException e) {
                        return 0;
                    }
                });

                for (Appointment sortedAppointment : appointments) {
                    String txt = "Name: " + sortedAppointment.getUser() + "\nDate: " + sortedAppointment.getDate() + "\nTime: " + sortedAppointment.getTime();
                    displayList.add(txt);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ListOfClients.this, "Failed to load appointments: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        appointmentList.setOnItemClickListener((parent, view, position, id) -> {
            if (position >= 0 && position < appointments.size()) {
                Appointment clickedAppointment = appointments.get(position);
                String clickedKey = clickedAppointment.getKey();


                if (selectedAppointmentKeys.contains(clickedKey)) {
                    selectedAppointmentKeys.remove(clickedKey);
                } else {

                    selectedAppointmentKeys.add(clickedKey);
                }
                Toast.makeText(ListOfClients.this, selectedAppointmentKeys.size() + " item(s) selected.", Toast.LENGTH_SHORT).show();
            }
        });



        removeTopClientButton.setOnClickListener(v -> {

            if (selectedAppointmentKeys.isEmpty()) {
                Toast.makeText(ListOfClients.this, "Please select one or more appointments to mark as done.", Toast.LENGTH_SHORT).show();
                return;
            }


            for (String keyToRemove : selectedAppointmentKeys) {
                // Remove each item from Firebase
                database.child(keyToRemove).removeValue();
            }


            Toast.makeText(ListOfClients.this, selectedAppointmentKeys.size() + " appointment(s) marked as done.", Toast.LENGTH_SHORT).show();


            selectedAppointmentKeys.clear();
            appointmentList.clearChoices();

            adapter.notifyDataSetChanged();
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
