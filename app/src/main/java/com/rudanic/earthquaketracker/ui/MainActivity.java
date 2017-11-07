package com.rudanic.earthquaketracker.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.rudanic.earthquaketracker.AboutEarthquakeFragment;
import com.rudanic.earthquaketracker.AboutFragment;
import com.rudanic.earthquaketracker.AboutTsunamiFragment;
import com.rudanic.earthquaketracker.Assets;
import com.rudanic.earthquaketracker.EarthquakeFragment;
import com.rudanic.earthquaketracker.FactsEarthquakeFragment;
import com.rudanic.earthquaketracker.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    final String NAVIGATION_DRAWER_BACKGROUND_URL = "http://i.giphy.com/3o85xoKJdWyZ35P6OQ.gif";
    ImageView navigationDrawerBackgroundImage;
    Fragment fragment;
    Context context = this;
    String mLatitude, mLongitude, mDegree;
    TextView latitude, longitude, twoDegree, fourDegree, eightDegree, fifteenDegree;
    private LocationManager locationManager;
    private LocationListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragment = new EarthquakeFragment();

        // Get Location

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Toast.makeText(getApplicationContext(), location.getLongitude() + " " + location.getLatitude(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        configure_button();

/////////////////////////////////////////////////////////////////////////////////
        // What if open FAB

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
                View mView = layoutInflaterAndroid.inflate(R.layout.fab_dialogbox, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
                alertDialogBuilderUserInput.setView(mView);

                final EditText latitude = (EditText) mView.findViewById(R.id.latitude);
                final EditText longitude = (EditText) mView.findViewById(R.id.longitude);
                final RadioButton twoDegree = (RadioButton) mView.findViewById(R.id.two_degree);
                final RadioButton fourDegree = (RadioButton) mView.findViewById(R.id.four_degree);
                final RadioButton eightDegree = (RadioButton) mView.findViewById(R.id.eight_degree);
                final RadioButton fifteenDegree = (RadioButton) mView.findViewById(R.id.fifteen_degree);


                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {

                                submitFilterationDetail(view);
                                Assets.filter = true;
                                fragment.getFragmentManager().beginTransaction().detach(fragment).commit();
                                fragment.getFragmentManager().beginTransaction().attach(fragment).commit();

                            }

                            private void submitFilterationDetail(View view) {

                                if (fifteenDegree.isChecked()) {
                                    mDegree = "15";
                                } else if (eightDegree.isChecked()) {
                                    mDegree = "8";
                                } else if (fourDegree.isChecked()) {
                                    mDegree = "4";
                                } else if (twoDegree.isChecked()) {
                                    mDegree = "2";
                                }
                                mLatitude = latitude.getText().toString();
                                mLongitude = longitude.getText().toString();

                                SharedPreferences.Editor editor = getSharedPreferences(Assets.SHARED_PREF_NAME, MODE_PRIVATE).edit();

                                editor.putString("latitude", mLatitude);
                                editor.putString("longitude", mLongitude);
                                editor.putString("radius", mDegree);
                                editor.commit();
                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (navigationDrawerBackgroundImage == null) {
                    YoYo.with(Techniques.ZoomInRight)
                            .duration(900)
                            .playOn(findViewById(R.id.iv_background_navigation_drawer));
                    navigationDrawerBackgroundImage = (ImageView) findViewById(R.id.iv_background_navigation_drawer);
                    Glide.with(getBaseContext()).load(NAVIGATION_DRAWER_BACKGROUND_URL).into(navigationDrawerBackgroundImage);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (navigationDrawerBackgroundImage != null) {

                    Glide.clear(navigationDrawerBackgroundImage);
                    navigationDrawerBackgroundImage = null;
                }
            }
        };

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.layout_container, fragment);
        fragmentTransaction.commit();
    }

    //////////////////////////////////////////////////////////////////////////////////

    private void configure_button() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //noinspection MissingPermission
              //  locationManager.requestLocationUpdates("gps", 5000, 0, listener);
//            }
//        });

    }

    ////////////////////////////////////////////////////////////////////////////////

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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about_earthquake) {
            fragment = new AboutEarthquakeFragment();

        } else if (id == R.id.nav_about_tsunami) {
            fragment = new AboutTsunamiFragment();

        } else if (id == R.id.nav_fact_earthquake) {
            fragment = new FactsEarthquakeFragment();

        } else if (id == R.id.nav_home) {
            fragment = new EarthquakeFragment();

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_feedback) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "dhruval.18@gmail.com", null));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Suggestion for \"Earthquake News\"");
            intent.putExtra(Intent.EXTRA_TEXT, "Hello,");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(Intent.createChooser(intent, "Send email..."));
            }

        } else if (id == R.id.nav_rate_update) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.rudanic.earthquaketracker"));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Check out \"Earthquake News\"");
            intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.rudanic.earthquaketracker");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(Intent.createChooser(intent, "Share via"));
            }
        } else if (id == R.id.nav_about) {
            fragment = new AboutFragment();
        }

        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.layout_container, fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
