package org.testytu.mezunapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.checkerframework.checker.nullness.qual.NonNull;

public class MainActivity extends AppCompatActivity implements
        BottomNavigationView.OnItemSelectedListener {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_test);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this);

        // Set the initial fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeShowFragment()).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        Fragment selectedFragment;

        if (itemId == R.id.action_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeShowFragment()).commit();
            return true;
        } else if (itemId == R.id.action_search) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new SearchFragment()).commit();
            return true;
        } else if (itemId == R.id.action_bell) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FriendRequestsFragment()).commit();
            return true;
        } else if (itemId == R.id.action_profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ProfileFragment()).commit();
            return true;
        }


        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

        return true;
    }
}
