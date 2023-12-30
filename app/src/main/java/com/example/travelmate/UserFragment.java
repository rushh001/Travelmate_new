package com.example.travelmate;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class UserFragment extends Fragment {

    SwitchMaterial switchMaterial;
    TextView gender_display;
EditText age,from,to;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_user, container, false);
        switchMaterial=view.findViewById(R.id.gender);
        gender_display=view.findViewById(R.id.gender_disp);
        SharedPreferences sharedPreferences=view.getContext().getSharedPreferences(
                "save", MODE_PRIVATE);

        switchMaterial.setChecked(sharedPreferences.getBoolean("value",true));

        if(switchMaterial.isChecked()==true)
            gender_display.setText("Female");
        else
            gender_display.setText("Male");



        switchMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchMaterial.isChecked()==true) {
                    gender_display.setText("Female");
                    SharedPreferences.Editor editor=view.getContext().getSharedPreferences("save",MODE_PRIVATE).edit();
                    editor.putBoolean("value",true);
                    editor.apply();
                    switchMaterial.setChecked(true);
                }

                else {
                    gender_display.setText("Male");
                    SharedPreferences.Editor editor=view.getContext().getSharedPreferences("save",MODE_PRIVATE).edit();
                    editor.putBoolean("value",false);
                    editor.apply();
                    switchMaterial.setChecked(false);

                }
            }
        });
        return  view;
    }
}