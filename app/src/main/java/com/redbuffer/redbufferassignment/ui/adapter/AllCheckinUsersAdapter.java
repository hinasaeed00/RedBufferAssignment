package com.redbuffer.redbufferassignment.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.redbuffer.redbufferassignment.R;
import com.redbuffer.redbufferassignment.model.User;
import com.redbuffer.redbufferassignment.ui.fragment.MapFragment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class AllCheckinUsersAdapter extends RecyclerView.Adapter<AllCheckinUserAdapterGridHolder> {

    private LayoutInflater mInflater;
    private Context context;
    private ArrayList<User> userArrayList;
private MapFragment mapFragment;
private ScrollView bottomsheet;
    public AllCheckinUsersAdapter(Context context , ArrayList<User> userArrayList, MapFragment mapFragment, ScrollView bottomsheet) {

        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.userArrayList=userArrayList;
        this.mapFragment=mapFragment;
        this.bottomsheet=bottomsheet;
    }

    @NonNull
    @Override
    public AllCheckinUserAdapterGridHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return initViewHolder(parent);
    }

    @NonNull
    private AllCheckinUserAdapterGridHolder initViewHolder(ViewGroup parent) {
        if (this.mInflater == null) {
            this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        View view = mInflater.inflate(R.layout.allcheckinuserslistlayout, parent, false);
        return new AllCheckinUserAdapterGridHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AllCheckinUserAdapterGridHolder holder, final int position) {
holder.userName.setText(userArrayList.get(position).getUsername());
holder.longitude.setText(""+userArrayList.get(position).getLng());
        holder.latitude.setText(""+userArrayList.get(position).getLat());
        holder.list_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapFragment.showLocationOnMap(userArrayList.get(position));
                BottomSheetBehavior.from(bottomsheet)
                        .setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }
}


class AllCheckinUserAdapterGridHolder extends RecyclerView.ViewHolder {

    //Children List
    TextView userName;
    TextView latitude;
    TextView longitude;
    TableLayout list_item;

    AllCheckinUserAdapterGridHolder(View itemView) {
        super(itemView);
userName=itemView.findViewById(R.id.userName);
latitude=itemView.findViewById(R.id.latitude);
longitude=itemView.findViewById(R.id.longitude);
list_item=itemView.findViewById(R.id.list_item);

    }
}

