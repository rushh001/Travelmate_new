package com.example.travelmate.usersection;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.travelmate.R;
import com.example.travelmate.data.user_details;
import com.example.travelmate.databinding.ActivityProctorSectionBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;

public class ProctorSection extends AppCompatActivity {

    private ActivityProctorSectionBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore database;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProctorSectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // Initialize Firestore and DocumentReference
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        userId = auth.getCurrentUser().getUid();

        // Fetch and display existing data
        fetchProctorData();

        // Update data on Save button click
        binding.saveButton.setOnClickListener(view -> saveProctorData());
    }

    private void fetchProctorData() {
        database.collection("user").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            user_details userDetails = document.toObject(user_details.class);
                            populateFields(userDetails);
                        } else {
                            Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void populateFields(user_details userDetails) {
        if (userDetails != null) {
            binding.proctorName.setText(userDetails.getProctorName());
            binding.proctorPhoneNumber.setText(userDetails.getProctorPhoneNumber());
            binding.proctorEmail.setText(userDetails.getProctorEmail());
        }
    }

    private void saveProctorData() {
        // Collect data from UI fields
        String proctorName = binding.proctorName.getText().toString();
        String proctorPhone = binding.proctorPhoneNumber.getText().toString();
        String proctorEmail = binding.proctorEmail.getText().toString();

        // Validation: Ensure fields are not empty
        if (proctorName.isEmpty() || proctorPhone.isEmpty() || proctorEmail.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get reference to the user document
        DocumentReference userDocRef = database.collection("user").document(userId);

        // Start a transaction to update the user data
        database.runTransaction((Transaction.Function<Void>) transaction -> {
            // Get the document to check its current state
            DocumentSnapshot snapshot = transaction.get(userDocRef);

            // Update Firestore data
            transaction.update(userDocRef, "proctorName", proctorName);
            transaction.update(userDocRef, "proctorPhoneNumber", proctorPhone);
            transaction.update(userDocRef, "proctorEmail", proctorEmail);

            return null; // No return value needed, just updating the document
        }).addOnSuccessListener(aVoid -> {
            Toast.makeText(ProctorSection.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(ProctorSection.this, "Failed to save data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;  // Prevent memory leaks
    }
}
