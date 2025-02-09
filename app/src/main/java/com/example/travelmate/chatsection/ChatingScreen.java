package com.example.travelmate.chatsection;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.travelmate.R;
import com.example.travelmate.data.ChatList;
import com.example.travelmate.data.MessageObject;
import com.example.travelmate.data.userMessage;
import com.example.travelmate.databinding.ActivityChatingScreenBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChatingScreen extends AppCompatActivity {

    private ActivityChatingScreenBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore database;
    private String currentUserId;
    private String clickedUserId;

    private List<MessageObject> messageList;
    private ChatAdapter chatAdapter;
    private ListenerRegistration messageListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatingScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        currentUserId = auth.getCurrentUser().getUid();

        // Retrieve clicked user ID from intent
        clickedUserId = getIntent().getStringExtra("clickedUserId");
//        clickedUserName=getIntent().getStringExtra("clickedUserName");
//        clickedUserPicture=getIntent().getStringExtra("clickedUserPicture");

        setHeader();

//        binding.userName.setText(clickedUserName);
//        Picasso.get()
//                .load(clickedUserPicture)
//                .placeholder(R.drawable.ic_launcher_background) // Default placeholder
//                .error(R.drawable.icon_name_profile__style_false__size_64px) // Error image
//                .into(binding.profileImage);


        if (clickedUserId == null) {
            Toast.makeText(this, "Error: User ID not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize RecyclerView
        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(ChatingScreen.this,messageList, currentUserId);
        // Set up the LinearLayoutManager for bottom-to-top scrolling
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);  // Start from the bottom
        layoutManager.setReverseLayout(false);  // Don't reverse the order of items

        binding.recyclerViewChat.setLayoutManager(layoutManager);
        binding.recyclerViewChat.setAdapter(chatAdapter);

        // Send message on button click
        binding.buttonSend.setOnClickListener(v -> {
            String messageText = binding.editTextMessage.getText().toString().trim();

            if (!messageText.isEmpty()) {
                sendMessage(messageText);
                binding.editTextMessage.setText(""); // Clear input field
            } else {
                Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        // Load messages
        loadMessages();
    }

    private void setHeader() {
        database.collection("user").document(clickedUserId)
                // Specify only the required fields
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            String username = document.getString("userName"); // Retrieve username
                            String profilePic = document.getString("userProfile"); // Retrieve profile picture URL

                            // Populate the UI fields
                            binding.userName.setText(username);

                            Picasso.get()
                .load(profilePic)
                .placeholder(R.drawable.ic_launcher_background) // Default placeholder
                .error(R.drawable.icon_name_profile__style_false__size_64px) // Error image
                .into(binding.profileImage);


                        } }
                });

    }

    // Send a message to Firestore
    private void sendMessage(String messageText) {
        long timestamp = System.currentTimeMillis();

        // Create a new MessageObject
        MessageObject newMessage = new MessageObject(currentUserId, messageText, timestamp);

        // Firestore reference for userMessage
        DocumentReference userMessageRef = database.collection("messages")
                .document(currentUserId)
                .collection("user_chats")
                .document(clickedUserId);

        // Get the existing userMessage object from Firestore
        userMessageRef.get().addOnSuccessListener(documentSnapshot -> {
            userMessage currentMessages = documentSnapshot.toObject(userMessage.class);

            if (currentMessages == null) {
                // If no messages exist, create a new userMessage object
                currentMessages = new userMessage();
                currentMessages.setMessages(new ArrayList<>());
            }

            // Add the new message to the existing message list
            currentMessages.getMessages().add(newMessage);

            // Update the userMessage document with the new message
            userMessage finalCurrentMessages = currentMessages;
            userMessageRef.set(currentMessages)
                    .addOnSuccessListener(aVoid -> {
                        // Also update the other user's chat document
                        DocumentReference otherUserMessageRef = database.collection("messages")
                                .document(clickedUserId)
                                .collection("user_chats")
                                .document(currentUserId);


                        // Update the other user's message list as well
                        otherUserMessageRef.set(finalCurrentMessages)
                                .addOnSuccessListener(aVoid1 -> {
                                    Toast.makeText(ChatingScreen.this, "Message sent!", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(ChatingScreen.this, "Failed to send message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ChatingScreen.this, "Failed to send message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }).addOnFailureListener(e -> {
            Toast.makeText(ChatingScreen.this, "Failed to load messages: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });


//        add the current user to the other user list

        DocumentReference chatListRef = database.collection("chatlist").document(clickedUserId);

        chatListRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Retrieve existing chatList
                List<String> chatList = documentSnapshot.toObject(ChatList.class).getChatList();

                if (chatList == null) {
                    chatList = new ArrayList<>();
                }

                // Add clickedUserId if not already present
                if (!chatList.contains(currentUserId)) {
                    chatList.add(0,currentUserId);

                    // Update Firestore
                    chatListRef.update("chatList", chatList)
                            .addOnSuccessListener(aVoid -> Toast.makeText(this, "User added to chat list!", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(this, "Failed to update chat list: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                } else {
                    Toast.makeText(this, "User already in chat list.", Toast.LENGTH_SHORT).show();
                }
            } else {
                // If no chatList exists, create one
                List<String> chatList = new ArrayList<>();
                chatList.add(currentUserId);

                // Create new document
                chatListRef.set(new ChatList(currentUserId, chatList))
                        .addOnSuccessListener(aVoid -> Toast.makeText(this, "Chat list created and user added!", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(this, "Failed to create chat list: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).addOnFailureListener(e -> Toast.makeText(this, "Failed to retrieve chat list: " + e.getMessage(), Toast.LENGTH_SHORT).show());




    }


    // Load messages from Firestore
    private void loadMessages() {
        // Set up a Firestore listener to fetch messages in real-time
        messageListener = database.collection("messages")
                .document(currentUserId)
                .collection("user_chats")
                .document(clickedUserId)
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        Toast.makeText(this, "Failed to load messages: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Get the 'userMessage' object which contains an ArrayList of messages
                        userMessage currentMessages = documentSnapshot.toObject(userMessage.class);

                        if (currentMessages != null && currentMessages.getMessages() != null) {
                            messageList.clear();  // Clear the existing message list
                            messageList.addAll(currentMessages.getMessages());  // Add all the messages to the list

                            // Update the adapter with the new list of messages
                            chatAdapter.updateMessages(messageList);
                            binding.recyclerViewChat.smoothScrollToPosition(messageList.size() - 1);  // Scroll to the latest message
                        }
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the Firestore listener when the activity is destroyed
        if (messageListener != null) {
            messageListener.remove();
        }
    }
}
