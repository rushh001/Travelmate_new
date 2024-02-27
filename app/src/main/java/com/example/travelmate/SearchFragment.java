package com.example.travelmate;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.travelmate.databinding.FragmentSearchBinding;
import com.example.travelmate.databinding.FragmentUserBinding;


public class SearchFragment extends Fragment {

    FragmentSearchBinding binding;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



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
}