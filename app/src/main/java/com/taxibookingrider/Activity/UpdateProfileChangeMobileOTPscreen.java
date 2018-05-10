package com.taxibookingrider.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.taxibookingrider.Controller.Config;
import com.taxibookingrider.Controller.GraduallyTextView;
import com.taxibookingrider.Controller.Progress_dialog;
import com.taxibookingrider.MyApplication;
import com.taxibookingrider.R;
import com.taxibookingrider.Utils.Pref_Master;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfileChangeMobileOTPscreen extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    RelativeLayout otpsubmit;
    EditText editextotp;
    //Progress_dialog progress_dialog;
    RequestQueue queue;
    Pref_Master pref_master;
    GoogleApiClient mGoogleApiClient;
    Context context = this;
    String otp;
    RelativeLayout resend;
    Location location;
    String pass;
    String mobile, email, fname, lname, password, image;
    String android_id;
    android.support.v7.app.AlertDialog progress_dialog;
    GraduallyTextView txt_load;
    ImageView img_gif;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpscreen);
        Intent intent = getIntent();
        final String id = intent.getStringExtra("otp");
        password = intent.getStringExtra("pass");
        fname = intent.getStringExtra("fname");
        lname = intent.getStringExtra("lname");
        mobile = intent.getStringExtra("mobile");
        email = intent.getStringExtra("email");
        image = intent.getStringExtra("image");

        pref_master = new Pref_Master(this);
        resend = (RelativeLayout) findViewById(R.id.skip);

        Log.e("OTP", id);


        editextotp = (EditText) findViewById(R.id.editextotp);
        otpsubmit = (RelativeLayout) findViewById(R.id.next);


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


       // progress_dialog=new Progress_dialog();
        queue = Volley.newRequestQueue(this);


        android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);


        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Resend_OTP();
            }
        });

        otpsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editextotp.getText().toString().equals(id) || editextotp.getText().toString().equals(otp)) {
                    if (mGoogleApiClient.isConnected()) {
                        makeJsonObjReq();
                    }
                } else {
                    editextotp.setError("Please Check OTP");
                }
            }
        });

        MyApplication.getInstance().trackScreenView("UpdateProfileChangeMobileOTPscreen");

    }


    public void Resend_OTP() {
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




        queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.resend + pref_master.getUser_number() + "Rider",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       progress_dialog.dismiss();
                        String jsonData = response.toString();
                        Log.e("Response", jsonData);

                        try {
                            JSONObject jobj = new JSONObject(jsonData);
                            otp = jobj.getString("OTP");
                            Toast.makeText(getApplicationContext(), getString(R.string.otp_message), Toast.LENGTH_LONG).show();


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

// Add the request to the RequestQueue.
        queue.add(stringRequest);


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
        postParam.put("uid", pref_master.getUID());
        postParam.put("utype", "Rider");
        postParam.put("oldmobile", pref_master.getUser_number());
        postParam.put("newmobile", mobile);
        postParam.put("oldemail", pref_master.getEmail());
        postParam.put("newemail", email);
        postParam.put("fname", fname);
        postParam.put("lname", lname);
        /*postParam.put("password", password);*/
        postParam.put("upic", image);
        postParam.put("verifiedotp", "yes");

        Log.e("parameter", " " + new JSONObject(postParam));


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Config.updateprofile, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Reposnse", response.toString());
                        progress_dialog.dismiss();
                        try {
                            Log.e("Status", response.getString("status"));

                            if (response.getString("status").equals("200")) {
                                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                JSONArray jsonArray = new JSONArray(response.getString("data"));

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jobj1 = jsonArray.getJSONObject(i);

                                    pref_master.setFirstname(jobj1.getString("fname"));
                                    pref_master.setLastname(jobj1.getString("lname"));
                                    pref_master.setUser_number(jobj1.getString("newmobile"));
                                    pref_master.setEmail(jobj1.getString("newemail"));
                                    pref_master.setUserimage(jobj1.getString("upic"));
                                }
                                Intent i = new Intent(getApplicationContext(), Profile.class);
                                startActivity(i);

                            } else if (response.getString("status").equals("400")) {

                                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();

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
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }


        };
        jsonObjReq.setTag("POST");
        // Adding request to request queue
        queue.add(jsonObjReq);

        // Cancelling request
    /* if (queue!= null) {
    queue.cancelAll(TAG);
    } */

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
