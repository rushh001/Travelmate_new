package com.example.travelmate.data;

public class MessageItem {
    private String senderId;
    private String messageText;
    private boolean isSent; // True if the message is sent by the current user, else received

    public MessageItem() { }

    public MessageItem(String senderId, String messageText) {
        this.senderId = senderId;
        this.messageText = messageText;
    }

    public MessageItem(String senderId, String messageText, boolean isSent) {
        this.senderId = senderId;
        this.messageText = messageText;
        this.isSent = isSent;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }
}

