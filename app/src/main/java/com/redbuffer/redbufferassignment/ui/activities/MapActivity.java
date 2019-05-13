package com.redbuffer.redbufferassignment.ui.activities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.redbuffer.redbufferassignment.R;
import com.redbuffer.redbufferassignment.communication.ActivityBlockingCallBack;
import com.redbuffer.redbufferassignment.communication.RestClient;
import com.redbuffer.redbufferassignment.listeners.MapListener;
import com.redbuffer.redbufferassignment.model.User;
import com.redbuffer.redbufferassignment.ui.adapter.AllCheckinUsersAdapter;
import com.redbuffer.redbufferassignment.ui.fragment.MapFragment;

import java.util.ArrayList;

import retrofit2.Response;

public class MapActivity extends FragmentActivity {
    Button btn_allcheckedin,btn_allcheckedinonmap;
    MapListener mapListener;
    SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    Gson gson = new Gson();
    ScrollView bottomsheet;
    BottomSheetBehavior sheetBehavior;
    boolean isBottomSheetExpanded=false;
    ImageView expandCollapsedImgView;
    RecyclerView rc_allusers;
    AllCheckinUsersAdapter allCheckinUsersAdapter;
    ArrayList<User> userArrayList=new ArrayList<>();
    TextView allcheckinusers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        bottomsheet=findViewById(R.id.bottom_sheet);
        expandCollapsedImgView=findViewById(R.id.expandcollpase);
        sheetBehavior = BottomSheetBehavior.from(bottomsheet);
        rc_allusers=findViewById(R.id.rc_allusers);
allcheckinusers=findViewById(R.id.allcheckinusers);
       // User user=(User)getIntent().getSerializableExtra("UserInformation");
        final MapFragment mapFragment =new MapFragment();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(MapActivity.this);
        editor = sharedPref.edit();
        String json=sharedPref.getString("userInformation","");
        User userObj = gson.fromJson(json, User.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("userInformation",userObj);
        mapFragment.setArguments(bundle);
        LinearLayoutManager horizontalLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rc_allusers.setLayoutManager(horizontalLayoutManager);

        allcheckinusers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            mapFragment.getAllUserLocation(userArrayList);
            }
        });
        expandCollapsedImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isBottomSheetExpanded)
                {
                    BottomSheetBehavior.from(bottomsheet)
                            .setState(BottomSheetBehavior.STATE_COLLAPSED);
                    isBottomSheetExpanded=false;
                }
                else
                {
                    BottomSheetBehavior.from(bottomsheet)
                            .setState(BottomSheetBehavior.STATE_EXPANDED);
                    isBottomSheetExpanded=true;
                }
            }
        });
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        isBottomSheetExpanded=true;
                        expandCollapsedImgView.setImageResource(R.drawable.ic_arrow_down);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        isBottomSheetExpanded=false;
                        expandCollapsedImgView.setImageResource(R.drawable.ic_arrow_up);
                    }
                    break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        userArrayList=getAllUserList();
        allCheckinUsersAdapter=new AllCheckinUsersAdapter(this,userArrayList,mapFragment,bottomsheet);
        rc_allusers.setAdapter(allCheckinUsersAdapter);
    }
    private ArrayList<User> getAllUserList()
    {
      //  final ArrayList<User> userArrayList=new ArrayList<>();
        RestClient.get().userLocationApi().getAllLocations().enqueue(new ActivityBlockingCallBack<ArrayList<User>>(this,true) {
            @Override
            public void handleSuccess(Response<ArrayList<User>> response) {
                userArrayList.addAll(response.body());
                allCheckinUsersAdapter.notifyDataSetChanged();
            }
        });
        return userArrayList;
    }

}
