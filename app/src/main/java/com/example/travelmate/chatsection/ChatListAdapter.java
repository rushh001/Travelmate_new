package com.example.travelmate.chatsection;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelmate.R;
import com.example.travelmate.data.ChatList;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {

    private Context context;
    private List<String> userIds; // Flattened list of user IDs
    private FirebaseFirestore database; // Firestore instance for querying usernames and profile pictures
    private Map<String, String> userNameCache; // Cache for user IDs to usernames
    private Map<String, String> userProfileCache; // Cache for user IDs to profile picture URLs

    public ChatListAdapter(Context context, List<ChatList> chatList, FirebaseFirestore database) {
        this.context = context;
        this.database = database;
        this.userNameCache = new HashMap<>();
        this.userProfileCache = new HashMap<>();
        this.userIds = new ArrayList<>();

        // Flatten the list of user IDs from ChatList
        for (ChatList chat : chatList) {
            List<String> ids = chat.getChatList();
            if (ids != null) {
                this.userIds.addAll(ids); // Add all IDs from each ChatList to the userIds list
            }
        }
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chatlist_adapter, parent, false);
        return new ChatListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        String userId = userIds.get(position);

        // Check if the username is already cached
        if (userNameCache.containsKey(userId)) {
            holder.chatListText.setText(userNameCache.get(userId)); // Display cached username
        } else {
            database.collection("user").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String userName = documentSnapshot.getString("userName");
                            userNameCache.put(userId, userName);
                            holder.chatListText.setText(userName);
                        } else {
                            holder.chatListText.setText("Unknown User");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Failed to fetch username", Toast.LENGTH_SHORT).show();
                        holder.chatListText.setText("Error");
                    });
        }

        // Set profile picture using Picasso
        database.collection("user").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String profilePictureUrl = documentSnapshot.getString("userProfile");
                        Picasso.get()
                                .load(profilePictureUrl)
                                .placeholder(R.drawable.ic_launcher_background) // Default placeholder
                                .error(R.drawable.icon_name_profile__style_false__size_64px) // Error image
                                .into(holder.profileImage);
                    }
                });

       holder.chatCard.setOnClickListener(v -> {
           Intent intent = new Intent(context,ChatingScreen.class);
           intent.putExtra("clickedUserId", userId);  // Pass additional data if needed
           intent.putExtra("clickedUserName", userNameCache.toString());// Pass additional data if needed
           intent.putExtra("clickedUserPicture", userProfileCache.toString());// Pass additional data if needed
           context.startActivity(intent);
       });
    }


    @Override
    public int getItemCount() {
        return userIds.size(); // Return the size of the flattened list
    }

    public static class ChatListViewHolder extends RecyclerView.ViewHolder {

        TextView chatListText;
        ImageView profileImage;
        CardView chatCard;

        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);
            chatListText = itemView.findViewById(R.id.ad_full_name); // Make sure R.id.ad_full_name exists in your layout
            profileImage = itemView.findViewById(R.id.ad_profile_image); // Make sure R.id.profile_image exists in your layout
            chatCard= itemView.findViewById(R.id.chat_card);
        }
    }
}
