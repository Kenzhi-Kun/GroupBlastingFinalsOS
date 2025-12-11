package com.example.finalsgroupblasting;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ClientUpload extends AppCompatActivity {

    private ImageView appointmentButton;
    private ImageView uploadButton;
    private ImageView filesButton;
    private TextView logoutButton;
    private ImageView addPhotoButton;
    private TextView userName;

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
        addPhotoButton = findViewById((R.id.addImage));
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
    }

    private void handleFile(Uri fileUri) {
        String fileName = getFileName(fileUri);

        if (fileUri.toString().endsWith(".jpg") ||fileUri.toString().endsWith(".png")) {
            addPhotoButton.setImageURI(fileUri);
        } else {
            Toast.makeText(this, "Selected File: ", Toast.LENGTH_SHORT).show();
        }
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