package com.taxibookingrider.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.taxibookingrider.Controller.Config;
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

public class ChangePasswordOTPscreen extends AppCompatActivity {


    RelativeLayout otpsubmit;
    EditText editextotp;
    RequestQueue queue;
    Pref_Master pref_master;
    LocationListener locationListener;
    LocationManager locationManager;
    Context context = this;
    String otp;
    RelativeLayout resend;
    String pass, uid;
    private FusedLocationProviderClient mFusedLocationClient;
    String android_id;
    android.support.v7.app.AlertDialog progress_dialog;
    GraduallyTextView txt_load;
    ImageView img_gif;
    //Progress_dialog progress_dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpscreen);
        Intent intent = getIntent();
        final String id = intent.getStringExtra("otp");
        pass = intent.getStringExtra("pass");
        pref_master = new Pref_Master(this);
        resend = (RelativeLayout) findViewById(R.id.skip);
       // progress_dialog=new Progress_dialog();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Log.e("OTP", id);


        editextotp = (EditText) findViewById(R.id.editextotp);
        otpsubmit = (RelativeLayout) findViewById(R.id.next);

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
                    makeJsonObjReq();

                } else {
                    editextotp.setError(getResources().getString(R.string.otp_verify));
                }
            }
        });

        MyApplication.getInstance().trackScreenView("ChangePasswordOTPscreen");
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
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.otp_message), Toast.LENGTH_SHORT).show();


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
        FragmentManager manager = null;
        final FragmentManager finalManager = manager;


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

        getLastLocation();

    }


    public void getLastLocation() {
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
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {


                    Map<String, String> postParam = new HashMap<String, String>();
                    postParam.put("mobile", pref_master.getUser_number());
                    postParam.put("password", pass);
                    postParam.put("utype", "Rider");
                    postParam.put("reason", "device-password");
                    postParam.put("deviceid", android_id);
                    postParam.put("otpcheck", "yes");
                    postParam.put("dtoken", FirebaseInstanceId.getInstance().getToken());
                    postParam.put("latlong", location.getLatitude() + "," + location.getLongitude());


                    Log.e("parameter", " " + new JSONObject(postParam));


                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                            Config.resetpassword, new JSONObject(postParam),
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.e("Reposnse", response.toString());

                                   progress_dialog.dismiss();
                                    try {
                                        Log.e("Status", response.getString("status"));

                                        JSONArray jsonArray = new JSONArray(response.getString("data"));

                                        if (response.getString("status").equals("200")) {
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jobj1 = jsonArray.getJSONObject(i);

                                                uid = jobj1.getString("uid");
                                                pref_master.setUID(uid);
                                            }


                                            Intent ii = new Intent(getApplicationContext(), Dashboard.class);
                                            ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(ii);
                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.reset_password), Toast.LENGTH_SHORT).show();


                                        } else {
                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_response),Toast.LENGTH_SHORT).show();
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
                         */
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {

                            return Config.getHeaderParam(context);
                        }


                    };
                    jsonObjReq.setTag("POST");
                    // Adding request to request queue
                    queue.add(jsonObjReq);

                } else {
                    getNewLOcation();
                }
            }
        });
    }


    public void getNewLOcation() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    locationManager.removeUpdates(locationListener);

                    Map<String, String> postParam = new HashMap<String, String>();
                    postParam.put("mobile", pref_master.getUser_number());
                    postParam.put("password", pass);
                    postParam.put("utype", "Rider");
                    postParam.put("reason", "device-password");
                    postParam.put("deviceid", android_id);
                    postParam.put("otpcheck", "yes");
                    postParam.put("dtoken", FirebaseInstanceId.getInstance().getToken());
                    postParam.put("latlong", location.getLatitude() + "," + location.getLongitude());


                    Log.e("parameter", " " + new JSONObject(postParam));


                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                            Config.resetpassword, new JSONObject(postParam),
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.e("Reposnse", response.toString());

                                    progress_dialog.dismiss();
                                    try {
                                        Log.e("Status", response.getString("status"));

                                        JSONArray jsonArray = new JSONArray(response.getString("data"));

                                        if (response.getString("status").equals("200")) {
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jobj1 = jsonArray.getJSONObject(i);

                                                uid = jobj1.getString("uid");
                                                pref_master.setUID(uid);
                                            }


                                            Intent ii = new Intent(getApplicationContext(), Dashboard.class);
                                            ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(ii);
                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.reset_password), Toast.LENGTH_SHORT).show();


                                        } else {
                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_response), Toast.LENGTH_SHORT).show();
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
                         */
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            return Config.getHeaderParam(context);
                        }


                    };
                    jsonObjReq.setTag("POST");
                    // Adding request to request queue
                    queue.add(jsonObjReq);

                } else {
                   progress_dialog.dismiss();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.location_not_found), Toast.LENGTH_LONG).show();
                    Log.e("ChangePasswordNewUSer", "Location");
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }
}
