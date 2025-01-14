package com.example.travelmate.usersection;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.travelmate.R;
import com.example.travelmate.databinding.FragmentUserBinding;
import com.example.travelmate.homesection.SignUp;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

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





//        SharedPreferences sharedPreferences=getContext().getSharedPreferences(
//                "save", MODE_PRIVATE);

//        binding.gender.setChecked(sharedPreferences.getBoolean("value",true));
//
//        if(binding.gender.isChecked())
//            binding.genderDisp.setText("Female");
//        else
//            binding.genderDisp.setText("Male");


//        binding.gender.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(binding.gender.isChecked()) {
//                    binding.genderDisp.setText("Female");
//                    SharedPreferences.Editor editor=view.getContext().getSharedPreferences("save",MODE_PRIVATE).edit();
//                    editor.putBoolean("value",true);
//                    editor.apply();
//                    binding.gender.setChecked(true);
//                }
//
//                else {
//                    binding.genderDisp.setText("Male");
//                    SharedPreferences.Editor editor=view.getContext().getSharedPreferences("save",MODE_PRIVATE).edit();
//                    editor.putBoolean("value",false);
//                    editor.apply();
//                    binding.gender.setChecked(false);
//
//                }
//            }
//        });







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
                     String nickname = documentSnapshot.getString("userName")+" ";
                    binding.name.setText("@"+nickname.substring(0,nickname.indexOf(" ")));










                  binding.profileTxt.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          Intent intent;
                          intent = new Intent(getActivity(), ProfileSection.class);
                          startActivity(intent);
                      }
                  });

                    binding.parentTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent;
                            intent = new Intent(getActivity(), ParentsSection.class);
                            startActivity(intent);
                        }
                    });

                    binding.proctorTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent;
                            intent = new Intent(getActivity(), ProctorSection.class);
                            startActivity(intent);
                        }
                    });

                    binding.cabTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent;
                            intent = new Intent(getActivity(), CabSection.class);
                            startActivity(intent);
                        }
                    });

                    binding.sosTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent;
                            intent = new Intent(getActivity(), MapsActivitySos.class);
                            startActivity(intent);
                        }
                    });
                    binding.aboutTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent;
                            intent = new Intent(getActivity(), AboutSection.class);
                            startActivity(intent);
                        }
                    });
                    binding.emergencyBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LayoutInflater inflater = LayoutInflater.from(getContext());
                            View bottomSheetView = inflater.inflate(R.layout.emergency_bottom_sheet, null);

                            // Create BottomSheetDialog
                            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
                            bottomSheetDialog.setContentView(bottomSheetView);

                            // Show the bottom sheet
                            bottomSheetDialog.show();


                            LinearLayout police = bottomSheetView.findViewById(R.id.police);
                            LinearLayout women = bottomSheetView.findViewById(R.id.women_helpline);
                            LinearLayout ambulance = bottomSheetView.findViewById(R.id.ambulance);
                            LinearLayout fire = bottomSheetView.findViewById(R.id.fire_helpline);


                            police.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                  Intent intent= new Intent(Intent.ACTION_DIAL);
                                  intent.setData(Uri.parse("tel:100"));
                                  startActivity(intent);
                                }
                            });

                            women.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent= new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:1090"));
                                    startActivity(intent);
                                }
                            });

                            ambulance.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent= new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:102"));
                                    startActivity(intent);
                                }
                            });

                            fire.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent= new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:101"));
                                    startActivity(intent);
                                }
                            });

                        }
                    });












//                    if (!documentSnapshot.getString("userAge").equals(" "))
//                        // Toast.makeText(getActivity(), "Error1", Toast.LENGTH_SHORT).show();
//                        binding.age.setText(documentSnapshot.getString("userAge"));

//                    if (!documentSnapshot.getString("userGender").equals(" "))
//                    {binding.genderDisp.setText(documentSnapshot.getString("userGender"));}
//                    //Toast.makeText(getActivity(), "Error3", Toast.LENGTH_SHORT).show();


//                    if (!documentSnapshot.getString("userYear").equals(" ")) {
//                        String yr_retrive = documentSnapshot.getString("userYear");
//                        if (yr_retrive.equals("1st yr"))
//                        {
//                            binding.firstyr.setBackgroundColor(getResources().getColor(R.color.light_blue));
//                            binding.firstyr.setTextColor(getResources().getColor(R.color.black));
//                        }
//                        else if (yr_retrive.equals("2nd yr")) {
//                            binding.secondyr.setBackgroundColor(getResources().getColor(R.color.light_blue));
//                            binding.secondyr.setTextColor(getResources().getColor(R.color.black));
//
//                        }
//                        else if (yr_retrive.equals("3rd yr")) {
//                            binding.thirdyr.setBackgroundColor(getResources().getColor(R.color.light_blue));
//                            binding.thirdyr.setTextColor(getResources().getColor(R.color.black));
//
//                        }
//                        else
//                        {
//                            binding.fourthyr.setBackgroundColor(getResources().getColor(R.color.light_blue));
//                            binding.fourthyr.setTextColor(getResources().getColor(R.color.black));
//
//                        }
//
//                    }

