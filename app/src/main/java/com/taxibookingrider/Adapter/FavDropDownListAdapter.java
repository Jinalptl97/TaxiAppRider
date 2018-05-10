package com.taxibookingrider.Adapter;

import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.taxibookingrider.Activity.Dashboard;
import com.taxibookingrider.Controller.Tahoma;
import com.taxibookingrider.Model.FavLocation;
import com.taxibookingrider.R;
import com.taxibookingrider.Utils.Pref_Master;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 07-Feb-18.
 */

public class FavDropDownListAdapter extends RecyclerView.Adapter<FavDropDownListAdapter.Viewholder> {
    Context context;
    ArrayList<FavLocation> array_list = new ArrayList<>();
    Pref_Master pref_master;
    View view;

    public FavDropDownListAdapter(Context context, ArrayList<FavLocation> array_list) {
        this.context = context;
        this.array_list = array_list;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_destination_list, parent, false);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        return new Viewholder(itemView);
    }

    @Override
    public void onBindViewHolder(FavDropDownListAdapter.Viewholder holder, final int position) {

        holder.address.setText(array_list.get(position).getAddress());

        pref_master = new Pref_Master(context);


        holder.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Address", "Address");

                getLocationFromAddress(array_list.get(position).getAddress());
                pref_master.setToaddress(array_list.get(position).getAddress());


            }
        });

    }

    @Override
    public int getItemCount() {
        return array_list.size();

    }

    public class Viewholder extends RecyclerView.ViewHolder {

        Tahoma address;

        public Viewholder(View view) {
            super(view);

            address = (Tahoma) view.findViewById(R.id.address);

        }
    }


    public void getLocationFromAddress(String strAddress) {
        //Create coder with Activity context - this
        Geocoder coder = new Geocoder(context);
        List<android.location.Address> address;

        try {
            //Get latLng from String
            address = coder.getFromLocationName(strAddress, 5);

            //check for null
            if (address == null) {
                return;
            }

            //Lets take first possibility from the all possibilities.
            android.location.Address location = address.get(0);
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            Log.e("LatlNg", "" + latLng);

            pref_master.setTolocationtrip(latLng.latitude + "," + latLng.longitude);


            Intent i = new Intent(context, Dashboard.class);
            i.putExtra("lat", latLng.latitude);
            i.putExtra("lng", latLng.longitude);
            context.startActivity(i);


            //Put marker on map on that LatLng


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}