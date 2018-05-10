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
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Change_password_for_newuser extends AppCompatActivity {

    EditText newpassword, confirmpassword;
    RelativeLayout submit;
    RequestQueue queue;

    Pref_Master pref_master;
    Context context = this;
    String android_id;
    LocationListener locationListener;
    LocationManager locationManager;
    Location location;

    //Progress_dialog progress_dialog;
    android.support.v7.app.AlertDialog progress_dialog;
    GraduallyTextView txt_load;
    ImageView img_gif;
    String password;
    private FusedLocationProviderClient mFusedLocationClient;
    TextInputLayout input_layout_newpassword,input_confirmpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset__password);

        newpassword = (EditText) findViewById(R.id.newpassword);
        confirmpassword = (EditText) findViewById(R.id.confirmnewpassword);
        input_layout_newpassword=(TextInputLayout)findViewById(R.id.input_layout_newpassword);
        input_confirmpassword=(TextInputLayout)findViewById(R.id.input_confirmpassword);
        submit = (RelativeLayout) findViewById(R.id.next);
        queue = Volley.newRequestQueue(this);
        pref_master = new Pref_Master(this);
        //progress_dialog=new Progress_dialog();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);



        newpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                input_layout_newpassword.setErrorEnabled(false);

            }
        });

        confirmpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                input_confirmpassword.setErrorEnabled(false);

            }
        });

        MyApplication.getInstance().trackScreenView("Change_password_for_newuser");

        android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (newpassword.getText().toString().trim().length() == 0) {
                    newpassword.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    input_layout_newpassword.setError(getResources().getString(R.string.error_password));
                } else if (newpassword.getText().toString().trim().length() < 6) {
                    newpassword.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    input_layout_newpassword.setError(getResources().getString(R.string.error_password_length));
                } else if (confirmpassword.getText().toString().trim().length() == 0) {
                    input_layout_newpassword.setErrorEnabled(false);
                    confirmpassword.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    input_confirmpassword.setError(getResources().getString(R.string.error_confirm_password));
                } else if (!newpassword.getText().toString().equals(confirmpassword.getText().toString())) {
                    input_confirmpassword.setError(getResources().getString(R.string.error_password_match));
                } else {
                    input_layout_newpassword.setErrorEnabled(false);
                    input_confirmpassword.setErrorEnabled(false);
                    makeJsonObjReq();
                }
            }
        });


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
                    // Logic to handle location object

                    Log.e("Lat", "" + location.getLatitude());
                    Log.e("Long", "" + location.getLongitude());


                    Map<String, String> postParam = new HashMap<String, String>();
                    postParam.put("mobile", pref_master.getUser_number());
                    postParam.put("password", confirmpassword.getText().toString());
                    postParam.put("utype", "Rider");
                    postParam.put("reason", "device-password");
                    postParam.put("deviceid", android_id);
                    postParam.put("otpcheck", "no");
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

                                        if (response.getString("status").equals("200")) {


                                            Toast.makeText(getApplicationContext(),getResources().getString(R.string.otp_message), Toast.LENGTH_SHORT).show();

                                            Intent i = new Intent(getApplicationContext(), ChangePasswordOTPscreen.class);
                                            i.putExtra("otp", response.getString("OTP"));
                                            i.putExtra("pass", confirmpassword.getText().toString());
                                            startActivity(i);

                                        } else {
                                           progress_dialog.dismiss();
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
                    // Logic to handle location object

                    Log.e("Lat", "" + location.getLatitude());
                    Log.e("Long", "" + location.getLongitude());


                    Map<String, String> postParam = new HashMap<String, String>();
                    postParam.put("mobile", pref_master.getUser_number());
                    postParam.put("password", confirmpassword.getText().toString());
                    postParam.put("utype", "Rider");
                    postParam.put("reason", "device-password");
                    postParam.put("deviceid", android_id);
                    postParam.put("otpcheck", "no");
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

                                        if (response.getString("status").equals("200")) {


                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.otp_message), Toast.LENGTH_SHORT).show();

                                            Intent i = new Intent(getApplicationContext(), ChangePasswordOTPscreen.class);
                                            i.putExtra("otp", response.getString("OTP"));
                                            i.putExtra("pass", confirmpassword.getText().toString());
                                            startActivity(i);

                                        } else {
                                            progress_dialog.dismiss();
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
}
