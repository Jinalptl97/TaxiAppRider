package com.taxibookingrider.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.google.firebase.database.FirebaseDatabase;
import com.taxibookingrider.Controller.Config;
import com.taxibookingrider.Controller.DialogBox;
import com.taxibookingrider.Controller.GraduallyTextView;
import com.taxibookingrider.Controller.Progress_dialog;
import com.taxibookingrider.Controller.Tahoma;
import com.taxibookingrider.Controller.TahomaBold;
import com.taxibookingrider.MyApplication;
import com.taxibookingrider.R;
import com.taxibookingrider.Utils.Pref_Master;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 1/9/2018.
 */

public class Payment extends AppCompatActivity {


    Context context = this;
    Tahoma fromaddress, toaddress, couponcode, waitingtime, cancelllationcharge, fare;
    TahomaBold finalfare;
    RelativeLayout pay;
   // Progress_dialog progress_dialog;
    Pref_Master pref_master;
    RequestQueue queue;
    String cncharge, totalfare, ptype;
    String stdon, endon;
    android.support.v7.app.AlertDialog progress_dialog;
    GraduallyTextView txt_load;
    ImageView img_gif;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123) {
            final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                DialogBox.setNoData(context, "Location is Disable Please Enable The Location", runnable);
            }
            /*Toast.makeText(context, "GPS is disabled!", Toast.LENGTH_LONG).show();*/
            else {

            }
        } else {

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_payment);
       // progress_dialog=new Progress_dialog();

        pref_master = new Pref_Master(context);
        queue = Volley.newRequestQueue(context);
        fromaddress = findViewById(R.id.fromaddress);
        toaddress = findViewById(R.id.toaddress);
        couponcode = findViewById(R.id.couponcode);
        waitingtime = findViewById(R.id.waitingtime);
        cancelllationcharge = findViewById(R.id.cancelllationcharge);
        fare = findViewById(R.id.fare);
        finalfare = findViewById(R.id.finalfare);
        pay = findViewById(R.id.pay);


        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            DialogBox.setNoData(context, getString(R.string.location_enable), runnable);
        } else {

        }


        makeJsonObjReq();

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Payment();
            }
        });


        MyApplication.getInstance().trackScreenView("Payment");

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
        postParam.put("riderid", pref_master.getUID());
        postParam.put("driverid", pref_master.getDriverid());
        postParam.put("tripid", pref_master.getTripid());

        Log.e("parameter", " " + new JSONObject(postParam));


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Config.trippaymentdetails, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Reposnse", response.toString());
                         progress_dialog.dismiss();
                        try {
                            Log.e("Status", response.getString("status"));

                            if (response.getString("status").equals("200")) {


                                JSONArray jsonArray = new JSONArray(response.getString("data"));


                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jobj1 = jsonArray.getJSONObject(i);

                                    fromaddress.setText(jobj1.getString("from"));
                                    toaddress.setText(jobj1.getString("to"));
                                    String couponcodes = jobj1.getString("cprate");

                                    if (couponcodes.equals("")) {
                                        couponcodes = Config.currency +"0.00";
                                        couponcode.setText(couponcodes);
                                    } else {
                                        couponcode.setText(Config.currency +couponcodes);
                                    }

                                    String waitingtimes = jobj1.getString("wcharge");
                                    if (waitingtimes.equals("0.00")) {
                                        waitingtimes = Config.currency + "0.00";
                                        waitingtime.setText(waitingtimes);
                                    } else {
                                        waitingtime.setText(waitingtimes);
                                    }

                                    cancelllationcharge.setText(Config.currency + jobj1.getString("cancelcharge"));
                                    fare.setText(Config.currency + jobj1.getString("total"));
                                    finalfare.setText(Config.currency + jobj1.getString("totalfare"));
                                    stdon = jobj1.getString("stdon");
                                    endon = jobj1.getString("endon");
                                    totalfare = jobj1.getString("totalfare");
                                    ptype = jobj1.getString("ptype");
                                    cncharge = jobj1.getString("cancelcharge");
                                }

                                pref_master.setStdon(stdon);
                                pref_master.setEndon(endon);


                            } else if (response.getString("status").equals("400")) {
                                Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
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


    private void Payment() {

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

        FragmentManager manager = null;
        final FragmentManager finalManager = manager;


        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("riderid", pref_master.getUID());
        postParam.put("driverid", pref_master.getDriverid());
        postParam.put("tripid", pref_master.getTripid());
        postParam.put("cncharge", cncharge);
        postParam.put("totalfare", totalfare);
        postParam.put("ptype", ptype);
        postParam.put("stdon", pref_master.getStdon());
        postParam.put("endon", pref_master.getEndon());
        postParam.put("dtoken", pref_master.getDrivertoken());

        Log.e("parameter", " " + new JSONObject(postParam));


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Config.receivepayment, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Reposnse", response.toString());


                        try {
                            Log.e("Status", response.getString("status"));

                            if (response.getString("status").equals("200")) {
                                progress_dialog.dismiss();
                                FirebaseDatabase.getInstance().getReference("cars").child("trip").child(pref_master.getTripid()).child("tripPage").setValue("4");
                                Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), Ride_rating.class);
                                startActivity(i);

                            } else if (response.getString("status").equals("400")) {
                                progress_dialog.dismiss();
                                Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                progress_dialog.dismiss();
                                Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
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

            Intent callGPSSettingIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(callGPSSettingIntent, 123);
        }
    };


}
