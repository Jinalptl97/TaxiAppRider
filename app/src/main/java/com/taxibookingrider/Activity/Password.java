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
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.taxibookingrider.Controller.Config;
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

public class Password extends AppCompatActivity {

    RelativeLayout enter;
    Context context = this;
    RelativeLayout relativeLayout;
    RequestQueue queue;
    Pref_Master pref_master;
    EditText enterpassword;
    String android_id;
    Tahoma forgotpassword;
    LocationManager locationManager;
    LocationRequest mLocationRequest;
    int splash = 0;
    LocationListener locationListener;
    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocationClient;
    //Progress_dialog progress_dialog;
    android.support.v7.app.AlertDialog progress_dialog;
    GraduallyTextView txt_load;
    ImageView img_gif;
    String uid, fnm, lnm, otp;
    TextInputLayout input_layout_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        enter = (RelativeLayout) findViewById(R.id.enter);
        pref_master = new Pref_Master(context);
       // progress_dialog=new Progress_dialog();
        enterpassword = (EditText) findViewById(R.id.enterpassword);
        relativeLayout = (RelativeLayout) findViewById(R.id.whole);
        queue = Volley.newRequestQueue(this);
        input_layout_password=(TextInputLayout)findViewById(R.id.input_layout_password);
        forgotpassword = (Tahoma) findViewById(R.id.forgotpassword);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);


        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (enterpassword.getText().toString().trim().length() == 0) {
                    input_layout_password.setError(getResources().getString(R.string.error_password));
                } else if (enterpassword.getText().toString().trim().length() < 6) {
                    input_layout_password.setError(getResources().getString(R.string.error_password_length));
                } else {
                    input_layout_password.setErrorEnabled(false);
                    makeJsonObjReq();

                }
            }
        });


        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Resend_OTP();
            }
        });

        MyApplication.getInstance().trackScreenView("Password");

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
                    postParam.put("utype", "Rider");
                    postParam.put("deviceid", android_id);
                    postParam.put("dtoken", FirebaseInstanceId.getInstance().getToken());
                    postParam.put("password", enterpassword.getText().toString());
                    postParam.put("latlong", location.getLatitude() + "," + location.getLongitude());


                    Log.e("parameter", " " + new JSONObject(postParam));


                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                            Config.login, new JSONObject(postParam),
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.e("Reposnse", response.toString());
                                    progress_dialog.dismiss();

                                    try {
                                        Log.e("Status", response.getString("status"));

                                        if (response.getString("status").equals("200")) {

                                            String activity = response.getString("activity");

                                            if (activity.equals("valid")) {

                                                JSONArray jsonArray = new JSONArray(response.getString("data"));

                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    JSONObject jobj1 = jsonArray.getJSONObject(i);

                                                    uid = jobj1.getString("uid");
                                                    fnm = jobj1.getString("fnm");
                                                    lnm = jobj1.getString("lnm");
                                                    pref_master.setUID(uid);
                                                    pref_master.setFirstname(fnm);
                                                    pref_master.setLastname(lnm);

                                                    Intent ii = new Intent(getApplicationContext(), Dashboard.class);
                                                    ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(ii);

                                                    Log.e("Response", uid + fnm + lnm);
                                                }
                                            }
                                        } else {
                                            progress_dialog.dismiss();
                                            String activity = response.getString("activity");

                                            if (activity.equals("invalid")) {
                                                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                                enterpassword.setText("");

                                            }
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
                    getNewLocation();
                }
            }
        });
    }


    public void getNewLocation() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    locationManager.removeUpdates(locationListener);
                    Map<String, String> postParam = new HashMap<String, String>();
                    postParam.put("mobile", pref_master.getUser_number());
                    postParam.put("utype", "Rider");
                    postParam.put("deviceid", android_id);
                    postParam.put("dtoken", FirebaseInstanceId.getInstance().getToken());
                    postParam.put("password", enterpassword.getText().toString());
                    postParam.put("latlong", location.getLatitude() + "," + location.getLongitude());


                    Log.e("parameter", " " + new JSONObject(postParam));


                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                            Config.login, new JSONObject(postParam),
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.e("Reposnse", response.toString());
                                    progress_dialog.dismiss();

                                    try {
                                        Log.e("Status", response.getString("status"));

                                        if (response.getString("status").equals("200")) {

                                            String activity = response.getString("activity");

                                            if (activity.equals("valid")) {

                                                JSONArray jsonArray = new JSONArray(response.getString("data"));

                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    JSONObject jobj1 = jsonArray.getJSONObject(i);

                                                    uid = jobj1.getString("uid");
                                                    fnm = jobj1.getString("fnm");
                                                    lnm = jobj1.getString("lnm");
                                                    pref_master.setUID(uid);
                                                    pref_master.setFirstname(fnm);
                                                    pref_master.setLastname(lnm);

                                                    Intent ii = new Intent(getApplicationContext(), Dashboard.class);
                                                    ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(ii);

                                                    Log.e("Response", uid + fnm + lnm);
                                                }
                                            }
                                        } else {
                                           progress_dialog.dismiss();
                                            String activity = response.getString("activity");

                                            if (activity.equals("invalid")) {
                                                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                                enterpassword.setText("");

                                            }
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
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.location_not_found), Toast.LENGTH_LONG).show();
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
                            Intent i = new Intent(getApplicationContext(), ResetPasswordOTPscreen.class);
                            i.putExtra("otp", otp);
                            startActivity(i);

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


    private void startLocationUpdates() {
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
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                null /* Looper */);
    }
}
