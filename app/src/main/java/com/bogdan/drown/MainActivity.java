package com.bogdan.drown;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import restclient.Drone;
import restclient.NetworkManager;
import restclient.Telemetry;
import restclient.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DroneFragment.OnListFragmentInteractionListener {
    public static final String TAG = "MainActivity";
    private User profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Drone.setContext(getApplicationContext());
        Telemetry.setContext(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!NetworkManager.hasTokens()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }
        initialize();
        setDefaultFragment();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.my_drones) {
            Fragment fragment = new DroneFragment();
            replaceFragment(fragment);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setDefaultFragment() {
        Fragment fragment = new DroneFragment();
        replaceFragment(fragment);
    }

    public void replaceFragment(Fragment destFragment) {
        replaceFragment(destFragment, null);
    }

    // Replace current Fragment with the destination Fragment.
    public void replaceFragment(Fragment destFragment, String tag)
    {
        // First get FragmentManager object.
        FragmentManager fragmentManager = this.getSupportFragmentManager();

        // Begin Fragment transaction.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the layout holder with the required Fragment object.
        if (tag != null) {
            fragmentTransaction.replace(R.id.main_outlet, destFragment, tag);
            fragmentTransaction.addToBackStack(tag);
        } else {
            fragmentTransaction.replace(R.id.main_outlet, destFragment);
        }

        // Commit the Fragment replace action.
        fragmentTransaction.commit();
    }

    private void initialize() {
        if (NetworkManager.hasProfile()) {
            profile = NetworkManager.getProfile();
            updateProfileInfo();
        } else {
            try {
                NetworkManager.getService().getProfile().enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        profile = response.body();
                        updateProfileInfo();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
            } catch (Exception err) {
                Log.wtf(TAG, err);
            }
        }

    }

    private void updateProfileInfo() {

        ((TextView)findViewById(R.id.navTextView)).setText(profile.getName());
        ((TextView)findViewById(R.id.navSubTextView)).setText(profile.getEmail());
    }

    @Override
    public void onListFragmentInteraction(Drone drone) {
        replaceFragment(DroneDetails.newInstance(drone.getDroneId()), DroneDetails.TAG_FRAGMENT);
    }
}
