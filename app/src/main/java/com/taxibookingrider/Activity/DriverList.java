package com.taxibookingrider.Activity;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.taxibookingrider.Adapter.DriverInfoAdapter;
import com.taxibookingrider.Adapter.DriverInfoAdapter2;
import com.taxibookingrider.Adapter.DriverInfoAdapter3;
import com.taxibookingrider.Controller.Config;
import com.taxibookingrider.Controller.DialogBox;
import com.taxibookingrider.Controller.GraduallyTextView;
import com.taxibookingrider.Controller.Progress_dialog;
import com.taxibookingrider.Controller.Tahoma;
import com.taxibookingrider.Controller.TahomaItalic;
import com.taxibookingrider.Model.DriverInfo;
import com.taxibookingrider.MyApplication;
import com.taxibookingrider.R;
import com.taxibookingrider.Utils.Pref_Master;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class DriverList extends AppCompatActivity implements NumberPicker.OnValueChangeListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    RelativeLayout appdrawer, submit;
    Pref_Master pref_master;
    private String[] paymentMethods;
    RelativeLayout payby;
    public static String driverid;
    RelativeLayout driverlist;
    RequestQueue queue;
    String key = "";
    Tahoma paytype;
    Context context = this;
    public static int position;
    Intent intent;
    Location location1;
    Double driverlat, driverlng;
    String paymentMethod = "";
    String CurrentDate;
  // Progress_dialog progress_dialog;
    public static DriverInfoAdapter driverInfoAdapter;
    android.support.v7.app.AlertDialog alert;
    public static DriverInfoAdapter2 driverInfoAdapter2;
    public static DriverInfoAdapter3 driverInfoAdapter3;

    public static ArrayList<DriverInfo> driverInfolist = new ArrayList<>();
    GoogleApiClient mGoogleApiClient;
    int radius = 2;
    ArrayList<String> rejecteddriver = new ArrayList<String>();
    String cartype, canceldriver, tripid, rej;
    RelativeLayout progresslayout;
    RecyclerView list, list2, list3;
    ImageView notify_bar;
    TahomaItalic sortbyname, sortbyname2, sortbyname3;
    Bundle extras;
    RelativeLayout sortlayout, recyclerlayout, sortlayout2, recyclerlayout2, sortlayout3, recyclerlayout3;
    RadioButton radioButton1,radioButton2;
    RadioGroup rg_payment;
    android.support.v7.app.AlertDialog progress_dialog;
    GraduallyTextView txt_load;
    ImageView img_gif;



    @Override
    public void onBackPressed() {
        Log.e("SelectLAT", pref_master.getTolat());
        Log.e("SelectLONG", pref_master.getTolng());


        Intent i = new Intent(getApplicationContext(), Dashboard.class);
        i.putExtra("lat", Double.parseDouble(pref_master.getTolat()));
        i.putExtra("lng",Double.parseDouble(pref_master.getTolng()));
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                DialogBox.setNoData(context,getString(R.string.location_enable), runnable);
            }
            /*Toast.makeText(context, "GPS is disabled!", Toast.LENGTH_LONG).show();*/
            else {
            }
        } else {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driverlist);

        pref_master = new Pref_Master(context);
        appdrawer = (RelativeLayout) findViewById(R.id.appdrawerdriverlist);
       // progress_dialog=new Progress_dialog();
        payby = findViewById(R.id.payby);
        driverlist = findViewById(R.id.driverlist);
        progresslayout = findViewById(R.id.progresslayout);
        queue = Volley.newRequestQueue(context);
        payby = findViewById(R.id.payby);
        paytype = findViewById(R.id.paytype);
        list = (RecyclerView) findViewById(R.id.list);
        notify_bar = findViewById(R.id.notify_bar);
        sortbyname = (TahomaItalic) findViewById(R.id.sortbyname);
        sortlayout = (RelativeLayout) findViewById(R.id.sortlayout);
        recyclerlayout = (RelativeLayout) findViewById(R.id.recyclerlayout);


