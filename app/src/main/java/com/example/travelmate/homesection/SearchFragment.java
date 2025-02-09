package com.example.travelmate.homesection;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.travelmate.R;
import com.example.travelmate.data.user_details;
import com.example.travelmate.databinding.ProfileDemoBinding;
import com.example.travelmate.mates_list;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


public class SearchFragment extends Fragment {

    ProfileDemoBinding binding;
    ArrayList<user_details> userDetails;
    mates_list adapter;
    FirebaseFirestore db;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth auth;


    public SearchFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =ProfileDemoBinding.inflate(inflater, container, false);
        //Toast.makeText(getActivity(), " error1", Toast.LENGTH_SHORT).show();
        return binding.getRoot();

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
//            BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(binding.sheet);
//            behavior.setPeekHeight(300);
//            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//dte and time current
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
//status bar color
        requireActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.white));
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        String[] from = getResources().getStringArray(R.array.places);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_states, from);
        binding.from.setAdapter(arrayAdapter);
        setupDropdownFix(binding.from);

        String[] to = getResources().getStringArray(R.array.places);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(requireContext(), R.layout.dropdown_states, to);
        binding.to.setAdapter(arrayAdapter2);
        setupDropdownFix(binding.to);





        binding.dateslot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dialog = new DatePickerDialog(getContext(), R.style.picker_theme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String formattedDate = String.format("%02d/%02d/%04d", day, month + 1, year);
                        binding.dateslot.setText(formattedDate);
//                     binding.dateslot.setText(String.valueOf(day)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year));
                    }
                }, currentYear, currentMonth, currentDay);
                dialog.show();

            }
        });

        binding.timeslot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog dialog_box = new TimePickerDialog(getContext(), R.style.picker_theme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {

                        String AM_PM;
                        if (hour < 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                            if (hour > 12) {
                                hour -= 12;
                            }
                        }

                        String formattedTime = String.format("%02d:%02d %s", hour, minute, AM_PM);
                        binding.timeslot.setText(formattedTime);

//             binding.timeslot.setText(String.valueOf(hour)+":"+String.valueOf(minute)+" "+AM_PM);
                    }
                }, currentHour, currentMinute, false);
                dialog_box.show();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.matesRecyclerview.setLayoutManager(layoutManager);

        // Toast.makeText(getActivity(), " error1", Toast.LENGTH_SHORT).show();
        //
        userDetails = new ArrayList<>();
        adapter = new mates_list(userDetails);
        binding.matesRecyclerview.setAdapter(adapter);

        // Toast.makeText(getActivity(), " error2", Toast.LENGTH_SHORT).show();
        db = FirebaseFirestore.getInstance();
        db.collection("user").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                //Toast.makeText(getActivity(), " error3", Toast.LENGTH_SHORT).show();
                for (DocumentSnapshot d : list) {//Toast.makeText(getActivity(), " error4", Toast.LENGTH_SHORT).show();
                    user_details obj = d.toObject(user_details.class);
                    userDetails.add(obj);

                }
                adapter.notifyDataSetChanged();

            }
        });
        //Toast.makeText(getActivity(), " error2", Toast.LENGTH_SHORT).show();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        auth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();
        DocumentReference documentReference = db.collection("user").document(id);

        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int c = 0;
                String From = binding.from.getText().toString();
                String to = binding.to.getText().toString();
                String date = binding.dateslot.getText().toString();
                String time = binding.timeslot.getText().toString();
                if (From.isEmpty())
                    Toast.makeText(getActivity(), "Enter the source", Toast.LENGTH_SHORT).show();


                else if (to.isEmpty())
                    Toast.makeText(getActivity(), "Enter the destination", Toast.LENGTH_SHORT).show();

                else if (date.isEmpty())
                    Toast.makeText(getActivity(), "Enter the date", Toast.LENGTH_SHORT).show();

                else if (time.isEmpty())
                    Toast.makeText(getActivity(), "Enter the time", Toast.LENGTH_SHORT).show();
                else {
                    user_details userDetails = new user_details();
                    db = FirebaseFirestore.getInstance();
                    db.runTransaction(new Transaction.Function<Void>() {
                        @Nullable
                        @Override
                        public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                            transaction.update(documentReference, "userSource", From);
                            transaction.update(documentReference, "userDestination", to);
                            transaction.update(documentReference, "userDate", date);
                            transaction.update(documentReference, "userTime", time);
                            return null;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getActivity(), "Results", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });


                }


            }
        });

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (!Objects.equals(documentSnapshot.getString("userSource"), " "))
                        binding.from.setText(documentSnapshot.getString("userSource"));
                    if (!Objects.equals(documentSnapshot.getString("userDestination"), " "))
                        binding.to.setText(documentSnapshot.getString("userDestination"));
                    if (!Objects.equals(documentSnapshot.getString("userDate"), " "))
                        binding.dateslot.setText(documentSnapshot.getString("userDate"));
                    if (!Objects.equals(documentSnapshot.getString("userTime"), " "))
                        binding.timeslot.setText(documentSnapshot.getString("userTime"));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });





    }




    private void setupDropdownFix(AutoCompleteTextView autoCompleteTextView) {
        autoCompleteTextView.setOnClickListener(v -> {
            autoCompleteTextView.showDropDown(); // Show full dropdown
        });

        autoCompleteTextView.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                autoCompleteTextView.showDropDown(); // Show full dropdown when focused
            }
        });
    }




}