package com.example.exampletwo;

public class Message {
    private String senderId;
    private String receiverId;
    private String text;

    // Default constructor required for Firebase
    public Message() {
        // Empty constructor
    }

    public Message(String senderId, String text) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.text = text;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getText() {
        return text;
    }
}
