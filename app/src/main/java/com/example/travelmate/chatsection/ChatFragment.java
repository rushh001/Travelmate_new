package com.example.travelmate.chatsection;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.travelmate.R;
import com.example.travelmate.data.ChatList;
import com.example.travelmate.databinding.FragmentChatBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    FragmentChatBinding binding;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth auth;
    ArrayList<ChatList> chatList;
    ChatListAdapter adapter;
    FirebaseFirestore db;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        Toast.makeText(getActivity(), "Something went wrong0", Toast.LENGTH_SHORT).show();
        requireActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.purple));
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(0);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        auth = FirebaseAuth.getInstance();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatlistrv.setLayoutManager(layoutManager);

// Initialize userDetails list to avoid NullPointerException
//        chatList = new ArrayList<>();

// Initialize adapter with the non-null list
//        adapter = new ChatListAdapter(getActivity(),chatList);
//        binding.chatlistrv.setAdapter(adapter);

// Initialize Firestore database instance
// Fetch data from Firestore
        // Firestore query to fetch data
        // Firestore query to fetch data
//        db.collection("chatlist").document(currentUserId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                if (queryDocumentSnapshots != null) {
//                    List<DocumentSnapshot> documentList = queryDocumentSnapshots.toObjects(chatList);
//                    List<ChatList> chatLists = new ArrayList<>();
//
//                    for (DocumentSnapshot doc : documentList) {
//                        ChatList chat = doc.toObject(ChatList.class);
//                        chatLists.add(chat); // Add the ChatList object to the list
//                    }
//
//                    // Set the adapter with the fetched data
//                    ChatListAdapter adapter = new ChatListAdapter(getContext(), chatLists,db);
//                    binding.chatlistrv.setAdapter(adapter);
//                }
//            }
//        });


        db = FirebaseFirestore.getInstance();
        String currentUserId = auth.getInstance().getCurrentUser().getUid();

        db.collection("chatlist")
                .whereEqualTo("userId", currentUserId) // Filter where the "userId" matches the current user
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            List<ChatList> chatLists = new ArrayList<>();

                            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                ChatList chat = doc.toObject(ChatList.class);
                                chatLists.add(chat); // Add the ChatList object to the list
                            }

                            // Set the adapter with the fetched data
                            ChatListAdapter adapter = new ChatListAdapter(getContext(), chatLists, db);
                            binding.chatlistrv.setAdapter(adapter);
                        } else {
                            // Handle the case where no chatlist is found for the current user
                            Log.d("ChatList", "No chat list found for the current user.");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("ChatList", "Error fetching chat list", e);
                    }
                });




    }
}