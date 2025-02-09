package com.example.travelmate.homesection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travelmate.R;
import com.example.travelmate.data.ChatList;
import com.example.travelmate.data.userMessage;
import com.example.travelmate.data.user_details;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUp extends AppCompatActivity {
    Button button;
    FirebaseAuth auth;
    FirebaseFirestore database;
    GoogleSignInClient googleSignInClient;
    int RC_SIGN_IN = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        button = findViewById(R.id.SignUpbtn);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(SignUp.this,"phase a",Toast.LENGTH_SHORT).show();
                googleSignIn();
            }

        });

        //Toast.makeText(SignUp.this,"phase 1",Toast.LENGTH_SHORT).show();

//
if(auth.getCurrentUser()!=null){
   // Toast.makeText(SignUp.this,"phase 2",Toast.LENGTH_SHORT).show();

    Intent intent=new Intent(SignUp.this, Navigation_bar.class);
    startActivity(intent);
    finish();
}
        }

    private void googleSignIn() {
        Intent intent=googleSignInClient.getSignInIntent();
        startActivityForResult(intent,RC_SIGN_IN);
       // Toast.makeText(SignUp.this,"phase1",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task= GoogleSignIn.getSignedInAccountFromIntent(data);
            try{

                GoogleSignInAccount account= task.getResult(ApiException.class);
               //Toast.makeText(SignUp.this,"phase2",Toast.LENGTH_SHORT).show();

                firebaseAuth(account.getIdToken());
            }

            catch (ApiException e)
            {
                Toast.makeText(SignUp.this,e.toString(),Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void firebaseAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) {
                        String userId = user.getUid();

                        // Check if user already exists in Firestore
                        database.collection("user").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document != null && document.exists()) {
                                        // User already exists, proceed to the next activity
                                        Intent intent = new Intent(SignUp.this, Navigation_bar.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // Initialize user data and chat list
                                        userData(user);
                                    }
                                } else {
                                    Toast.makeText(SignUp.this, "Failed to check user data.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(SignUp.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void userData(FirebaseUser user) {
        String userId = user.getUid();

        // Create a new user entry
        user_details userDetails = new user_details();
        userDetails.setUserId(userId);
        userDetails.setUserName(user.getDisplayName());
        userDetails.setUserProfile(user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "");
        userDetails.setUserEmail(user.getEmail() != null ? user.getEmail() : "");
//        userDetails.setUserDestination(" ");
//        userDetails.setUserSource(" ");
//        userDetails.setUserOrigin(" ");
//        userDetails.setUserGender(" ");
//        userDetails.setUserAge(" ");
//        userDetails.setUserYear(" ");
//        userDetails.setUserTime(" ");
//        userDetails.setUserDate(" ");

        database.collection("user").document(userId).set(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    userChatList(userId);
                } else {
                    Toast.makeText(SignUp.this, "Failed to save user data.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void userChatList(String userId) {
        // Create a new chat list entry
        ChatList chatList = new ChatList();
        chatList.setUserId(userId);

        database.collection("chatlist").document(userId).set(chatList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    userMessages(userId);
                } else {
                    Toast.makeText(SignUp.this, "Failed to create chat list.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void userMessages(String userId) {
        // Create an instance of the data class
        userMessage messageData = new userMessage();
        messageData.addMessage(userId, "Welcome to TravelMate! This is your personalized message.");

        // Save the data to Firestore
        database.collection("messages")
                .document(userId) // Document with userId
                .collection("user_chats") // Nested collection
                .document(userId) // Document inside nested collection
                .set(messageData) // Set the data
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Success feedback
                            Intent intent = new Intent(SignUp.this, Navigation_bar.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            // Failure feedback
                            Toast.makeText(SignUp.this, "Failed to initialize user messages.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }









//
//    private void firebaseAuth(String idToken) {
//        //Toast.makeText(SignUp.this,idToken,Toast.LENGTH_SHORT).show();
//        AuthCredential credential= GoogleAuthProvider.getCredential(idToken,null);
//        auth.signInWithCredential(credential).addOnCompleteListener(SignUp.this,new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//
//                   // Toast.makeText(SignUp.this,"phase3",Toast.LENGTH_SHORT).show();
//                        FirebaseUser user = auth.getCurrentUser();
//                        user_details userDetails=new user_details();
//                        userDetails.setUserId(user.getUid());
//                        userDetails.setUserName(user.getDisplayName());
//                        userDetails.setUserProfile(user.getPhotoUrl().toString());
//                        userDetails.setUserEmail(user.getEmail().toString());
//
//
//
//                        userDetails.setUserDestination(" ");
//                        userDetails.setUserSource(" ");
//                        userDetails.setUserOrigin(" ");
//                        userDetails.setUserGender(" ");
//                        userDetails.setUserAge(" ");
//                        userDetails.setUserYear(" ");
//                        userDetails.setUserTime(" ");
//                        userDetails.setUserDate(" ");
//                         String id=user.getUid();
//
//                         // Toast.makeText(SignUp.this,"phase4",Toast.LENGTH_SHORT).show();
//                        database.collection("user").document(id).set(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                //Toast.makeText(SignUp.this,"phase5",Toast.LENGTH_SHORT).show();
//                                if(task.isSuccessful())
//                                {  Intent intent = new Intent(SignUp.this, Navigation_bar.class);
//                                startActivity(intent);
//                                }
//                                else {
//                                    Toast.makeText(SignUp.this, "Something went wrong", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//
//                   }
//
//               else {
//                    Toast.makeText(SignUp.this,"failed",Toast.LENGTH_SHORT).show();
//                }
//
//
//            }
//        });
//    }

}
