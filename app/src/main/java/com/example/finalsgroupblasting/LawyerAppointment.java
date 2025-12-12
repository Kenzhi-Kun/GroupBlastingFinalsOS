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
import java.util.HashMap;

public class LawyerAppointment extends AppCompatActivity {

    private TextView userNameTextView;
    private ImageView uploadButton;
    private ImageView listOfClients;
    private TextView logoutTextBtn;
    private Button appointmentButton;
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

        uploadButton = findViewById(R.id.upload_home_client9);
        listOfClients = findViewById(R.id.files_home_client9);
        logoutTextBtn = findViewById(R.id.logoutTextBtn9);
        appointmentButton = findViewById(R.id.accept_button);
        userNameTextView = findViewById(R.id.user_name8);
        appointmentList = findViewById(R.id.appointment_list);
        displayUsername();


        uploadButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(LawyerAppointment.this, "This feature is not currently available.", Toast.LENGTH_SHORT).show();
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
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
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

            }
        });

        appointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!list.isEmpty()) {
                    String key = database.push().getKey();
                    String topList = list.get(0).toString();
                    HashMap<String, Object> hash = new HashMap<>();
                    hash.put("Appointment: ", topList);

                    if (key != null) {
                        database2.child(key).setValue(hash);
                    }

                    list.remove(0);
                    adapter.notifyDataSetChanged();
                }
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