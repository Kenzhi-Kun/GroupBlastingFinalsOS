//package com.example.finalsgroupblasting;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.CalendarView;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//import java.util.Locale;
//
//public class AppointmentClientSide extends AppCompatActivity {
//
//    private CalendarView calendarView;
//    private RecyclerView appointmentsRecyclerView;
//    private FloatingActionButton addAppointmentButton;
//    private AppointmentAdapter appointmentAdapter;
//    private List<Appointment> appointmentList;
//    private DatabaseReference databaseReference;
//    private String selectedDate;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.appointment_client_ui);
//
//        calendarView = findViewById(R.id.calendarView);
//        appointmentsRecyclerView = findViewById(R.id.appointmentsRecyclerView);
//        addAppointmentButton = findViewById(R.id.addAppointmentButton);
//
//        //Kenzhi can you put the url of the firebase
//        databaseReference = FirebaseDatabase.getInstance("https://finalsgroupblasting-6eab4d18-default-rtdb.firebaseio.com/").getReference("appointments");
//
//        setupRecyclerView();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//        selectedDate = sdf.format(Calendar.getInstance().getTime());
//        loadAppointments(selectedDate);
//
//        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
//            selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
//            loadAppointments(selectedDate);
//        });
//
//        addAppointmentButton.setOnClickListener(v -> showAddAppointmentDialog());
//    }
//
//    private void setupRecyclerView() {
//        appointmentList = new ArrayList<>();
//        appointmentAdapter = new AppointmentAdapter(appointmentList);
//        appointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        appointmentsRecyclerView.setAdapter(appointmentAdapter);
//    }
//
//    private void loadAppointments(String date) {
//        databaseReference.child(date).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                appointmentList.clear();
//                for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
//                    Appointment appointment = appointmentSnapshot.getValue(Appointment.class);
//                    appointmentList.add(appointment);
//                }
//                appointmentAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(AppointmentClientSide.this, "Failed to load appointments.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void showAddAppointmentDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.dialog_add_appointment, null);
//        builder.setView(dialogView);
//
//        final EditText appointmentTitleInput = dialogView.findViewById(R.id.appointmentTitleInput);
//        final EditText appointmentTimeInput = dialogView.findViewById(R.id.appointmentTimeInput);
//        Button saveButton = dialogView.findViewById(R.id.saveAppointmentButton);
//
//        AlertDialog dialog = builder.create();
//
//        saveButton.setOnClickListener(v -> {
//            String title = appointmentTitleInput.getText().toString().trim();
//            String time = appointmentTimeInput.getText().toString().trim();
//
//            if (title.isEmpty() || time.isEmpty()) {
//                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            String id = databaseReference.child(selectedDate).push().getKey();
//            Appointment newAppointment = new Appointment(id, title, time, selectedDate);
//
//            if (id != null) {
//                databaseReference.child(selectedDate).child(id).setValue(newAppointment)
//                        .addOnSuccessListener(aVoid -> {
//                            Toast.makeText(this, "Appointment saved!", Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();
//                        })
//                        .addOnFailureListener(e -> Toast.makeText(this, "Failed to save appointment.", Toast.LENGTH_SHORT).show());
//            }
//        });
//
//        dialog.show();
//    }
//}
