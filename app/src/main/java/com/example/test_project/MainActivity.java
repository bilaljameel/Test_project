package com.example.test_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test_project.Retrofit.Modal;
import com.example.test_project.Retrofit.MyApi;
import com.example.test_project.Retrofit.Location;
import com.example.test_project.Retrofit.RetrofitMethods;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,LocationListener {
    private GoogleMap mMap;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int LOCATION_SETTINGS_REQUEST = 9001;
    protected LocationManager locationManager;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    public static List<Modal> data;
    public static Location result;

    ImageView currentLocationbtn;
    ProgressDialog progressDialog;
    Dialog dialog ;

    //Classes
    Methods methods = new Methods();
    RetrofitMethods retrofitMethods = new RetrofitMethods();

    public static long timestamp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        dialog = new Dialog(MainActivity.this);
        currentLocationbtn = findViewById(R.id.current_location);
        progressDialog =new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");

        currentLocationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enableGPSLocation();
                getDeviceLocation();

            }
        });


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }else {

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            getDeviceLocation();
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            mMap = googleMap;



            try {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {


                } else {
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    mMap.setMyLocationEnabled(true);
                    retrofitMethods.getAllCourses(data,result,mMap,MainActivity.this);
                }
            } catch (SecurityException e) {
                Log.e("Exception: %s", e.getMessage());
            }
        }


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {


                dialog.setContentView(R.layout.markdialouge);
                final EditText title = dialog.findViewById(R.id.title);
                Button datepick= dialog.findViewById(R.id.datebtn);
                Button mark = dialog.findViewById(R.id.mark);


                datepick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Methods methods = new Methods();
                        methods.datePicker(MainActivity.this);
                    }
                });

                mark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        progressDialog.show();

                        if (title.getText().toString().equals("")){

                            Toast.makeText(MainActivity.this, "Please fill Title", Toast.LENGTH_SHORT).show();

                        }else{


                            Location location= new Location(latLng.latitude,latLng.longitude);
                            Modal modal = new Modal(data.size()+1,title.getText().toString(),location, timestamp);
                            data.add(modal);

                            String stTitle = title.getText().toString();
                            retrofitMethods.add(MainActivity.this,modal,stTitle,timestamp,data.size(),progressDialog);


                            int i = data.size();

                            mMap.addMarker(new MarkerOptions().position(
                                    new LatLng(latLng.latitude,latLng.longitude))
                                    .title(title.getText().toString())
                                    .snippet(String.valueOf(i))
                            );

                            if (dialog.isShowing()){
                                dialog.dismiss();
                            }
                        }
                    }
                });

                dialog.show();


            }
        });


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {



                dialog.setContentView(R.layout.infodialoug);
                TextView title =dialog.findViewById(R.id.title);
                TextView time =dialog.findViewById(R.id.timestamp);
                Button delete = dialog.findViewById(R.id.btn);

                final int position = Integer.parseInt(marker.getSnippet())-1;

                title.setText(data.get(position).getTitle());
                time.setText(methods.getTimestampToTime(data.get(position).getTimestamp()));


                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        retrofitMethods.delete(MainActivity.this,position);

                    }
                });

                dialog.show();
                return false;
            }
        });
    }


    public void getDeviceLocation() {
        try {


            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }else {


                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

                Task<android.location.Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(new OnCompleteListener<android.location.Location>() {
                    @Override
                    public void onComplete(@NonNull Task<android.location.Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            android.location.Location location = task.getResult();
                            if (location != null){

                                LatLng currentLatLng = new LatLng(location.getLatitude(),
                                        location.getLongitude());
                                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(currentLatLng,
                                        15);
                                mMap.animateCamera(update);
                            }else{
                                getDeviceLocation();
                            }
                        }
                    }
                });

            }


//            }

        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    mMap.setMyLocationEnabled(true);
                    enableGPSLocation();
                    getDeviceLocation();
                    retrofitMethods.getAllCourses(data,result,mMap,MainActivity.this);
                } else {
                    finish();
                }
                return;
            }
        }
    }



    @Override
    public void onLocationChanged(@NonNull android.location.Location location) {

        getDeviceLocation();
    }


    @Override
    public void onProviderEnabled(@NonNull String provider) {

        getDeviceLocation();
        retrofitMethods.getAllCourses(data,result,mMap,MainActivity.this);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }


    private void enableGPSLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }else {

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        Task<LocationSettingsResponse> result =
                LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {


            @Override
            public void onComplete(Task<LocationSettingsResponse> task) {
                try {
                    task.getResult(ApiException.class);
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                resolvable.startResolutionForResult(
                                        MainActivity.this,
                                        LOCATION_SETTINGS_REQUEST);
                            } catch (IntentSender.SendIntentException e) {
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            }
        });

    }
}
