package com.example.exampletwo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class profileActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private TextView usernameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        profileImageView = findViewById(R.id.profileImageView);
        usernameTextView = findViewById(R.id.usernameTextView);

        // Retrieve image path and username from intent extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String imagePath = extras.getString("imagePath");
            String username = extras.getString("username");

            // Load image from the path and display it in the ImageView
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            profileImageView.setImageBitmap(bitmap);

            // Set the retrieved username to the TextView
            usernameTextView.setText(username);
        }
    }
}
