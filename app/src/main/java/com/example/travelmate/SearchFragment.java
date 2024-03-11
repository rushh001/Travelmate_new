package com.example.travelmate;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.travelmate.databinding.FragmentSearchBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class SearchFragment extends Fragment {

    FragmentSearchBinding binding;
    ArrayList<user_details> userDetails;
    mates_list matesList;
    FirebaseFirestore db;
    user_details user_details;

    public SearchFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        //Toast.makeText(getActivity(), " error1", Toast.LENGTH_SHORT).show();

        binding.matesRecyclerview.setHasFixedSize(true);
        db=FirebaseFirestore.getInstance();

        //Toast.makeText(getActivity(), " error1", Toast.LENGTH_SHORT).show();
        userDetails=new ArrayList<>();

       // Toast.makeText(getActivity(), " error2", Toast.LENGTH_SHORT).show();
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        binding.matesRecyclerview.setLayoutManager(layoutManager);

       // Toast.makeText(getActivity(), " error3", Toast.LENGTH_SHORT).show();
        matesList=new mates_list((FragmentActivity) getContext(),userDetails);
        binding.matesRecyclerview.setAdapter(matesList);
        EventChangeListner();
        //Toast.makeText(getActivity(), " error2", Toast.LENGTH_SHORT).show();


        return binding.getRoot();





    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(binding.sheet);
        behavior.setPeekHeight(100);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);



        String[] from= getResources().getStringArray(R.array.places);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_states, from);
        binding.from.setAdapter(arrayAdapter);

        String[] to= getResources().getStringArray(R.array.places);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(requireContext(), R.layout.dropdown_states, to);
        binding.to.setAdapter(arrayAdapter2);

     binding.dateslot.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

             DatePickerDialog dialog=new DatePickerDialog(getContext(), R.style.picker_theme, new DatePickerDialog.OnDateSetListener() {
                 @Override
                 public void onDateSet(DatePicker view, int year, int month, int day) {
                     binding.dateslot.setText(String.valueOf(day)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year));
                 }
             }, 2024, 1, 20);
             dialog.show();

         }
     });

     binding.timeslot.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

             TimePickerDialog dialog_box =new TimePickerDialog(getContext(),R.style.picker_theme ,new TimePickerDialog.OnTimeSetListener() {
                 @Override
                 public void onTimeSet(TimePicker view, int hour, int minute) {
binding.timeslot.setText(String.valueOf(hour)+":"+String.valueOf(minute));
                 }
             },15,00,false);
             dialog_box.show();
         }
     });




    }

    private void EventChangeListner() {
        db.collection("user").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!= null)
                {
                    Toast.makeText(getActivity(), "Firebase error", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getActivity(), " error3", Toast.LENGTH_SHORT).show();

                for (DocumentChange dc:value.getDocumentChanges()){
                    Toast.makeText(getActivity(), " error4", Toast.LENGTH_SHORT).show();

                    if (dc.getType()== DocumentChange.Type.ADDED){
                        userDetails.add(dc.getDocument().toObject(user_details.class));
                        Toast.makeText(getActivity(), " error5", Toast.LENGTH_SHORT).show();
                       // Toast.makeText(getActivity(), " error6", Toast.LENGTH_SHORT).show();

                      // userDetails.add(user_details);

                        matesList.notifyDataSetChanged();
                        Toast.makeText(getActivity(), " error8", Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });
    }
}