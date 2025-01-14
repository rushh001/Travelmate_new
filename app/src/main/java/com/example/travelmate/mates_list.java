package com.example.travelmate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelmate.data.ChatList;
import com.example.travelmate.data.user_details;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class mates_list extends RecyclerView.Adapter<mates_list.ViewHolder> {

    ArrayList<user_details> matelist;
    private FirebaseAuth auth;
    private FirebaseFirestore database;

    public mates_list(ArrayList<user_details> matelist) {
        this.matelist = matelist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_view_adapter, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        user_details user = matelist.get(position);

        // Set text values
        holder.fullname.setText(user.userName != null ? user.userName : "Unknown");
        holder.year.setText(user.userYear != null ? user.userYear : "N/A");
        holder.from.setText(user.userSource != null ? user.userSource : "Unknown");
        holder.to.setText(user.userDestination != null ? user.userDestination : "Unknown");
        holder.age.setText(user.userAge != null ? user.userAge : "N/A");
        holder.gender.setText(user.userGender != null ? user.userGender : "N/A");
        holder.origin.setText(user.userOrigin != null ? user.userOrigin : "Unknown");
        holder.time.setText(user.userTime != null ? user.userTime : "00:00");
        holder.date.setText(user.userDate != null ? user.userDate : "dd/mm/yyyy");

        // Load profile image using Picasso
        if (user.userProfile != null && !user.userProfile.isEmpty()) {
            Picasso.get()
                    .load(user.userProfile)
                    .placeholder(R.drawable.ic_launcher_background) // Placeholder image
                    .error(R.drawable.ic_launcher_background) // Error image
                    .into(holder.profile);
        } else {
            holder.profile.setImageResource(R.drawable.ic_launcher_background);
        }

        // OnClickListener for connectButton
        holder.connectButton.setOnClickListener(view -> {
            String clickedUserId = user.userId; // ID of the clicked user
            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Current user's ID

            // Firestore reference
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            DocumentReference chatListRef = firestore.collection("chatlist").document(currentUserId);

            chatListRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    // Retrieve existing chatList
                    List<String> chatList = documentSnapshot.toObject(ChatList.class).getChatList();

                    if (chatList == null) {
                        chatList = new ArrayList<>();
                    }

                    // Add clickedUserId if not already present
                    if (!chatList.contains(clickedUserId)) {
                        chatList.add(0,clickedUserId);

                        // Update Firestore
                        chatListRef.update("chatList", chatList)
                                .addOnSuccessListener(aVoid -> Toast.makeText(view.getContext(), "User added to chat list!", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(view.getContext(), "Failed to update chat list: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(view.getContext(), "User already in chat list.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // If no chatList exists, create one
                    List<String> chatList = new ArrayList<>();
                    chatList.add(clickedUserId);

                    // Create new document
                    chatListRef.set(new ChatList(currentUserId, chatList))
                            .addOnSuccessListener(aVoid -> Toast.makeText(view.getContext(), "Chat list created and user added!", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(view.getContext(), "Failed to create chat list: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }).addOnFailureListener(e -> Toast.makeText(view.getContext(), "Failed to retrieve chat list: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }


    @Override
    public int getItemCount() {
        return matelist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView fullname, year, from, to, age, origin, gender, time, date;
        ImageView profile;
        MaterialButton connectButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views
            fullname = itemView.findViewById(R.id.ad_full_name);
            year = itemView.findViewById(R.id.ad_year);
            from = itemView.findViewById(R.id.ad_source);
            to = itemView.findViewById(R.id.ad_destination);
            age = itemView.findViewById(R.id.ad_age);
            gender = itemView.findViewById(R.id.ad_gender);
            origin = itemView.findViewById(R.id.ad_placeOfOrigin);
            profile = itemView.findViewById(R.id.ad_profile_image);
            time = itemView.findViewById(R.id.ad_time);
            date = itemView.findViewById(R.id.ad_date);
            connectButton = itemView.findViewById(R.id.connect_button);

        }
    }
}
