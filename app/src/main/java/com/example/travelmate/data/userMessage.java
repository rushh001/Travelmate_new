package com.example.travelmate.data;

import java.util.ArrayList;
import java.util.List;

public class userMessage {
    private List<MessageObject> messages;

    // Constructor
    public userMessage() {
        this.messages = new ArrayList<>();
    }

    // Add a message
    public void addMessage(String senderId, String messageText) {
        long timestamp = System.currentTimeMillis(); // Capture the current timestamp
        MessageObject newMessage = new MessageObject(senderId, messageText, timestamp);
        this.messages.add(newMessage);
    }

    // Get all messages
    public List<MessageObject> getMessages() {
        return this.messages;
    }

    // Set all messages (overwrites existing messages)
    public void setMessages(List<MessageObject> messages) {
        this.messages = messages;
    }
}
