package com.example.finalsgroupblasting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class ClientAppointment extends AppCompatActivity {
    private ImageView uploadButton;
    private ImageView filesButton;
    private TextView logoutButton;
    private TextView userName;
    private CalendarView calendarView;
    private Button addtoQueue;
    private EditText timeInput;
    private TextView date;
    private TextView time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_client_appointment);

        addtoQueue = findViewById(R.id.appoint_button);
        uploadButton = findViewById(R.id.upload_home_client4);
        filesButton = findViewById(R.id.files_home_client4);
        logoutButton = findViewById(R.id.logoutTextBtn4);
        userName = findViewById(R.id.user_name5);
        calendarView = findViewById(R.id.calendarView);
        timeInput = findViewById(R.id.time_input);

        // Text ID's
        date = findViewById(R.id.date1);
        time = findViewById(R.id.time1);


        displayUsername();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientAppointment.this, ClientUpload.class);
                startActivity(intent);
                finish();
            }
        });

        filesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ClientAppointment.this, ClientFiles.class);
                startActivity(intent2);
                finish();
            }

        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(ClientAppointment.this, MainActivity.class);
                startActivity(intent3);
                finish();
            }
        });

        timeInput.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                time.setText(s);
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {

            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String selectedDate = (month + 1) + "/" + dayOfMonth + "/" + year;
                date.setText(selectedDate);
            }
        });

        addtoQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    generateAppointment();
            }
        });
    }

    public void generateAppointment() {
        DatabaseReference database = FirebaseDatabase.getInstance("https://finalsgroupblasting-6eab4d18-default-rtdb.firebaseio.com/").getReference("appointment");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String time2 = time.getText().toString();
        String date2 = date.getText().toString();

        String key = database.push().getKey();

        if (time2.isEmpty() || date2.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, Object> hash = new HashMap<>();
        hash.put("user", user.getEmail());
        hash.put("date", date2);
        hash.put("time", time2);



        database.child(key).setValue(hash)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                               Toast.makeText(this, "Appointment added!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Appointment Failed!", Toast.LENGTH_SHORT).show();
                    }
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