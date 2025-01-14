package com.example.travelmate.usersection;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travelmate.R;
import com.example.travelmate.data.user_details;
import com.example.travelmate.databinding.ActivityParentsSectionBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;

import java.util.Objects;

public class ParentsSection extends AppCompatActivity {

    private ActivityParentsSectionBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore database;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityParentsSectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firestore and DocumentReference
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        userId = auth.getCurrentUser().getUid();

        // Fetch and display existing data
        fetchParentData();

        // Update data on Save button click
        binding.saveButton.setOnClickListener(view -> saveParentsData());
    }

    private void fetchParentData() {
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
        binding.fatherName.setText(userDetails.getFatherName());
        binding.fatherPhoneNumber.setText(userDetails.getFatherPhoneNumber());
        binding.fatherEmail.setText(userDetails.getFatherEmail());
        binding.motherName.setText(userDetails.getMotherName());
        binding.motherPhoneNumber.setText(userDetails.getMotherPhoneNumber());

    }
        private void saveParentsData()  {
        // Collect data from UI fields
        String fName = (binding.fatherName.getText()).toString();
        String fPhone = (binding.fatherPhoneNumber.getText()).toString();
        String fEmail = (binding.fatherEmail.getText()).toString();
        String mName = (binding.motherName.getText()).toString();
        String mPhone = (binding.motherPhoneNumber.getText()).toString();

        // Validation: Ensure fields are not empty
        if (fName.isEmpty() || fPhone.isEmpty() || fEmail.isEmpty() || mName.isEmpty() || mPhone.isEmpty()) {
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
        transaction.update(userDocRef,"fatherName", fName);
                transaction.update(userDocRef,"fatherPhoneNumber", fPhone);
                transaction.update(userDocRef,"fatherEmail", fEmail);
                transaction.update(userDocRef,"motherName", mName);
                transaction.update(userDocRef, "motherPhoneNumber", mPhone);
                // Return null since we are just updating the document
                return null;
            }).addOnSuccessListener(aVoid -> {
                Toast.makeText(ParentsSection.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(ParentsSection.this, "Failed to save data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;  // Prevent memory leaks
    }
}
