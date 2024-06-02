package com.example.daiplan;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.daiplan.databinding.ActivityMainBinding;
import com.example.daiplan.fragments.HomeFragment;
import com.example.daiplan.fragments.StatisticFragment;
import com.example.daiplan.notification.ReminderBroadcast;
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    HomeFragment homeFragment = new HomeFragment(this);
    StatisticFragment statisticFragment = new StatisticFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(homeFragment);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) { replaceFragment(homeFragment);}
            if (item.getItemId() == R.id.statistic) { replaceFragment(statisticFragment);}
            return true;
        });
    }
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}