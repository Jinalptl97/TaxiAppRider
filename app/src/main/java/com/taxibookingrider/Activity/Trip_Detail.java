package com.taxibookingrider.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.taxibookingrider.Controller.Config;
import com.taxibookingrider.Controller.GraduallyTextView;
import com.taxibookingrider.Controller.Progress_dialog;
import com.taxibookingrider.Controller.Tahoma;
import com.taxibookingrider.Controller.TahomaBold;
import com.taxibookingrider.MyApplication;
import com.taxibookingrider.R;
import com.taxibookingrider.Utils.Pref_Master;


import java.util.ArrayList;
import java.util.List;


public class Trip_Detail extends AppCompatActivity {

    Tahoma date, time, from, to, fares, charges;
    Tahoma finalfare;
    SupportMapFragment mapFragment;
    RequestQueue queue;
    CameraUpdate cu;
    Pref_Master pref_master;
    RelativeLayout maplayout;
    Context context = this;
   // Progress_dialog progress_dialog;
    String dates, times, froms, tos, fare, wcharges, finalfares, flat, flng, tlat, tlng,tripno;
    ArrayList<LatLng> points;
    List<Marker> markersList = new ArrayList<Marker>();
    Marker marker1, marker2;
    LatLngBounds.Builder builder;
    ImageView notify_bar;
    android.support.v7.app.AlertDialog progress_dialog;
    GraduallyTextView txt_load;
    ImageView img_gif;
    Tahoma trip_no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_trip__detail);


        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        notify_bar = findViewById(R.id.notify_bar);
        fares = findViewById(R.id.fares);
        charges = findViewById(R.id.charges);
        finalfare = findViewById(R.id.finalfare);
        maplayout = findViewById(R.id.maplayout);
        trip_no=findViewById(R.id.trip_no);
        //progress_dialog=new Progress_dialog();
        queue = Volley.newRequestQueue(this);
        pref_master = new Pref_Master(getApplicationContext());
        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.progress_dialog_layout, null);
        progress_dialog = new android.support.v7.app.AlertDialog.Builder(context, R.style.cart_dialog).setView(v).show();
        progress_dialog.setCancelable(false);
        progress_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        txt_load = (GraduallyTextView) v.findViewById(R.id.txt_load);
        img_gif = (ImageView) v.findViewById(R.id.img_gif);
        txt_load.startLoading();

        Glide.with(context)
                .load(R.drawable.gifimage111)
                .asGif()
                .placeholder(R.drawable.gifimage111)
                .crossFade()
                .into(img_gif);
        progress_dialog.show();





        notify_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        dates = intent.getStringExtra("date");
        times = intent.getStringExtra("time");
        froms = intent.getStringExtra("from");
        tos = intent.getStringExtra("to");
        fare = intent.getStringExtra("fare");
        wcharges = intent.getStringExtra("wcharge");
        finalfares = intent.getStringExtra("finalfare");
        flat = intent.getStringExtra("flat");
        flng = intent.getStringExtra("flng");
        tlat = intent.getStringExtra("tlat");
        tlng = intent.getStringExtra("tlng");
        tripno=intent.getStringExtra("trip_no");


        final PolylineOptions polylineOptions = new PolylineOptions();

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {

                points = new ArrayList<>();
                points.add(new LatLng(Double.parseDouble(flat), Double.parseDouble(flng)));
                points.add(new LatLng(Double.parseDouble(tlat), Double.parseDouble(tlng)));

                marker1 = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(flat), Double.parseDouble(flng)))
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.currentmarker)));

                marker2 = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(tlat), Double.parseDouble(tlng)))
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.des_marker)));


                googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                context, R.raw.map_style));

                markersList.add(marker1);
                markersList.add(marker2);


                builder = new LatLngBounds.Builder();
                for (Marker m : markersList) {
                    builder.include(m.getPosition());
                }
                int padding = 145;
                LatLngBounds bounds = builder.build();
                cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                maplayout.setVisibility(View.VISIBLE);
                googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {

                        progress_dialog.dismiss();
                        googleMap.moveCamera(cu);
                    }
                });


                polylineOptions.addAll(points);
                polylineOptions.width(3);
                polylineOptions.color(Color.BLACK);
                googleMap.addPolyline(polylineOptions);


            }
        });


        date.setText(dates);
        time.setText(times);
        from.setText(froms);
        to.setText(tos);
        fares.setText(Config.currency + fare);
        charges.setText(Config.currency + wcharges);
        finalfare.setText(Config.currency + finalfares);
        trip_no.setText(tripno);

        MyApplication.getInstance().trackScreenView("Trip_Detail");


    }


}
