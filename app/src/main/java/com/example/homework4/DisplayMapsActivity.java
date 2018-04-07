package com.example.homework4;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class DisplayMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        location = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        setContentView(R.layout.activity_display_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        /*mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        mMap = googleMap;

        // Add a marker and move the camera
        double lat = 0;
        double lon = 0;
        int zoom = 10;
        String marker = "";
        int animate = 0;
        try {
            String lastword = location.substring(location.lastIndexOf(" ")+1);
            if (lastword.startsWith("Zoom:")) {
                int newZoom = Integer.parseInt(lastword.substring(5));
                if (newZoom >= 0 || newZoom <= mMap.getMinZoomLevel())  {
                    zoom = newZoom;
                }
                location = location.substring(0, location.lastIndexOf(" "));
            }
            else if (lastword.startsWith("Animate:")) {
                String animation = lastword.substring(8);
                if (animation.equals("True")) {
                    animate = 1;
                }
                location = location.substring(0, location.lastIndexOf(" "));
            }
        } catch (Exception e) {

        }

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> list = geocoder.getFromLocationName(location, 1);
            if (list != null) {
                if (list.size() > 0) {
                    lat = list.get(0).getLatitude();
                    lon = list.get(0).getLongitude();
                    String locality = list.get(0).getLocality();
                    String state = list.get(0).getAdminArea();
                    String country = list.get(0).getCountryCode();
                    String zip = list.get(0).getPostalCode();
                    if (locality != null) {
                        marker += locality + " ";
                    }
                    if (state != null) {
                        marker += state + " ";
                    }
                    if (country != null) {
                        marker += country + " ";
                    }
                    if (zip != null) {
                        marker += zip;
                    }
                }
            }
            if (lat == 0 && lon == 0) {
                String[] coords = location.split(" ");
                if (coords.length == 2) {
                    lat = Double.parseDouble(coords[0].replaceAll(",",""));
                    lon = Double.parseDouble(coords[1]);
                    List<Address> list2 = geocoder.getFromLocation(lat, lon, 1);
                    if (list2 != null) {
                        if (list.size() > 0) {
                            String locality = list2.get(0).getLocality();
                            String state = list2.get(0).getAdminArea();
                            String country = list2.get(0).getCountryCode();
                            String zip = list2.get(0).getPostalCode();
                            if (locality != null) {
                                marker += locality + " ";
                            }
                            if (state != null) {
                                marker += state + " ";
                            }
                            if (country != null) {
                                marker += country + " ";
                            }
                            if (zip != null) {
                                marker += zip;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        LatLng loc = new LatLng(lat, lon);
        if (marker.equals("")) {
            marker = "Marker";
        }
        mMap.addMarker(new MarkerOptions().position(loc).title(marker));
        if (animate == 1) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(loc)      // Sets the center of the map to Mountain View
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(60)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, zoom));
        }
    }
}
