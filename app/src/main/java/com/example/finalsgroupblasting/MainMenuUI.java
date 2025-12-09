package com.example.finalsgroupblasting;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainMenuUI extends AppCompatActivity {
    private TextView userLawTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_ui);

        userLawTextView = findViewById(R.id.user_law);
        displayUsername();

    }

    public void displayUsername() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String userName = user.getDisplayName();
            if (userName != null && !userName.isEmpty()) {
                userLawTextView.setText(userName);
            } else {
                userLawTextView.setText("Anonymous");
            }
        } else {
            userLawTextView.setText("Anonymous");
        }
    }

}
