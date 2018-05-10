package com.taxibookingrider.Fragment;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.taxibookingrider.Activity.Current_tripDemo;
import com.taxibookingrider.Activity.Reason_Current_trip;
import com.taxibookingrider.Controller.Config;
import com.taxibookingrider.Controller.DialogBox;
import com.taxibookingrider.Controller.GraduallyTextView;
import com.taxibookingrider.Controller.Progress_dialog;
import com.taxibookingrider.Controller.Tahoma;
import com.taxibookingrider.MyApplication;
import com.taxibookingrider.R;
import com.taxibookingrider.Utils.Pref_Master;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ankita on 4/2/2018.
 */

public class Fare extends Fragment implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

    FragmentManager manager;
    private NotificationManager notificationManager;
    //Progress_dialog progress_dialog;

    RelativeLayout rr_cancel_ride;
    GoogleApiClient mGoogleApiClient;
    Context context;
    RequestQueue queue;
    Location location, locationss;
    Pref_Master pref_master;
    String tripid, riderid, fare, flag, km, eta, coupon, cprate, total, flong, flat, rname, cncharge, flagval, driverid, tlat, tlong, ctype, stdon, endon;
    Marker mk = null;
    float bearing = 0;
    int markercount = 0;
    GoogleMap map;
    Tahoma couponcode, finalfare, couponcodeapplied, totalfare,kms,teta;
    Bitmap icon;
    LatLng latlngOne;
    SupportMapFragment mapFragment;
    android.support.v7.app.AlertDialog progress_dialog;
    GraduallyTextView txt_load;
    ImageView img_gif;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fregment_fare, container, false);
       context= getActivity();
      // progress_dialog=new Progress_dialog();
        manager = getFragmentManager();
        queue = Volley.newRequestQueue(context);
         mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapp));
        /*couponcode = view.findViewById(R.id.couponcode);*/
        totalfare = view.findViewById(R.id.totalfare);
        kms = view.findViewById(R.id.kms);
        teta = view.findViewById(R.id.eta);
        finalfare = view.findViewById(R.id.finalfare);
        couponcodeapplied = view.findViewById(R.id.couponcodeapplied);
        totalfare = view.findViewById(R.id.totalfare);

        pref_master = new Pref_Master(context);
        icon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.app_icon_rider);
        rr_cancel_ride = (RelativeLayout) view.findViewById(R.id.rr_cancel_ride);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }


        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            DialogBox.setNoData(context, getResources().getString(R.string.location_enable), runnable);
        } else {

        }

        rr_cancel_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext().getApplicationContext(), Reason_Current_trip.class);
                i.putExtra("tripid", tripid);
                startActivity(i);
            }
        });



        return view;
    }

    private void makeJsonObjReq() {

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
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("tripid", pref_master.getTripid());
        postParam.put("riderid", pref_master.getUID());
        Log.e("parameter", " " + new JSONObject(postParam));


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Config.tripinfo, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("ReposnseGetTripINfo", response.toString());
                        progress_dialog.dismiss();
                        try {
                            Log.e("Status", response.getString("status"));

                            if (response.getString("status").equals("200")) {
                                JSONArray jsonArray = new JSONArray(response.getString("data"));

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jobj1 = jsonArray.getJSONObject(i);

                                    tripid = jobj1.getString("tripid");
                                    riderid = jobj1.getString("riderid");
                                    fare = jobj1.getString("total");
                                    flag = jobj1.getString("flag");
                                    flagval = jobj1.getString("flagval");
                                    km = jobj1.getString("km");
                                    eta = jobj1.getString("eta");
                                    coupon = jobj1.getString("coupon");
                                    cprate = jobj1.getString("cprate");
                                    total = jobj1.getString("finaltotal");
                                    flat = jobj1.getString("flat");
                                    flong = jobj1.getString("flong");
                                    rname = jobj1.getString("rname");
                                    cncharge = jobj1.getString("cncharge");
                                    driverid = jobj1.getString("driverid");
                                    tlat = jobj1.getString("tlat");
                                    tlong = jobj1.getString("tlong");
                                    ctype = jobj1.getString("ctype");


                                    /*fromaddress.setText(pref_master.getFromaddress());
                                    toaddress.setText(pref_master.getToaddress());*/
                                    /*couponcode.setText(coupon);*/
                                    finalfare.setText(Config.currency + total);
                                    couponcodeapplied.setText(Config.currency + cprate);
                                    totalfare.setText(Config.currency + fare);
                                    kms.setText(km + " Kms");
                                    teta.setText(eta + " mins");
                                    pref_master.setStr_tripId(tripid);
                                    pref_master.setStr_tripId(jobj1.getString("tripid"));
                                    pref_master.setStr_driverId(jobj1.getString("driverid"));
                                    Log.e("the tripId","::::"+pref_master.getStr_tripId());
                                    Log.e("the driverId","::::"+pref_master.getStr_driverId());
                                    live();
                                  /*  getMessage();*/
                                    live(location);
                                }


                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progress_dialog.dismiss();
                VolleyLog.e("Error", "Error: " + error.getMessage());

            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Config.getHeaderParam(context);
            }


        };
        jsonObjReq.setTag("POST");
        // Adding request to request queue
        queue.add(jsonObjReq);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat #requestPermissions for more details.
            return;
        }

        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location != null) {
            makeJsonObjReq();



            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    googleMap.setMyLocationEnabled(true);


                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17);
                    googleMap.animateCamera(cameraUpdate);


                }
            });
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            Intent callGPSSettingIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(callGPSSettingIntent, 123);
        }
    };

    private void live() {


        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;

                FirebaseDatabase.getInstance().getReference("cars").child("driverDetail").child(driverid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Double lat = Double.parseDouble("" + dataSnapshot.child("lat").getValue());
                        Double log = Double.parseDouble("" + dataSnapshot.child("lng").getValue());
                        bearing = Float.parseFloat(" " + dataSnapshot.child("bearing").getValue());


                        String myParentNode = driverid;

                        locationss = new Location("");
                        locationss.setLatitude(lat);
                        locationss.setLongitude(log);
                        latlngOne = new LatLng(lat, log);


                        if (markercount == 0) {
                            mk = map.addMarker(new MarkerOptions()
                                    .position(new LatLng(lat, log))
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_pin)));
                            map.setMapStyle(
                                    MapStyleOptions.loadRawResourceStyle(
                                            context, R.raw.map_style));

                            markercount = 1;
                        } else if (markercount == 1) {
                            animateMarker(locationss, mk, bearing);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });

    }


    public static void animateMarker(final Location destination, final Marker marker, final float Bearing) {
        if (marker != null) {
            final LatLng startPosition = marker.getPosition();
            final LatLng endPosition = new LatLng(destination.getLatitude(), destination.getLongitude());

            final float startRotation = marker.getRotation();

            final LatLngInterpolator latLngInterpolator = new LatLngInterpolator.LinearFixed();
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(1000); // duration 1 second
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        float v = animation.getAnimatedFraction();
                        LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                        marker.setPosition(newPosition);
                        marker.setRotation(computeRotation(v, startRotation, Bearing));
                        marker.setFlat(true);
                    } catch (Exception ex) {
                        // I don't care atm..
                    }
                }
            });

            valueAnimator.start();
        }
    }


    private interface LatLngInterpolator {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class LinearFixed implements Fare.LatLngInterpolator {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;
                // Take the shortest path across the 180th meridian.
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }
    }

    private static float computeRotation(float fraction, float start, float end) {
        float normalizeEnd = end - start; // rotate start to 0
        float normalizedEndAbs = (normalizeEnd + 360) % 360;

        float direction = (normalizedEndAbs > 180) ? -1 : 1; // -1 = anticlockwise, 1 = clockwise
        float rotation;
        if (direction > 0) {
            rotation = normalizedEndAbs;
        } else {
            rotation = normalizedEndAbs - 360;
        }

        float result = fraction * rotation + start;
        return (result + 360) % 360;
    }


    public void live(Location location) {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("cars").child(ctype).child("driverAvailable");

        GeoFire geoFire = new GeoFire(reference);
        final GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(location.getLatitude(), location.getLongitude()), 0.050);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
            @Override
            public void onDataEntered(DataSnapshot dataSnapshot, GeoLocation location) {

                if (driverid.equals(dataSnapshot.getKey())) {

                    FirebaseDatabase.getInstance().getReference("cars").child("driverDetail").child(driverid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("driverstatus").getValue().equals("2")) {
                                Current_tripDemo.addNotification();
                            } else if (dataSnapshot.child("driverstatus").getValue().equals("1")){
                                geoQuery.removeAllListeners();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            }

            @Override
            public void onDataExited(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onDataMoved(DataSnapshot dataSnapshot, GeoLocation location) {

            }

            @Override
            public void onDataChanged(DataSnapshot dataSnapshot, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });


    }

    /*private void addNotification() {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.logorider);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.logorider)
                        .setLargeIcon(Bitmap.createBitmap(icon))
                        .setContentTitle("Joy Rider")
                        .setSound(defaultSoundUri)
                        .setContentText("Driver Arrived");

        Intent notificationIntent = new Intent(context,);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public void onResume() {
        super.onResume();

        MyApplication.getInstance().trackScreenView("Fare");
    }
}
