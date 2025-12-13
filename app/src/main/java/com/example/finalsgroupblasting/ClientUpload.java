package com.example.finalsgroupblasting;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ClientUpload extends AppCompatActivity {

    private ImageView appointmentButton;
    private ImageView uploadButton;
    private ImageView filesButton;
    private TextView logoutButton;
    private ImageView addPhotoButton;
    private TextView userName;
    private CheckBox affidavitButton;
    private CheckBox contractsButton;
    private CheckBox willsButton;
    private Button saveButton;
    private EditText encodeFileNameInput;
    private String selectedCategory = null;
    private Uri selectedFile = null;



    ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            Uri imageUri = result.getData().getData();

            if (result.getData().getClipData() != null) {
                int count = result.getData().getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri image = result.getData().getClipData().getItemAt(i).getUri();
                    handleFile(image);
                }
            } else {
                Uri image = result.getData().getData();
                handleFile(image);
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_client_upload);

        appointmentButton = findViewById(R.id.appointment_home_client3);
        uploadButton = findViewById(R.id.upload_home_client3);
        filesButton = findViewById(R.id.files_home_client3);
        logoutButton = findViewById(R.id.logoutTextBtn3);
        userName = findViewById(R.id.user_name7);
        encodeFileNameInput = findViewById(R.id.encodeUwU);
        addPhotoButton = findViewById((R.id.addImage));
        affidavitButton = findViewById(R.id.affidavitUwU);
        contractsButton = findViewById(R.id.contractsUwU);
        willsButton = findViewById(R.id.willsUwU);
        saveButton = findViewById(R.id.saveButtonUwU);

        displayUsername();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        filesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ClientUpload.this, ClientFiles.class);
                startActivity(intent2);
                finish();
            }

        });

        appointmentButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ClientUpload.this, ClientAppointment.class);
                startActivity(intent);
            }
        });


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(ClientUpload.this, MainActivity.class);
                startActivity(intent3);
                finish();
            }
        });

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent5 = new Intent(Intent.ACTION_GET_CONTENT);
                intent5.setType("*/*");
                intent5.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{
                        "image/*",
                        "application/pdf",
                        "application/msword",
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                });
                intent5.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                filePickerLauncher.launch(intent5);
            }
        });

        affidavitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                affidavitButton.setChecked(true);
                contractsButton.setChecked(false);
                willsButton.setChecked(false);
                selectedCategory = "affidavit";
            }
        });

        contractsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                affidavitButton.setChecked(false);
                contractsButton.setChecked(true);
                willsButton.setChecked(false);
                selectedCategory = "contracts";
            }
        });

        willsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                affidavitButton.setChecked(false);
                contractsButton.setChecked(false);
                willsButton.setChecked(true);
                selectedCategory = "will";
            }
        });

        saveButton.setOnClickListener(v -> saveSelectedFile());
    }

    private void handleFile(Uri fileUri) {
        selectedFile = fileUri;

        String fileName = getFileName(fileUri);
        if (fileName == null) {
            Toast.makeText(this, "Invalid file", Toast.LENGTH_SHORT).show();
            return;
        }

        if (fileUri.toString().endsWith(".jpg") ||
                fileUri.toString().endsWith(".jpeg") ||
                fileUri.toString().endsWith(".png")) {

            addPhotoButton.setImageURI(fileUri);
        }

        Toast.makeText(this, "Selected: " + fileName, Toast.LENGTH_SHORT).show();
    }
    public String getFileName(Uri nameUri) {
        String result = null;

        if (nameUri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(nameUri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index >= 0) {
                        result = cursor.getString(index);
                    }
                }
            }
        }
        if (result == null) {
            result = nameUri.getLastPathSegment();
        }
        return result;
    }

    private void saveSelectedFile() {

        String encodedName = encodeFileNameInput.getText().toString();

        if (selectedFile == null) {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedCategory == null) {
            Toast.makeText(this, "Select a category first", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User is not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String fileName = getFileName(selectedFile);
        String location = "Client-Files";
        if (fileName == null) fileName = "file_" + System.currentTimeMillis();

        DatabaseReference ref = FirebaseDatabase
                .getInstance("https://finalsgroupblasting-6eab4d18-default-rtdb.firebaseio.com/")
                .getReference("appointment")
                .child(location)
                .child(user.getUid())
                .child(selectedCategory);

        String key = ref.push().getKey();
        if (key == null) return;

        Map<String, Object> fileData = new HashMap<>();
        fileData.put("user", user.getUid());
        fileData.put("uploadedBy", user.getEmail());
        fileData.put("fileName", fileName);
        fileData.put("encodedName", encodedName);
        fileData.put("uploadedAt", System.currentTimeMillis());
        fileData.put("fileUri", selectedFile.toString());

        ref.child(key)
                .setValue(fileData)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(this, "Saved to " + selectedCategory, Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Save failed: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
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