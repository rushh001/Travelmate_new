package com.example.travelmate;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {
Button button;
FirebaseAuth auth;
FirebaseFirestore database;
GoogleSignInClient googleSignInClient;
int RC_SIGN_IN=20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
auth=FirebaseAuth.getInstance();
database=FirebaseFirestore.getInstance();
button=findViewById(R.id.SignUpbtn);
GoogleSignInOptions gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
googleSignInClient=GoogleSignIn.getClient(this,gso);
button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        googleSignIn();
    }


});

if(auth.getCurrentUser()!=null){
    Intent intent=new Intent(SignUp.this,Navigation_bar.class);
    startActivity(intent);
    finish();
}
        }

    private void googleSignIn() {
        Intent intent=googleSignInClient.getSignInIntent();
        startActivityForResult(intent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task= GoogleSignIn.getSignedInAccountFromIntent(data);
            try{

                GoogleSignInAccount account= task.getResult(ApiException.class);
               // Toast.makeText(SignUp.this,"phase2",Toast.LENGTH_SHORT).show();

                firebaseAuth(account.getIdToken());
            }

            catch (ApiException e)
            {
                Toast.makeText(SignUp.this,e.toString(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuth(String idToken) {
        AuthCredential credential= GoogleAuthProvider.getCredential(idToken,null);
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                        Toast.makeText(SignUp.this, "phase5", Toast.LENGTH_SHORT).show();

                        FirebaseUser user = auth.getCurrentUser();
                        user_details userDetails=new user_details();
                        userDetails.setUserId(user.getUid());
                        userDetails.setUserName(user.getDisplayName());
                        userDetails.setUserProfile(user.getPhotoUrl().toString());
                         String id=user.getUid();
//
                        database.collection("user").document(id).set(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {  Intent intent = new Intent(SignUp.this, Navigation_bar.class);
                                startActivity(intent); }
                                else {
                                    Toast.makeText(SignUp.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }


            }
        });
    }
}

