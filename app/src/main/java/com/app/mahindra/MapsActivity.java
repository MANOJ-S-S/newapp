package com.app.mahindra;

import android.Manifest;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    ActionBar toolbar;
    LinearLayout nav, ll_auto, dwn_l,manual_but,manual_flt,stop_lout;
    CardView autoMode;
    ListView lv;
    LatLng latLng;
    ProgressBar circle_pop;
    int i=0;
    FragmentManager fm;
    //Marker mCurrLocationMarker=null;
    Location mLastLocation;
    ImageView play;
    LocationManager locationManager;
    GoogleApiClient mGoogleApiClient;

    private static final int MAP_PERMISSION=200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        hideNavigationBarAndStatusBar();


        //Arraylist for file list view







        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(MapsActivity.this.LOCATION_SERVICE);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        autoMode = findViewById(R.id.auto_mode);
        autoMode.setVisibility(View.INVISIBLE);
        manual_flt=findViewById(R.id.manual_float);
        manual_but=findViewById(R.id.manual_butt);
        manual_flt.setVisibility(View.INVISIBLE);
        stop_lout=findViewById(R.id.stop_layout);

        nav = findViewById(R.id.navigation);
        play=findViewById(R.id.playicon);
        dwn_l = findViewById(R.id.dwn_lout);
        ll_auto = findViewById(R.id.ll_auto);


        ll_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // showCustomDialog();
                manual_but.setBackgroundColor(Color.TRANSPARENT);
                manual_flt.setVisibility(View.INVISIBLE);
                ll_auto.setBackgroundColor(Color.RED);
                autoMode.setVisibility(View.VISIBLE);


            }
        });

        manual_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                autoMode.setVisibility(View.INVISIBLE);
                manual_but.setBackgroundColor(Color.RED);
                ll_auto.setBackgroundColor(Color.TRANSPARENT);
                manual_flt.setVisibility(View.VISIBLE);

            }
        });
        stop_lout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play.setBackgroundColor(Color.GREEN);
            }
        });

        dwn_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//

                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(MapsActivity.this).inflate(R.layout.second_alert_layout, viewGroup, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);

                builder.setView(dialogView);

                lv=dialogView.findViewById(R.id.filelist_view);

                lv.setAdapter(new ListAdapt(getApplication()));





                //  Button dwnld = dialogView.findViewById(R.id.download);
//                final AlertDialog downloadAlertDialog = builder.create();
//                downloadAlertDialog.show();
//                //alertDialog.dismiss();
//
//                dwnld.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(MapsActivity.this, "Downloading", Toast.LENGTH_SHORT).show();
//                        autoMode.setVisibility(View.GONE);
//                        downloadAlertDialog.dismiss();
//
//
//                    }
//
//
//                });


            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigationBarAndStatusBar();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        // Add a marker in Sydney and move the camera
       /* LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
       if(checkPermission())
        mMap.setMyLocationEnabled(true);
        else askPermission();
            


    }

    private void askPermission() {
        ActivityCompat.requestPermissions(
                MapsActivity.this,
                new String[] { Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                MAP_PERMISSION
        );
    }

     private boolean checkPermission() {

        // Ask for permission if it wasn't granted yet
         return (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED )&&(ContextCompat.checkSelfPermission(MapsActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MAP_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("yes", "yes");
                    //onLocationChanged(mLastLocation);

                } else {
                    Log.d("yes", "no");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }

    }

    private void hideNavigationBarAndStatusBar(){


        View overlay = findViewById(R.id.cons_par);

        overlay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN);


    }


    private void showCustomDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.confirm_alert_layout, viewGroup, false);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        Button yes=dialogView.findViewById(R.id.yes_but);
        Button no=dialogView.findViewById(R.id.no_but);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        //Button yes=alertDialog.getButton(R.id.yes_but);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                autoMode.setVisibility(View.INVISIBLE);


            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo close the dialog
                alertDialog.dismiss();
            }
        });

    }


    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
       /* if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }*/
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Log.e("Lat",String.valueOf(location.getLatitude()));
        Log.e("Long",String.valueOf(location.getLongitude()));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(100));


    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
