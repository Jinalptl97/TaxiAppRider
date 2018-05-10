package com.taxibookingrider.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.taxibookingrider.Activity.DriverListDemo;
import com.taxibookingrider.Adapter.DriverInfoAdapter;
import com.taxibookingrider.Adapter.DriverInfoAdapter2;
import com.taxibookingrider.Adapter.DriverInfoAdapter3;
import com.taxibookingrider.Controller.DialogBox;
import com.taxibookingrider.Controller.Progress_dialog;
import com.taxibookingrider.Controller.Tahoma;
import com.taxibookingrider.Model.DriverInfo;
import com.taxibookingrider.R;

import java.util.ArrayList;

/**
 * Created by Ankita on 3/31/2018.
 */

public class ShortByRating extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {

    private static String rej,cartype, canceldriver, tripid;
    Context context;
    RecyclerView list2;
    GoogleApiClient mGoogleApiClient;
    Location location1;
    Double driverlat, driverlng;
    public static String driverid;
    int radius = 2;
    DriverInfoAdapter driverInfoAdapter;
    ArrayList<DriverInfo> driverInfolist = new ArrayList<>();

    String key = "";
    Progress_dialog progress_dialog;
    ArrayList<String> rejecteddriver = new ArrayList<String>();
    RequestQueue queue;
    RelativeLayout appdrawer, submit;
    DriverInfoAdapter2 driverInfoAdapter2;

