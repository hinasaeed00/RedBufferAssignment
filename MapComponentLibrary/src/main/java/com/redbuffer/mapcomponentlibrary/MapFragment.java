package com.redbuffer.mapcomponentlibrary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    private FusedLocationProviderClient mFusedLocationClient; // Object used to receive location updates
    private LocationRequest locationRequest; // Object that defines important parameters regarding location request.
    private SupportMapFragment mMapFragment; // MapView UI element
    private GoogleMap mGoogleMap; // object that represents googleMap and allows us to use Google Maps API features
    Activity mActivity;
    LinkedHashMap<String, String> userInformation;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_map, container, false);
        // Inflate the layout for this fragment
        // SET MAP
        mMapFragment =  (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
        //LOCATION REQUEST
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        locationRequest = LocationRequest.create();
        String user_information=getArguments().getSerializable("userInformation").toString();
        Gson gson = new Gson();
        Type entityType = new TypeToken<LinkedHashMap<String, String>>(){}.getType();
        userInformation = gson.fromJson(user_information, entityType);
        sendUpdatedLocationMessage();
        return view;
    }
    @Override
    public void onPause() {
        super.onPause();
        mMapFragment.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapFragment.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapFragment.onLowMemory();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity=(Activity)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mGoogleMap = googleMap;
            mGoogleMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void sendUpdatedLocationMessage() {

        try {
            mFusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();
                    LinkedHashMap<String, String> latlong = getNewLocationMessage(location.getLatitude(), location.getLongitude());
                    userInformation.put("latitude",location.getLatitude());
                }
            }, Looper.myLooper());

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /*
     Helper method that takes in latitude and longitude as parameter and returns a LinkedHashMap representing this data.
     This LinkedHashMap will be the message published by driver.
  */
    private LinkedHashMap<String, String> getNewLocationMessage(double lat, double lng) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("lat", String.valueOf(lat));
        map.put("lng", String.valueOf(lng));
        return map;
    }


}
