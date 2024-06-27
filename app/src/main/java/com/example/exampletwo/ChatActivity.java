package com.example.exampletwo;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exampletwo.MessageAdapter;
import com.example.exampletwo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText messageEditText;
    private Button sendButton;

    private MessageAdapter messageAdapter;
    private List<com.example.exampletwo.Message> messageList;

    private DatabaseReference messagesDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.recyclerView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messageList, FirebaseAuth.getInstance().getCurrentUser().getUid());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        messagesDatabaseReference = FirebaseDatabase.getInstance().getReference().child("messages");

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        // Listen for new messages
// Listen for new messages
        messagesDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                com.example.exampletwo.Message message = snapshot.getValue(Message.class);
                messageList.add(message);
                recyclerView.scrollToPosition(messageList.size() - 1);
            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void sendMessage() {
        String messageText = messageEditText.getText().toString().trim();
        if (!messageText.isEmpty()) {
            String senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String receiverId = "receiver_id_here"; // Replace with the receiver's ID
           com.example.exampletwo.Message message = new com.example.exampletwo.Message("sender", messageText);
            messagesDatabaseReference.push().setValue(message);
            messageEditText.setText("");
        }
    }
}
