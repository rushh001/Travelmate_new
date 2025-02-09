package com.example.travelmate.usersection;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.travelmate.R;
import com.example.travelmate.data.user_details;
import com.example.travelmate.databinding.ActivityCabSectionBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;

public class CabSection extends AppCompatActivity {

    private ActivityCabSectionBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore database;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCabSectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // Initialize FirebaseAuth and FirebaseFirestore
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        userId = auth.getCurrentUser().getUid();

        // Fetch and display existing cab data
        fetchCabData();

        // Update data on Save button click
        binding.saveCabDetailsButton.setOnClickListener(view -> saveCabData());
    }

    private void fetchCabData() {
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
            binding.carNumberPlate.setText(userDetails.getCabCarNumberPlate());
            binding.driverName.setText(userDetails.getCabDriverName());
            binding.driverPhoneNumber.setText(userDetails.getCabDriverNumber());
    }


    private void saveCabData() {
        // Collect data from UI fields
        String cNumberPlate = (binding.carNumberPlate.getText()).toString();
        String cdriverName = (binding.driverName.getText()).toString();
        String cdriverPhoneNumber = (binding.driverPhoneNumber.getText()).toString();

        // Validate all fields
        if (cNumberPlate.isEmpty() || cdriverName.isEmpty() || cdriverPhoneNumber.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get a reference to the user document
        DocumentReference usercabRef = database.collection("user").document(userId);

        // Run transaction to update data
        database.runTransaction((Transaction.Function<Void>) transaction -> {
            DocumentSnapshot snapshot = transaction.get(usercabRef);

            // Update Firestore data
            transaction.update(usercabRef, "cabCarNumberPlate", cNumberPlate);
            transaction.update(usercabRef, "cabDriverName", cdriverName);
            transaction.update(usercabRef, "cabDriverNumber", cdriverPhoneNumber);

            // Return null since we are just updating the document
            return null;
        }).addOnSuccessListener(aVoid -> {
            Toast.makeText(CabSection.this, "Cab details saved successfully", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(CabSection.this, "Failed to save data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null; // Prevent memory leaks
    }
}
