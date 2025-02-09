package com.example.travelmate.usersection;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.travelmate.R;
import com.example.travelmate.data.user_details;
import com.example.travelmate.databinding.ActivityProfileSectionBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;

public class ProfileSection extends AppCompatActivity {

    private ActivityProfileSectionBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore database;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityProfileSectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        userId = auth.getCurrentUser().getUid();

        // Load and display user data if available
        loadUserData();

        // Set up the dropdown for "State" selection
        binding.placeOfOrigin.setOnClickListener(v -> setupStateDropdown());

        // Handle "Save" button click
        binding.saveButton.setOnClickListener(v -> saveUserData());
    }

    private void setupStateDropdown() {
       String[] states= getResources().getStringArray(R.array.indian_states);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, states);
        binding.placeOfOrigin.setAdapter(adapter);
        }

    private void loadUserData() {
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
        binding.userName.setText(userDetails.getUserName());
        binding.phoneNumber.setText(userDetails.getUserPhoneNumber());
        binding.userAge.setText(userDetails.getUserAge());
        binding.placeOfOrigin.setText(userDetails.getUserOrigin());

        // Set Academic Year selection
        if (userDetails.getUserYear() != null) {
            switch (userDetails.getUserYear()) {
                case "1st":
                    binding.yearGroup.check(R.id.year1);
                    break;
                case "2nd":
                    binding.yearGroup.check(R.id.year2);
                    break;
                case "3rd":
                    binding.yearGroup.check(R.id.year3);
                    break;
                case "4th":
                    binding.yearGroup.check(R.id.year4);
                    break;
            }
        }

        // Set Gender selection
        if (userDetails.getUserGender() != null) {
            if (userDetails.getUserGender().equalsIgnoreCase("Male")) {
                binding.genderGroup.check(R.id.genderMale);
            } else if (userDetails.getUserGender().equalsIgnoreCase("Female")) {
                binding.genderGroup.check(R.id.genderFemale);
            }
        }
    }

    private void saveUserData() {
        // Collect data from input fields
        String userName = binding.userName.getText().toString();
        String phoneNumber = binding.phoneNumber.getText().toString();
        String userAge = binding.userAge.getText().toString();
        String placeOfOrigin = binding.placeOfOrigin.getText().toString();

        // Validate if any required fields are empty
        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userAge.isEmpty()) {
            Toast.makeText(this, "Please enter your Age", Toast.LENGTH_SHORT).show();
            return;
        }
        if (placeOfOrigin.isEmpty()|| placeOfOrigin.equals(" ")) {
            Toast.makeText(this, "Please enter your place of origin", Toast.LENGTH_SHORT).show();
            return;
        }

        String academicYear;
        int selectedYearId = binding.yearGroup.getCheckedRadioButtonId();
        if (selectedYearId == -1) {
            academicYear = "";
            Toast.makeText(this, "Please select your academic year", Toast.LENGTH_SHORT).show();
            return;
        } else {
            RadioButton selectedYear = findViewById(selectedYearId);
            academicYear = selectedYear.getText().toString();
        }

        String gender;
        int selectedGenderId = binding.genderGroup.getCheckedRadioButtonId();
        if (selectedGenderId == -1) {
            gender = "";
            Toast.makeText(this, "Please select your gender", Toast.LENGTH_SHORT).show();
            return;
        } else {
            RadioButton selectedGender = findViewById(selectedGenderId);
            gender = selectedGender.getText().toString();
        }

        // Get reference to the user document
        DocumentReference userRef = database.collection("user").document(userId);

        // Start a transaction to update the user data
        database.runTransaction((Transaction.Function<Void>) transaction -> {
            // Get the document to check its current state
            DocumentSnapshot snapshot = transaction.get(userRef);

            // Update the fields in the document
            transaction.update(userRef, "userName", userName);
            transaction.update(userRef, "userPhoneNumber", phoneNumber);
            transaction.update(userRef, "userAge", userAge);
            transaction.update(userRef, "userOrigin", placeOfOrigin);
            transaction.update(userRef, "userYear", academicYear);
            transaction.update(userRef, "userGender", gender);

            // Return null since we are just updating the document
            return null;
        }).addOnSuccessListener(aVoid -> {
            Toast.makeText(ProfileSection.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(ProfileSection.this, "Failed to save data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;  // Prevent memory leaks
    }
}
