package com.example.finalsgroupblasting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LawyerFiles extends AppCompatActivity {

    private ImageView appointmentButton;
    private ImageView filesButton;
    private ImageView listOfClients;
    private TextView logoutTextBtn;
    private TextView userName;

    private Button affidavitButton;
    private Button contractButton;
    private Button willButton;

    private ListView filesListView;
    private ArrayList<String> displayList;
    private ArrayList<String> uriList;
    private ArrayAdapter<String> adapter;

    private String currentCategory = "affidavit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        getSupportActionBar().hide();
        setContentView(R.layout.activity_lawyer_files);

        appointmentButton = findViewById(R.id.appointment_home_client5);
        filesButton = findViewById(R.id.file_home_client5);
        listOfClients = findViewById(R.id.files_home_client5);
        logoutTextBtn = findViewById(R.id.logoutTextBtn5);
        userName = findViewById(R.id.user_name10);

        affidavitButton = findViewById(R.id.affidavitsClick);
        contractButton = findViewById(R.id.contractsClick);
        willButton = findViewById(R.id.willsClick);

        filesListView = findViewById(R.id.listOfFiles);


        // ListView setup
        displayList = new ArrayList<>();
        uriList = new ArrayList<>();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                displayList
        );
        filesListView.setAdapter(adapter);

        filesListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(uriList.get(position)));
            startActivity(intent);
        });

        // Category buttons
        affidavitButton.setOnClickListener(v -> {
            currentCategory = "affidavit";
            fetchFiles();
        });

        contractButton.setOnClickListener(v -> {
            currentCategory = "contracts";
            fetchFiles();
        });

        willButton.setOnClickListener(v -> {
            currentCategory = "will";
            fetchFiles();
        });

        logoutTextBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        appointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(LawyerFiles.this, LawyerAppointment.class);
                startActivity(intent2);
            }
        });

        listOfClients.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LawyerFiles.this, ListOfClients.class);
                startActivity(intent);
            }
        });

        displayLawyerName();
        fetchFiles();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
    }

    private void fetchFiles() {
        DatabaseReference ref = FirebaseDatabase.getInstance(
                        "https://finalsgroupblasting-6eab4d18-default-rtdb.firebaseio.com/")
                .getReference("appointment")
                .child("Client-Files");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                displayList.clear();
                uriList.clear();

                for (DataSnapshot client : snapshot.getChildren()) {
                    if (!client.hasChild(currentCategory)) continue;

                    for (DataSnapshot file : client.child(currentCategory).getChildren()) {
                        String name = file.child("fileName").getValue(String.class);
                        String email = file.child("uploadedBy").getValue(String.class);
                        String uri = file.child("fileUri").getValue(String.class);

                        if (name != null && uri != null) {
                            displayList.add(name + "\n" + email);
                            uriList.add(uri);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void displayLawyerName() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            userName.setText(name != null ? name : "Lawyer");
        }
    }
}
