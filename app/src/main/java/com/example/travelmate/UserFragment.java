package com.example.travelmate;

import static android.content.Context.MODE_PRIVATE;

import static com.google.type.Color.ALPHA_FIELD_NUMBER;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelmate.databinding.ActivityMainBinding;
import com.example.travelmate.databinding.FragmentUserBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.rpc.Status;
import com.google.rpc.context.AttributeContext;
import com.google.type.Color;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

public class UserFragment extends Fragment {
FragmentUserBinding binding;
GoogleSignInClient googleSignInClient;
    FirebaseAuth auth;

String yr="";
FirebaseFirestore firestore=FirebaseFirestore.getInstance();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         binding=FragmentUserBinding.inflate(inflater,container,false);
             return  binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        Toast.makeText(getActivity(), "Something went wrong0", Toast.LENGTH_SHORT).show();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        auth = FirebaseAuth.getInstance();
         binding.signout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                     if (auth.getCurrentUser() != null)
                         auth.signOut();
                     Intent intent = new Intent(getActivity(), SignUp.class);
                     startActivity(intent);
                 getActivity().finish();

                 }
         });





        SharedPreferences sharedPreferences=getContext().getSharedPreferences(
                "save", MODE_PRIVATE);

        binding.gender.setChecked(sharedPreferences.getBoolean("value",true));

        if(binding.gender.isChecked())
            binding.genderDisp.setText("Female");
        else
            binding.genderDisp.setText("Male");


        binding.gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.gender.isChecked()) {
                    binding.genderDisp.setText("Female");
                    SharedPreferences.Editor editor=view.getContext().getSharedPreferences("save",MODE_PRIVATE).edit();
                    editor.putBoolean("value",true);
                    editor.apply();
                    binding.gender.setChecked(true);
                }

                else {
                    binding.genderDisp.setText("Male");
                    SharedPreferences.Editor editor=view.getContext().getSharedPreferences("save",MODE_PRIVATE).edit();
                    editor.putBoolean("value",false);
                    editor.apply();
                    binding.gender.setChecked(false);

                }
            }
        });







        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String id= user.getUid();
        DocumentReference documentReference=firestore.collection("user").document(id);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    String url=documentSnapshot.getString("userProfile");
                    Picasso.get().load(url).into(binding.profileImage);

                    binding.fullName.setText(documentSnapshot.getString("userName"));
                 // Toast.makeText(getActivity(), "Error2", Toast.LENGTH_SHORT).show();

                    binding.name.setText("@"+documentSnapshot.getString("userName").substring(0,binding.fullName.getText().toString().indexOf(" ")));

                    if (!documentSnapshot.getString("userAge").equals(" "))
                       // Toast.makeText(getActivity(), "Error1", Toast.LENGTH_SHORT).show();
                        binding.age.setText(documentSnapshot.getString("userAge"));

                    if (!documentSnapshot.getString("userGender").equals(" "))
                    binding.genderDisp.setText(documentSnapshot.getString("userGender"));
                    //Toast.makeText(getActivity(), "Error3", Toast.LENGTH_SHORT).show();


                    if (!documentSnapshot.getString("userYear").equals(" ")) {
                        String yr_retrive = documentSnapshot.getString("userYear");
                        if (yr_retrive.equals("1st yr"))
                        {
                            binding.firstyr.setBackgroundColor(getResources().getColor(R.color.light_blue));
                           binding.firstyr.setTextColor(getResources().getColor(R.color.black));
                        }
                        else if (yr_retrive.equals("2nd yr")) {
                            binding.secondyr.setBackgroundColor(getResources().getColor(R.color.light_blue));
                            binding.secondyr.setTextColor(getResources().getColor(R.color.black));

                        }
                        else if (yr_retrive.equals("3rd yr")) {
                            binding.thirdyr.setBackgroundColor(getResources().getColor(R.color.light_blue));
                            binding.thirdyr.setTextColor(getResources().getColor(R.color.black));

                        }
                        else
                        {
                            binding.fourthyr.setBackgroundColor(getResources().getColor(R.color.light_blue));
                            binding.fourthyr.setTextColor(getResources().getColor(R.color.black));

                        }

                    }

                    if (!documentSnapshot.getString("userOrigin").equals(" "))
                        binding.placeOfOrigin.setText(documentSnapshot.getString("userOrigin"));
                    //Toast.makeText(getActivity(), "Error4", Toast.LENGTH_SHORT).show();



                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });





        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_details userDetails=new user_details();
                if(binding.age.getText().toString().isEmpty())
                    Toast.makeText(getActivity(), "Enter Age", Toast.LENGTH_SHORT).show();

              else if(binding.genderDisp.getText().toString().isEmpty())
                    Toast.makeText(getActivity(), "Select Gender", Toast.LENGTH_SHORT).show();
              else if(yr.isEmpty())
                    Toast.makeText(getActivity(), "Select Year", Toast.LENGTH_SHORT).show();
               else if(binding.placeOfOrigin.getText().toString().isEmpty())
                    Toast.makeText(getActivity(), "Select State", Toast.LENGTH_SHORT).show();
                else {
                    firestore.runTransaction(new Transaction.Function<Void>() {
                                @Override
                                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                    // DocumentSnapshot snapshot = transaction.get(sfDocRef);


                                    transaction.update(documentReference, "userAge", binding.age.getText().toString());
                                    transaction.update(documentReference, "userGender", binding.genderDisp.getText().toString());
                                    transaction.update(documentReference, "userOrigin", binding.placeOfOrigin.getText().toString());
                                    transaction.update(documentReference, "userYear", yr);

                                    // Success
                                    return null;
                                }
                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                                }
                            });

                         }

            }
        });

      binding.firstyr.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              yr = (String) binding.firstyr.getText().toString();
              binding.firstyr.setBackgroundColor(getResources().getColor(R.color.light_blue));
              binding.secondyr.setBackgroundColor(getResources().getColor(R.color.blue));
              binding.thirdyr.setBackgroundColor(getResources().getColor(R.color.blue));
              binding.fourthyr.setBackgroundColor(getResources().getColor(R.color.blue));
              binding.firstyr.setTextColor(getResources().getColor(R.color.black));
              binding.thirdyr.setTextColor(getResources().getColor(R.color.white));
              binding.secondyr.setTextColor(getResources().getColor(R.color.white));
              binding.fourthyr.setTextColor(getResources().getColor(R.color.white));

          }
      });

        binding.secondyr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yr = (String) binding.secondyr.getText().toString();
                binding.firstyr.setBackgroundColor(getResources().getColor(R.color.blue));
                binding.secondyr.setBackgroundColor(getResources().getColor(R.color.light_blue));
                binding.thirdyr.setBackgroundColor(getResources().getColor(R.color.blue));
                binding.fourthyr.setBackgroundColor(getResources().getColor(R.color.blue));
                binding.secondyr.setTextColor(getResources().getColor(R.color.black));
                binding.thirdyr.setTextColor(getResources().getColor(R.color.white));
                binding.fourthyr.setTextColor(getResources().getColor(R.color.white));
                binding.firstyr.setTextColor(getResources().getColor(R.color.white));
            }
        });


        binding.thirdyr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yr = (String) binding.thirdyr.getText().toString();
                binding.firstyr.setBackgroundColor(getResources().getColor(R.color.blue));
                binding.secondyr.setBackgroundColor(getResources().getColor(R.color.blue));
                binding.thirdyr.setBackgroundColor(getResources().getColor(R.color.light_blue));
                binding.fourthyr.setBackgroundColor(getResources().getColor(R.color.blue));
                binding.thirdyr.setTextColor(getResources().getColor(R.color.black));
                binding.fourthyr.setTextColor(getResources().getColor(R.color.white));
                binding.secondyr.setTextColor(getResources().getColor(R.color.white));
                binding.firstyr.setTextColor(getResources().getColor(R.color.white));
            }
        });



        binding.fourthyr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yr = (String) binding.fourthyr.getText().toString();
                binding.firstyr.setBackgroundColor(getResources().getColor(R.color.blue));
                binding.secondyr.setBackgroundColor(getResources().getColor(R.color.blue));
                binding.thirdyr.setBackgroundColor(getResources().getColor(R.color.blue));
                binding.fourthyr.setBackgroundColor(getResources().getColor(R.color.light_blue));
                binding.fourthyr.setTextColor(getResources().getColor(R.color.black));
                binding.thirdyr.setTextColor(getResources().getColor(R.color.white));
                binding.secondyr.setTextColor(getResources().getColor(R.color.white));
                binding.firstyr.setTextColor(getResources().getColor(R.color.white));
            }
        });


            String[] state= getResources().getStringArray(R.array.state);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_states, state);
            binding.placeOfOrigin.setAdapter(arrayAdapter);










    }

//    private void signout() {
//        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                 startActivity(new Intent(getActivity(),SignUp.class));
//
//            }
//        });
//    }

}

