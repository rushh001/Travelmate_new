package com.example.travelmate.homesection;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.travelmate.R;
import com.example.travelmate.chatsection.ChatFragment;
import com.example.travelmate.usersection.UserFragment;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class Navigation_bar extends AppCompatActivity {
 MeowBottomNavigation bottomNavigation;
//ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding=ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(R.layout.activity_navigation_bar);
bottomNavigation= findViewById(R.id.bottom_navigation);
replaceFragment(new SearchFragment());
  bottomNavigation.show(2,true);
        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.icon_name_profile__style_false__size_64px));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.icon_name_search__style_false__size_64px));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.icon_name_chat__style_false__size_64px));

        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {


                switch(model.getId())
                {
                    case 1:
                        replaceFragment(new UserFragment());
                        break;
                    case 2:
                        replaceFragment(new SearchFragment());
                        break;
                    case 3:
                        replaceFragment(new ChatFragment());
                        break;


                }
                return null;
            }
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.commit();
    }
}