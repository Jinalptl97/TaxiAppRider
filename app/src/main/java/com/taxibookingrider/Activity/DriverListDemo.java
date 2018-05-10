package com.taxibookingrider.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.taxibookingrider.Controller.Config;
import com.taxibookingrider.Controller.DialogBox;
import com.taxibookingrider.Controller.GraduallyTextView;
import com.taxibookingrider.Controller.Progress_dialog;
import com.taxibookingrider.Fragment.ShortByETA;
import com.taxibookingrider.Fragment.ShortByPrice;
import com.taxibookingrider.Fragment.ShortByRating;
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
import java.util.List;
import java.util.Map;

/**
 * Created by Ankita on 3/31/2018.
 */

public class DriverListDemo extends AppCompatActivity  implements NumberPicker.OnValueChangeListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {


    Context context = this;
    Pref_Master pref_master;
    Intent intent;
    String cartype, canceldriver, tripid, rej;
    GoogleApiClient mGoogleApiClient;
    Bundle bundle;
    RequestQueue queue;
    RelativeLayout appdrawer, submit;
   // Progress_dialog progress_dialog;
    RelativeLayout progresslayout;
    ImageView notify_bar;
    RadioButton radioButton1,radioButton2;
    RadioGroup rg_payment;
    String paymentMethod = "";
    String CurrentDate;


    android.support.v7.app.AlertDialog progress_dialog;
    GraduallyTextView txt_load;
    ImageView img_gif;




    @Override
    public void onBackPressed() {
        pref_master = new Pref_Master(context);
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

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            finish();
        }
    };


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driverlist);


        pref_master = new Pref_Master(context);

        Log.e("SelectLATTTTTTTTTTTT", pref_master.getTolat());


        appdrawer = (RelativeLayout) findViewById(R.id.appdrawerdriverlist);
        //progress_dialog=new Progress_dialog();
        progresslayout = findViewById(R.id.progresslayout);
        notify_bar = findViewById(R.id.notify_bar);
        submit = findViewById(R.id.submit);

        queue = Volley.newRequestQueue(context);




        pref_master.setDriverfare("");
        pref_master.setDriverid("");
        pref_master.setDriverregistrationnumber("");
        pref_master.setCtype("");
        pref_master.setDrivertoken("");
        pref_master.setDriverprofilepic("");


        intent = getIntent();
        cartype = intent.getStringExtra("cartype");

        Log.e("SelectLAT", cartype);
        canceldriver = intent.getStringExtra("driverid");
        tripid = intent.getStringExtra("tripid");
        rej = intent.getStringExtra("rej");


        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        radioButton1=(RadioButton)findViewById(R.id.radioButton1);
        radioButton2=(RadioButton)findViewById(R.id.radioButton2);
        rg_payment=(RadioGroup)findViewById(R.id.rg_payment);


       /*  bundle=new Bundle();
        bundle.putString("rej", rej);
        bundle.putString("cartype",cartype);
        bundle.putString("cancledrive",canceldriver);*/



        // Add Fragments to adapter one by one
        adapter.addFragment(new ShortByPrice(), "Sort by Price");
        adapter.addFragment(new ShortByRating(), "Sort by Rating");
        adapter.addFragment(new ShortByETA(), "Sort by ETA");
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

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

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        DateFormat df = new SimpleDateFormat("yyyy-MMM-d HH:mm:ss");
        CurrentDate = df.format(c.getTime());
        Log.e("Dateeeee", CurrentDate);


        notify_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("SelectLAT", pref_master.getTolat());
                Log.e("SelectLONG", pref_master.getTolng());


                Intent i = new Intent(getApplicationContext(), Dashboard.class);
                i.putExtra("lat", Double.parseDouble(pref_master.getTolat()));
                i.putExtra("lng",Double.parseDouble(pref_master.getTolng()));
                startActivity(i);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pref_master.getDriverid().equals("")) {
                    DialogBox.setPayby(DriverListDemo.this, getString(R.string.please_select_driver));
                } else if (paymentMethod.equals("")) {
                    DialogBox.setPayby(DriverListDemo.this, getString(R.string.select_payment_type));
                } else {
                    makeJsonObjReq();
                }


            }
        });
        MyApplication.getInstance().trackScreenView("DriverListDemo");



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



    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();




        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);

        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