//                    if (!documentSnapshot.getString("userOrigin").equals(" "))
//                        binding.placeOfOrigin.setText(documentSnapshot.getString("userOrigin"));
                    //Toast.makeText(getActivity(), "Error4", Toast.LENGTH_SHORT).show();



                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });





//        binding.save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                user_details userDetails=new user_details();
//                if(binding.age.getText().toString().isEmpty())
//                    Toast.makeText(getActivity(), "Enter Age", Toast.LENGTH_SHORT).show();
//
//                else if(binding.genderDisp.getText().toString().isEmpty())
//                    Toast.makeText(getActivity(), "Select Gender", Toast.LENGTH_SHORT).show();
//                else if(yr.isEmpty())
//                    Toast.makeText(getActivity(), "Select Year", Toast.LENGTH_SHORT).show();
//                else if(binding.placeOfOrigin.getText().toString().isEmpty())
//                    Toast.makeText(getActivity(), "Select State", Toast.LENGTH_SHORT).show();
//                else {
//                    firestore.runTransaction(new Transaction.Function<Void>() {
//                                @Override
//                                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
//                                    // DocumentSnapshot snapshot = transaction.get(sfDocRef);
//
//
//                                    transaction.update(documentReference, "userAge", binding.age.getText().toString());
//                                    transaction.update(documentReference, "userGender", binding.genderDisp.getText().toString());
//                                    transaction.update(documentReference, "userOrigin", binding.placeOfOrigin.getText().toString());
//                                    transaction.update(documentReference, "userYear", yr);
//
//                                    // Success
//                                    return null;
//                                }
//                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//
//                }
//
//            }
//        });

//        binding.firstyr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                yr = (String) binding.firstyr.getText().toString();
//                binding.firstyr.setBackgroundColor(getResources().getColor(R.color.light_blue));
//                binding.secondyr.setBackgroundColor(getResources().getColor(R.color.blue));
//                binding.thirdyr.setBackgroundColor(getResources().getColor(R.color.blue));
//                binding.fourthyr.setBackgroundColor(getResources().getColor(R.color.blue));
//                binding.firstyr.setTextColor(getResources().getColor(R.color.black));
//                binding.thirdyr.setTextColor(getResources().getColor(R.color.white));
//                binding.secondyr.setTextColor(getResources().getColor(R.color.white));
//                binding.fourthyr.setTextColor(getResources().getColor(R.color.white));
//
//            }
//        });

//        binding.secondyr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                yr = (String) binding.secondyr.getText().toString();
//                binding.firstyr.setBackgroundColor(getResources().getColor(R.color.blue));
//                binding.secondyr.setBackgroundColor(getResources().getColor(R.color.light_blue));
//                binding.thirdyr.setBackgroundColor(getResources().getColor(R.color.blue));
//                binding.fourthyr.setBackgroundColor(getResources().getColor(R.color.blue));
//                binding.secondyr.setTextColor(getResources().getColor(R.color.black));
//                binding.thirdyr.setTextColor(getResources().getColor(R.color.white));
//                binding.fourthyr.setTextColor(getResources().getColor(R.color.white));
//                binding.firstyr.setTextColor(getResources().getColor(R.color.white));
//            }
//        });


//        binding.thirdyr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                yr = (String) binding.thirdyr.getText().toString();
//                binding.firstyr.setBackgroundColor(getResources().getColor(R.color.blue));
//                binding.secondyr.setBackgroundColor(getResources().getColor(R.color.blue));
//                binding.thirdyr.setBackgroundColor(getResources().getColor(R.color.light_blue));
//                binding.fourthyr.setBackgroundColor(getResources().getColor(R.color.blue));
//                binding.thirdyr.setTextColor(getResources().getColor(R.color.black));
//                binding.fourthyr.setTextColor(getResources().getColor(R.color.white));
//                binding.secondyr.setTextColor(getResources().getColor(R.color.white));
//                binding.firstyr.setTextColor(getResources().getColor(R.color.white));
//            }
//        });



//        binding.fourthyr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                yr = (String) binding.fourthyr.getText().toString();
//                binding.firstyr.setBackgroundColor(getResources().getColor(R.color.blue));
//                binding.secondyr.setBackgroundColor(getResources().getColor(R.color.blue));
//                binding.thirdyr.setBackgroundColor(getResources().getColor(R.color.blue));
//                binding.fourthyr.setBackgroundColor(getResources().getColor(R.color.light_blue));
//                binding.fourthyr.setTextColor(getResources().getColor(R.color.black));
//                binding.thirdyr.setTextColor(getResources().getColor(R.color.white));
//                binding.secondyr.setTextColor(getResources().getColor(R.color.white));
//                binding.firstyr.setTextColor(getResources().getColor(R.color.white));
//            }
//        });


//        String[] state= getResources().getStringArray(R.array.state);
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_states, state);
//        binding.placeOfOrigin.setAdapter(arrayAdapter);










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