    public ShortByRating() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fregment_short_by_rating, container, false);
        context = getActivity();

        list2=(RecyclerView)view.findViewById(R.id.list2);

        appdrawer = (RelativeLayout)view.findViewById(R.id.appdrawerdriverlist);

        queue = Volley.newRequestQueue(context);
        progress_dialog=new Progress_dialog();

        /*Bundle b=getArguments();
        rej=b.getString("rej","1");
        cartype=b.getString("cartype","");*/

        rej = ((DriverListDemo)getActivity()).getIntent().getExtras().getString("SIDE","1");
        cartype = ((DriverListDemo)getActivity()).getIntent().getExtras().getString("cartype","");
        canceldriver = ((DriverListDemo)getActivity()).getIntent().getExtras().getString("canceldriver","");
        tripid = ((DriverListDemo)getActivity()).getIntent().getExtras().getString("tripid","");




        LinearLayoutManager layoutManager2 = new LinearLayoutManager(context);
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        list2.setLayoutManager(layoutManager2);

        driverInfoAdapter2 = new DriverInfoAdapter2(context, driverInfolist);
        list2.setAdapter(driverInfoAdapter2);


        if (mGoogleApiClient == null) {
            Log.e("the Inside","::::::::::::::");
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

            DialogBox.setNoData(context, getString(R.string.location_enable), getRunnable);
        } else {

        }




        return view;

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
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        final Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        location1 = location;
        Log.e("Connected", "Connected");

        if (rej.equals("1")) {

            if (location != null) {
                progress_dialog.show(getFragmentManager(),"");

                driverInfolist.clear();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("cars").child(cartype).child("driverAvailable");

                GeoFire geoFire = new GeoFire(reference);
                final GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(location.getLatitude(), location.getLongitude()), radius);
                geoQuery.removeAllListeners();
                geoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
                    @Override
                    public void onDataEntered(final DataSnapshot childshot, final GeoLocation location) {


                        key = childshot.getKey();


                        FirebaseDatabase.getInstance().getReference("cars").child("driverDetail").child(childshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {





                                driverInfoAdapter2 = new DriverInfoAdapter2(context, driverInfolist);
                                list2.setAdapter(driverInfoAdapter2);

                                Log.e("IDsss", childshot.getKey());
                                ArrayList<String> Ids = new ArrayList<String>();
                                Ids.add(childshot.getKey());
                                Log.e("RejectedSize", "" + rejecteddriver.size());
                                Log.e("IDSize", "" + rejecteddriver.size());
                                if (canceldriver.equals(childshot.getKey())) {
                                    Log.e("DriverRejected", "DriverRejected");
                                } else {
                                    if (dataSnapshot.child("driverstatus").getValue().equals("1")) {
                                        Log.e("INside", "Inside");
                                        DriverInfo driverInfo = new DriverInfo((Float.parseFloat(String.valueOf(dataSnapshot.child("Fare").getValue()))));
                                        driverInfo.setDriverid("" + dataSnapshot.getKey());
                                        driverid = dataSnapshot.getKey();
                                        driverInfo.setCar("" + dataSnapshot.child("car").getValue());
                                        driverInfo.setCarModel("" + (dataSnapshot.child("carModel").getValue()));
                                        driverInfo.setFname("" + (dataSnapshot.child("fname").getValue()));
                                        driverInfo.setLname("" + (dataSnapshot.child("lname").getValue()));
                                        driverInfo.setDevice_token("" + (dataSnapshot.child("device_token").getValue()));
                                        driverInfo.setSelected("0");
                                        driverInfo.setColor(String.valueOf(R.color.White));
                                        driverInfo.setDriverProfilepic("" + dataSnapshot.child("profile").getValue());
                                        driverInfo.setCar_number("" + dataSnapshot.child("car_number").getValue());
                                        driverlat = Double.parseDouble(String.valueOf(dataSnapshot.child("lat").getValue()));
                                        driverlng = Double.parseDouble(String.valueOf(dataSnapshot.child("lng").getValue()));
                                        Location driverlocation = new Location("");
                                        driverlocation.setLatitude(driverlat);
                                        driverlocation.setLongitude(driverlng);
                                        Float distance = location1.distanceTo(driverlocation);
                                        driverInfo.setDistance(distance / 1000);
                                        driverInfo.setRating(Float.valueOf("" + dataSnapshot.child("driverrating").getValue()));
                                        driverInfolist.add(driverInfo);
                                        Log.e("Distance", "" + distance / 1000 + "  " + driverid);
                                    } else if (dataSnapshot.child("driverstatus").getValue().equals("0")) {
                                        Log.e("Remove", "Remove");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                        driverInfoAdapter2.notifyDataSetChanged();


                        // FirebaseDatabase.getInstance().getReference("cars").child("driverDetail").child(childshot.getKey()).removeEventListener(DriverListValueevent);

                    }

                    @Override
                    public void onDataExited(DataSnapshot dataSnapshot) {

                        for (int i = 0; i < driverInfolist.size(); i++) {
                            if (dataSnapshot.getKey().equals(driverInfolist.get(i).getDriverid())) {

                                driverInfolist.remove(driverInfolist.get(i));

                                driverInfoAdapter2.notifyDataSetChanged();


                            }
                        }

                        geoQuery.removeAllListeners();

                    }

                    @Override
                    public void onDataMoved(DataSnapshot dataSnapshot, GeoLocation location) {

                    }

                    @Override
                    public void onDataChanged(DataSnapshot dataSnapshot, GeoLocation location) {

                    }

                    @Override
                    public void onGeoQueryReady() {


                        progress_dialog.dismiss();
                        Log.e("Done", "Done");

//                    FirebaseDatabase.getInstance().getReference("cars").child("driverDetail").child(key).removeEventListener(DriverListValueevent);
                    }

                    @Override
                    public void onGeoQueryError(DatabaseError error) {

                    }
                });
            }

        } else {

            if (location != null) {
                driverInfolist.clear();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("cars").child(cartype).child("driverAvailable");

                GeoFire geoFire = new GeoFire(reference);
                final GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(location.getLatitude(), location.getLongitude()), radius);
                geoQuery.removeAllListeners();
                geoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
                    @Override
                    public void onDataEntered(final DataSnapshot childshot, final GeoLocation location) {


                        key = childshot.getKey();


                        FirebaseDatabase.getInstance().getReference("cars").child("driverDetail").child(childshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {




                                driverInfoAdapter2 = new DriverInfoAdapter2(context, driverInfolist);
                                list2.setAdapter(driverInfoAdapter2);

                                Log.e("IDsss", childshot.getKey());
                                ArrayList<String> Ids = new ArrayList<String>();
                                Ids.add(childshot.getKey());
                                Log.e("RejectedSize", "" + rejecteddriver.size());
                                Log.e("IDSize", "" + rejecteddriver.size());
                                if (canceldriver.equals(childshot.getKey())) {
                                    Log.e("DriverRejected", "DriverRejected");
                                } else {
                                    if (dataSnapshot.child("driverstatus").getValue().equals("1")) {
                                        Log.e("INside", "Inside");
                                        DriverInfo driverInfo = new DriverInfo((Float.parseFloat(String.valueOf(dataSnapshot.child("Fare").getValue()))));
                                        driverInfo.setDriverid("" + dataSnapshot.getKey());
                                        driverid = dataSnapshot.getKey();
                                        driverInfo.setCar("" + dataSnapshot.child("car").getValue());
                                        driverInfo.setCarModel("" + (dataSnapshot.child("carModel").getValue()));
                                        driverInfo.setFname("" + (dataSnapshot.child("fname").getValue()));
                                        driverInfo.setLname("" + (dataSnapshot.child("lname").getValue()));
                                        driverInfo.setDevice_token("" + (dataSnapshot.child("device_token").getValue()));
                                        driverInfo.setSelected("0");
                                        driverInfo.setColor(String.valueOf(R.color.White));
                                        driverInfo.setDriverProfilepic("" + dataSnapshot.child("profile").getValue());
                                        driverInfo.setCar_number("" + dataSnapshot.child("car_number").getValue());
                                        driverlat = Double.parseDouble(String.valueOf(dataSnapshot.child("lat").getValue()));
                                        driverlng = Double.parseDouble(String.valueOf(dataSnapshot.child("lng").getValue()));
                                        Location driverlocation = new Location("");
                                        driverlocation.setLatitude(driverlat);
                                        driverlocation.setLongitude(driverlng);
                                        Float distance = location1.distanceTo(driverlocation);
                                        driverInfo.setDistance(distance / 1000);
                                        driverInfo.setRating(Float.valueOf("" + dataSnapshot.child("driverrating").getValue()));
                                        driverInfolist.add(driverInfo);
                                        Log.e("Distance", "" + distance / 1000 + "  " + driverid);
                                    } else if (dataSnapshot.child("driverstatus").getValue().equals("0")) {
                                        Log.e("Remove", "Remove");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                        driverInfoAdapter2.notifyDataSetChanged();


                        // FirebaseDatabase.getInstance().getReference("cars").child("driverDetail").child(childshot.getKey()).removeEventListener(DriverListValueevent);

                    }

                    @Override
                    public void onDataExited(DataSnapshot dataSnapshot) {

                        for (int i = 0; i < driverInfolist.size(); i++) {
                            if (dataSnapshot.getKey().equals(driverInfolist.get(i).getDriverid())) {

                                driverInfolist.remove(driverInfolist.get(i));
                                driverInfoAdapter2.notifyDataSetChanged();


                            }
                        }

                        geoQuery.removeAllListeners();

                    }

                    @Override
                    public void onDataMoved(DataSnapshot dataSnapshot, GeoLocation location) {

                    }

                    @Override
                    public void onDataChanged(DataSnapshot dataSnapshot, GeoLocation location) {

                    }

                    @Override
                    public void onGeoQueryReady() {


                        Log.e("Done", "Done");

//                    FirebaseDatabase.getInstance().getReference("cars").child("driverDetail").child(key).removeEventListener(DriverListValueevent);
                    }

                    @Override
                    public void onGeoQueryError(DatabaseError error) {

                    }
                });
            }

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    Runnable getRunnable = new Runnable() {
        @Override
        public void run() {

            Intent callGPSSettingIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(callGPSSettingIntent, 123);

        }
    };
}
