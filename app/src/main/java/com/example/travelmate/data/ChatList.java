package com.example.travelmate.data;

import java.util.List;

public class ChatList {

        private String userId;
        private List<String> chatList;

        public ChatList() {}



    public ChatList(String userId, List<String> chatList) {
            this.userId = userId;

            this.chatList=chatList;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

    public List<String> getChatList() {
        return chatList;
    }

    public void setChatList(List<String> chatList) {
        this.chatList = chatList;
    }

}