/*
        list2 = (RecyclerView) findViewById(R.id.list2);
        sortbyname2 = (TahomaItalic) findViewById(R.id.sortbyname2);
        sortlayout2 = (RelativeLayout) findViewById(R.id.sortlayout2);
        recyclerlayout2 = (RelativeLayout) findViewById(R.id.recyclerlayout2);

        list3 = (RecyclerView) findViewById(R.id.list3);
        sortbyname3 = (TahomaItalic) findViewById(R.id.sortbyname3);
        sortlayout3 = (RelativeLayout) findViewById(R.id.sortlayout3);
        recyclerlayout3 = (RelativeLayout) findViewById(R.id.recyclerlayout3);
*/

        pref_master.setDriverfare("");
        pref_master.setDriverid("");
        pref_master.setDriverregistrationnumber("");
        pref_master.setCtype("");
        pref_master.setDrivertoken("");
        pref_master.setDriverprofilepic("");


        intent = getIntent();
        cartype = intent.getStringExtra("cartype");
        canceldriver = intent.getStringExtra("driverid");
        Log.e("the string driverid",":"+canceldriver);
        tripid = intent.getStringExtra("tripid");
        rej = intent.getStringExtra("rej");

        notify_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(layoutManager);

        radioButton1=(RadioButton)findViewById(R.id.radioButton1);
        radioButton2=(RadioButton)findViewById(R.id.radioButton2);
        rg_payment=(RadioGroup)findViewById(R.id.rg_payment);


       /* LinearLayoutManager layoutManager2 = new LinearLayoutManager(context);
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        list2.setLayoutManager(layoutManager2);


        LinearLayoutManager layoutManager3 = new LinearLayoutManager(context);
        layoutManager3.setOrientation(LinearLayoutManager.VERTICAL);
        list3.setLayoutManager(layoutManager3);*/


        driverInfoAdapter = new DriverInfoAdapter(context, driverInfolist);
        list.setAdapter(driverInfoAdapter);


       /* driverInfoAdapter2 = new DriverInfoAdapter2(context, driverInfolist);
        list2.setAdapter(driverInfoAdapter2);

        driverInfoAdapter3 = new DriverInfoAdapter3(context, driverInfolist);
        list3.setAdapter(driverInfoAdapter3);


*/

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        DateFormat df = new SimpleDateFormat("yyyy-MMM-d HH:mm:ss");
        CurrentDate = df.format(c.getTime());
        Log.e("Dateeeee", CurrentDate);

        if(Build.VERSION.SDK_INT>=21) {

            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_checked},
                            new int[]{android.R.attr.state_checked}
                    },
                    new int[]{

                            Color.DKGRAY
                            , Color.rgb(255, 165, 0),
                    }
            );
            radioButton1.setButtonTintList(colorStateList);
            radioButton2.setButtonTintList(colorStateList);

        }
        rg_payment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                paymentMethod = radioButton.getText().toString();
                Log.e("the redio button value",":::"+paymentMethod);
            }
        });

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

            DialogBox.setNoData(context, getString(R.string.location_enable), getRunnable);
        } else {

        }


        MyApplication.getInstance().trackScreenView("DriverList");



     /*   recyclerlayout2.setVisibility(View.GONE);
        recyclerlayout3.setVisibility(View.GONE);*/


       /* sortlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerlayout.setVisibility(View.VISIBLE);
                recyclerlayout2.setVisibility(View.GONE);
                recyclerlayout3.setVisibility(View.GONE);
            }
        });

        sortlayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerlayout2.setVisibility(View.VISIBLE);
                recyclerlayout.setVisibility(View.GONE);
                recyclerlayout3.setVisibility(View.GONE);
            }
        });
        sortlayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerlayout3.setVisibility(View.VISIBLE);
                recyclerlayout2.setVisibility(View.GONE);
                recyclerlayout.setVisibility(View.GONE);
            }
        });*/

        submit = findViewById(R.id.submit);


       /* paymentMethods = new String[]{"Cash", "Card"};*/


       /* payby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });*/


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pref_master.getDriverid().equals("")) {
                    DialogBox.setPayby(DriverList.this, getString(R.string.please_select_driver));
                } else if (paymentMethod.equals("")) {
                    DialogBox.setPayby(DriverList.this, getString(R.string.select_payment_type));
                } else {
                    makeJsonObjReq();
                }


            }
        });

    }


  /*  public void show() {
        final Dialog dialogNumberPicker = new Dialog(context);
        dialogNumberPicker.setContentView(R.layout.dialog_number_picker_pay);

        RelativeLayout buttonCancel = (RelativeLayout) dialogNumberPicker.findViewById(R.id.next);
        RelativeLayout buttonSet = (RelativeLayout) dialogNumberPicker.findViewById(R.id.cancel);

        final NumberPicker numberPicker =
                (NumberPicker) dialogNumberPicker.findViewById(R.id.numberPicker1);
        //Specify the maximum value/number of NumberPicker
        numberPicker.setMaxValue(paymentMethods.length - 1); //to array last value
        //Populate NumberPicker values from String array values
        //Set the minimum value of NumberPicker
        numberPicker.setMinValue(0);
        //Gets whether the selector wheel wraps when reaching the min/max value.
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setDisplayedValues(paymentMethods);
        numberPicker.setOnValueChangedListener(this);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogNumberPicker.dismiss();

                Log.d("press", "Press button SET np.getValue(): " + numberPicker.getValue());
                paymentMethod = paymentMethods[numberPicker.getValue()];
                if (paymentMethods[numberPicker.getValue()].equalsIgnoreCase("Cash")) {
                    paymentMethod = "Cash";
                } else if (paymentMethods[numberPicker.getValue()].equalsIgnoreCase("Card")) {
                    paymentMethod = "Card";
                }

                Log.d("Select", paymentMethod);
                paytype.setText(paymentMethod);

            }
        });

        buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogNumberPicker.dismiss();
            }
        });

        dialogNumberPicker.show();


        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        DateFormat df = new SimpleDateFormat("yyyy-MMM-d HH:mm:ss");
        CurrentDate = df.format(c.getTime());
        Log.e("Dateeeee", CurrentDate);
    }
*/

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

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
                LayoutInflater li = LayoutInflater.from(context);
                View v = li.inflate(R.layout.progress_dialog_layout, null);
                progress_dialog = new android.support.v7.app.AlertDialog.Builder(context, R.style.cart_dialog).setView(v).show();
                progress_dialog.setCancelable(false);
                progress_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                txt_load = (GraduallyTextView) v.findViewById(R.id.txt_load);
                img_gif = (ImageView) v.findViewById(R.id.img_gif);

                Glide.with(context)
                        .load(R.drawable.gifimage111)
                        .asGif()
                        .placeholder(R.drawable.gifimage111)
                        .crossFade()
                        .into(img_gif);
                progress_dialog.show();

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


                                driverInfoAdapter = new DriverInfoAdapter(context, driverInfolist);
                                list.setAdapter(driverInfoAdapter);

