package com.redbuffer.redbufferassignment.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.google.android.gms.maps.model.MarkerOptions;
import com.redbuffer.redbufferassignment.R;
import com.redbuffer.redbufferassignment.communication.ActivityBlockingCallBack;
import com.redbuffer.redbufferassignment.communication.CommonResponse;
import com.redbuffer.redbufferassignment.communication.RestClient;
import com.redbuffer.redbufferassignment.listeners.MapListener;
import com.redbuffer.redbufferassignment.model.User;

import java.util.ArrayList;

import retrofit2.Response;


public class MapFragment extends Fragment implements OnMapReadyCallback,MapListener {
    private FusedLocationProviderClient mFusedLocationClient; // Object used to receive location updates
    private LocationRequest locationRequest; // Object that defines important parameters regarding location request.
    private SupportMapFragment mMapFragment; // MapView UI element
    private GoogleMap mGoogleMap; // object that represents googleMap and allows us to use Google Maps API features
    Activity mActivity;
   User userInformation;
   MapListener mapListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setMapListener(MapListener mapListener) {
        this.mapListener = mapListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_map, container, false);
        // Inflate the layout for this fragment
        // SET MAP
        mMapFragment =  (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
        setMapListener(this);
        //LOCATION REQUEST
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        locationRequest = LocationRequest.create();
        userInformation=(User)getArguments().getSerializable("userInformation");

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
                   // LinkedHashMap<String, String> latlong = getNewLocationMessage(location.getLatitude(), location.getLongitude());
                    userInformation.setLat(location.getLatitude());
                    userInformation.setLng(location.getLongitude());
                    LatLng newLocation = new LatLng(Double.valueOf(location.getLatitude()), Double.valueOf(location.getLongitude()));
                    updateLocationOnMap(true,false,false,newLocation);
                    saveUserInformation(userInformation);
                }
            }, Looper.myLooper());

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
    private void updateLocationOnMap(boolean isMyLocation,boolean isAllLocation,boolean isiIndividualLocation,LatLng newLocation)
    {
        if(isiIndividualLocation)
        {
            mGoogleMap.clear();
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    newLocation, 15.5f));
            mGoogleMap.addMarker(new MarkerOptions().position(newLocation).
                    icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_user_marker)));
        }
        else if(isMyLocation)
        {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    newLocation, 15.5f));
            mGoogleMap.addMarker(new MarkerOptions().position(newLocation).
                    icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)));
        }
        else
        {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    newLocation, 8.0f));
              mGoogleMap.addMarker(new MarkerOptions().position(newLocation).
                    icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_user_marker)));
        }

    }


  private void saveUserInformation(User userInformation)
  {
      try
      {
          RestClient.get().userLocationApi().saveUserLocation(userInformation).enqueue(new ActivityBlockingCallBack<CommonResponse>(getActivity(),true) {
              @Override
              public void handleSuccess(Response<CommonResponse> response) {

              }
          });
      }
      catch (Exception ex)
      {

      }
  }


    @Override
    public void getAllUserLocation(ArrayList<User> userArrayList) {
        for (User user:userArrayList) {
            LatLng newLocation = new LatLng(Double.valueOf(user.getLat()), Double.valueOf(user.getLng()));
            updateLocationOnMap(false,true,false,newLocation);
        }

    }

    @Override
    public void showLocationOnMap(User user) {
        LatLng newLocation = new LatLng(Double.valueOf(user.getLat()), Double.valueOf(user.getLng()));
        updateLocationOnMap(false,false,true,newLocation);
    }
}
