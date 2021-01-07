package com.example.localizben;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    Button btnScan;
    Button btnLocation;
    TextView text_coordinate;
    TextView text_address;
    FusedLocationProviderClient mFusedLocationClient;
    String phoneNumber = "0666562621";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScan = (Button) findViewById(R.id.btnScan);
        //btnLocation = (Button) findViewById(R.id.btnLocation);
        // text_coordinate = (TextView) findViewById(R.id.text_coordinate);
        text_address = (TextView) findViewById(R.id.text_address);

        getLastLocation();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        /*btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_LOCATION_PERMISSION
                    );
                } else {
                    getLocation();
                }
            }
        });*/
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            // If the permission is granted, get the location,
            // else, show a Toast
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this,
                        "error",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getLocation(Location location) {
        String strAddress = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i ++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                    strAddress = strReturnedAddress.toString();
                    text_address.setText(String.format(
                            "Address: %s",
                            strAddress
                    ));
                }
            } else {
                text_address.setText("No address found");
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void getLastLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        getLocation(location);
                    }
                }
            });
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, REQUEST_CODE_LOCATION_PERMISSION);
            }
        }
    }

    /*private void getCurrentLocation() {
        // request
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    // response
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            double latitude =
                                    locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            double longitude =
                                    locationResult.getLocations().get(latestLocationIndex).getLongitude();

                            String strAddress = "";
                            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                if (addresses != null) {
                                    Address returnedAddress = addresses.get(0);
                                    StringBuilder strReturnedAddress = new StringBuilder("");

                                    for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i ++) {
                                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                                        strAddress = strReturnedAddress.toString();
                                        text_address.setText(String.format(
                                                "Address: %s",
                                                strAddress
                                        ));
                                    }
                                    // SmsManager.getDefault().sendTextMessage(phoneNumber, null, strAddress, null, null);
                                } else {
                                    text_address.setText("No address found");
                                }
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                            text_coordinate.setText(
                                    String.format(
                                            "Latidude: %s\nLongitude: %s",
                                            latitude,
                                            longitude
                                    )
                            );
                        }
                    }
                }, Looper.getMainLooper());
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                Log.e("Scan", "Scan has been cancelled");
            } else {
                Log.e("Scan", "Sucessfully scanned");

                Toast.makeText(this, result.getContents() + text_address.getText(), Toast.LENGTH_SHORT).show();
                // SmsManager.getDefault().sendTextMessage(phoneNumber, null, result.getContents() + text_address.getText(), null, null);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}