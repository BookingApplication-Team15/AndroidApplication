package com.example.bookingapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.bookingapplication.R;
import com.example.bookingapplication.fragments.FragmentTransition;
import com.example.bookingapplication.fragments.analytics.AnalyticsFragment;
import com.example.bookingapplication.util.SharedPreferencesManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bookingapplication.databinding.ActivityHomeBinding;
import com.example.bookingapplication.model.enums.UserType;
public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private Toolbar toolbar;
    private NavController navController;
    private BottomNavigationView navView;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = binding.toolbar;

        setSupportActionBar(toolbar);
        getSupportActionBar();

        navView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_account, R.id.navigation_notifications,R.id.guestReservationsFragment,
                R.id.addedPropertiesFragment,R.id.hostPropertiesFragment,R.id.commentsFragment,R.id.reportedUsersFragment,
                R.id.loginActivity,R.id.accountAdminFragment, R.id.createAccommodationFragment,R.id.accommodationApprovingFragment,
                R.id.accommodationsForHostFragment, R.id.guestReservationsFragment, R.id.hostReservationsFragment,
                R.id.analyticsAnnualFragment, R.id.guestFavouritesFragment, R.id.approveCommentsFragment,
                        R.id.myCommentsFragment, R.id.notificationForHostFragment)

                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);



        Intent intent = getIntent();
        UserType role = UserType.valueOf(intent.getStringExtra("Role"));
        switch (role) {
            case GUEST:
                Toast.makeText(getApplicationContext(), role.toString(), Toast.LENGTH_SHORT).show();
                navView.inflateMenu(R.menu.bottom_nav_menu_guest);
                break;
            case HOST:
                Toast.makeText(getApplicationContext(), role.toString(), Toast.LENGTH_SHORT).show();
                navView.inflateMenu(R.menu.bottom_nav_menu_host);
                break;
            case ADMIN:
                Toast.makeText(getApplicationContext(), role.toString(), Toast.LENGTH_SHORT).show();
                navView.inflateMenu(R.menu.bottom_nav_menu_admin);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.loginActivity){
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            SharedPreferencesManager.clearUserInfo(getApplicationContext());
            startActivity(intent);
            finish();
            return true;
        }else if(item.getItemId() == R.id.analyticsAnnualFragment){
            navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
            navController.navigate(R.id.analyticsAnnualFragment);
            return true;
        }else if(item.getItemId() == R.id.notificationForHostFragment){
            navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
            navController.navigate(R.id.notificationForHostFragment);
            return true;
        }
        else if(item.getItemId() == R.id.myCommentsFragment){
            navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
            navController.navigate(R.id.myCommentsFragment);
            return true;
        }
        else if(item.getItemId() == R.id.addCommentsFragment){
            navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
            navController.navigate(R.id.addCommentsFragment2);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Intent intent = getIntent();
        UserType role = UserType.valueOf(intent.getStringExtra("Role"));
        switch (role) {
            case GUEST:
                getMenuInflater().inflate(R.menu.toolbar_menu_guest, menu);
                break;
            case HOST:
                getMenuInflater().inflate(R.menu.toolbar_menu_host, menu);
                break;
            case ADMIN:
                getMenuInflater().inflate(R.menu.toolbar_menu, menu);
                break;
            default:
                break;
        }

        return true;
    }

}