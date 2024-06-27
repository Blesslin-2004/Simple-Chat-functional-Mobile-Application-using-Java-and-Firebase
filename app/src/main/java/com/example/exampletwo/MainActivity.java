package com.example.exampletwo;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    private static final int IMAGE_CAPTURE_CODE = 1001;
    private static final int IMAGE_PICK_CODE = 1002;

    private ImageView imageView , plus;
    private EditText editText;
    private Button  completeButton;
    private String imagePath,username;


    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        imageView = findViewById(R.id.profilepic);
        editText = findViewById(R.id.nameET);
        completeButton = findViewById(R.id.combtn);
        plus = findViewById(R.id.plus);

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        // Capture image from camera


        // Select image from gallery
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, IMAGE_PICK_CODE);
                plus.setVisibility(View.GONE);
            }
        });

        // Save image path and username to database
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 username = editText.getText().toString().trim();
                if (!username.isEmpty() && imagePath != null) {
                    long result = dbHelper.insertData(username, imagePath);
                    if (result != -1) {
                        Toast.makeText(MainActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                        // Start ProfileActivity and pass image path and username as extras
                        Intent intent = new Intent(MainActivity.this, profileActivity.class);
                        intent.putExtra("imagePath", imagePath);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    }
                    if (isAllDataSaved()) {
                        // All data saved, navigate to another activity or perform another action
                        // For example, start another activity
                        Intent intent = new Intent(MainActivity.this, profileActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(MainActivity.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please enter username and select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private boolean isAllDataSaved() {
        return imagePath != null && !username.isEmpty();
    }



    // Handle image capture or selection result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_CAPTURE_CODE) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(bitmap);
                imagePath = dbHelper.saveToInternalStorage(bitmap);
            } else if (requestCode == IMAGE_PICK_CODE) {
                Uri imageUri = data.getData();
                imageView.setImageURI(imageUri);
                imagePath = dbHelper.getRealPathFromURI(this, imageUri);
            }
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close(); // Close the database connection when activity is destroyed
        super.onDestroy();
    }

    // Retrieve and display saved data from the database
    // Retrieve and display saved data from the database
    private void displaySavedData() {
        Cursor cursor = dbHelper.getAllData();
        if (cursor.moveToFirst()) {
            do {
                int usernameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USERNAME);
                int imagePathIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE_PATH);

                // Check if the column exists in the cursor
                if (usernameIndex >= 0 && imagePathIndex >= 0) {
                    String username = cursor.getString(usernameIndex);
                    String imagePath = cursor.getString(imagePathIndex);

                    // Load image into ImageView
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    imageView.setImageBitmap(bitmap);

                    // Set username in EditText
                    editText.setText(username);
                } else {
                    // Handle the case where column index is -1
                    Log.e("DisplaySavedData", "Column not found in cursor");
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

}