/*
                                driverInfoAdapter2 = new DriverInfoAdapter2(context, driverInfolist);
                                list2.setAdapter(driverInfoAdapter2);
                                driverInfoAdapter3 = new DriverInfoAdapter3(context, driverInfolist);
                                list3.setAdapter(driverInfoAdapter3);*/
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


                        driverInfoAdapter.notifyDataSetChanged();
                        driverInfoAdapter2.notifyDataSetChanged();
                        driverInfoAdapter3.notifyDataSetChanged();

                        // FirebaseDatabase.getInstance().getReference("cars").child("driverDetail").child(childshot.getKey()).removeEventListener(DriverListValueevent);

                    }

                    @Override
                    public void onDataExited(DataSnapshot dataSnapshot) {

                        for (int i = 0; i < driverInfolist.size(); i++) {
                            if (dataSnapshot.getKey().equals(driverInfolist.get(i).getDriverid())) {

                                driverInfolist.remove(driverInfolist.get(i));
                                driverInfoAdapter.notifyDataSetChanged();
                                /*driverInfoAdapter2.notifyDataSetChanged();
                                driverInfoAdapter3.notifyDataSetChanged();*/

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


                        alert.dismiss();
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


                                driverInfoAdapter = new DriverInfoAdapter(context, driverInfolist);
                                list.setAdapter(driverInfoAdapter);


                              /*  driverInfoAdapter2 = new DriverInfoAdapter2(context, driverInfolist);
                                list2.setAdapter(driverInfoAdapter2);
                                driverInfoAdapter3 = new DriverInfoAdapter3(context, driverInfolist);
                                list3.setAdapter(driverInfoAdapter3);*/
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


                        driverInfoAdapter.notifyDataSetChanged();
                       /* driverInfoAdapter2.notifyDataSetChanged();
                        driverInfoAdapter3.notifyDataSetChanged();*/

                        // FirebaseDatabase.getInstance().getReference("cars").child("driverDetail").child(childshot.getKey()).removeEventListener(DriverListValueevent);

                    }

                    @Override
                    public void onDataExited(DataSnapshot dataSnapshot) {

                        for (int i = 0; i < driverInfolist.size(); i++) {
                            if (dataSnapshot.getKey().equals(driverInfolist.get(i).getDriverid())) {

                                driverInfolist.remove(driverInfolist.get(i));
                                driverInfoAdapter.notifyDataSetChanged();
                                /*driverInfoAdapter2.notifyDataSetChanged();
                                driverInfoAdapter3.notifyDataSetChanged();*/

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

       /* DialogBox.setDriverList(context, "Finding Your Ride Please Wait");*/

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("tripid", tripid);
        postParam.put("driverid", pref_master.getDriverid());
        postParam.put("riderid", pref_master.getUID());
        postParam.put("flatlong", pref_master.getFromlocationtrip());
        postParam.put("tlatlong", pref_master.getTolocationtrip());
        postParam.put("eta", pref_master.getTripeta());
        postParam.put("paymenttype", paymentMethod);
        postParam.put("fromaddrs", pref_master.getFromaddress());
        postParam.put("toaddrs", pref_master.getToaddress());
        postParam.put("distance", pref_master.getDistance());
        postParam.put("tripdatetime", CurrentDate);
        postParam.put("coupon", "");
        postParam.put("cprate", "");
        postParam.put("dfare", pref_master.getDriverfare());
        postParam.put("regno", pref_master.getDriverregistrationnumber());
        postParam.put("ctype", pref_master.getCtype());
        postParam.put("dtoken", pref_master.getDrivertoken());
        postParam.put("rname", pref_master.getFirstname() + " " + pref_master.getLastname());


        Log.e("parameter", " " + new JSONObject(postParam));


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Config.addtrip, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Reposnse", response.toString());
                       /* progress_dialog.dismiss();*/


                        try {
                            Log.e("Status", response.getString("status"));

                            if (response.getString("status").equals("200")) {

                                Toast.makeText(context, getString(R.string.request_send_driver), Toast.LENGTH_SHORT).show();
                                JSONArray jsonArray = new JSONArray(response.getString("data"));

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jobj1 = jsonArray.getJSONObject(i);
                                    pref_master.setTripid(jobj1.getString("tripid"));

                                    Log.e("the trip value","::"+ pref_master.getTripid());
                                }
                            } else {
                                Toast.makeText(context, response.getString("status"), Toast.LENGTH_SHORT).show();
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

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            finish();
        }
    };

    Runnable getRunnable = new Runnable() {
        @Override
        public void run() {

            Intent callGPSSettingIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(callGPSSettingIntent, 123);

        }
    };

    public void DriverRejeted() {
        progresslayout.setVisibility(View.GONE);
    }
}
