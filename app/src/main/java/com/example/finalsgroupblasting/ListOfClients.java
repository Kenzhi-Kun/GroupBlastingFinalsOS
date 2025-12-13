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
        ArrayList<Appointment> appointments = new ArrayList<>(); // This holds the actual appointment objects for sorting and selection
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, displayList);
        appointmentList.setAdapter(adapter);
        appointmentList.setChoiceMode(ListView.CHOICE_MODE_SINGLE); // This will visually show the user what item is selected.

        // NEW: Add a variable to store the key of the selected appointment.
        final String[] selectedAppointmentKey = {null};

        DatabaseReference database = FirebaseDatabase.getInstance("https://finalsgroupblasting-6eab4d18-default-rtdb.firebaseio.com/").getReference("appointment").child("Accepted-Appointment");

        // This listener fetches data, sorts it, and updates the UI
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

                // Sorting logic remains the same
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

        // NEW: Set a listener to know which item the user clicks on in the list
        appointmentList.setOnItemClickListener((parent, view, position, id) -> {
            // 'position' is the index of the clicked item (0, 1, 2, ...)
            if (position >= 0 && position < appointments.size()) {
                // Get the corresponding appointment from our sorted list
                Appointment selectedAppointment = appointments.get(position);
                // Save its unique Firebase key
                selectedAppointmentKey[0] = selectedAppointment.getKey();
                Toast.makeText(ListOfClients.this, "Selected: " + selectedAppointment.getUser(), Toast.LENGTH_SHORT).show();
            }
        });

        // UPDATED: The "Done" button now acts on the selected item
        removeTopClientButton.setOnClickListener(v -> {
            // Check if an item has been selected
            if (selectedAppointmentKey[0] == null) {
                Toast.makeText(ListOfClients.this, "Please select an appointment from the list first.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Use the saved key to delete the correct item from Firebase
            database.child(selectedAppointmentKey[0]).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(ListOfClients.this, "Appointment marked as done.", Toast.LENGTH_SHORT).show();
                        // Reset the selection after removing the item
                        selectedAppointmentKey[0] = null;
                        appointmentList.clearChoices(); // Clears the visual selection indicator
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ListOfClients.this, "Failed to remove appointment.", Toast.LENGTH_SHORT).show();
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
