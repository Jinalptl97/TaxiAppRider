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
import android.widget.TextView;
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

import static com.taxibookingrider.Controller.Config.isValidEmailAddress;

public class Registration extends AppCompatActivity {

    Context context = this;
    Pref_Master pref_master;
   /* TextView mobilenumber;*/
    EditText firstname, lastname, password, emailid;
    Tahoma mobilenumber;
    RelativeLayout next;
    LocationManager locationManager;
    RequestQueue queue;
    Location locations;
    String android_id;
    LocationListener locationListener;
   // Progress_dialog progress_dialog;
    private FusedLocationProviderClient mFusedLocationClient;
    TextInputLayout input_fname,input_lname,input_email,input_password;
    android.support.v7.app.AlertDialog progress_dialog;
    GraduallyTextView txt_load;
    ImageView img_gif;


    @Override
    public void onBackPressed() {
        System.exit(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        pref_master = new Pref_Master(context);

       // progress_dialog=new Progress_dialog();


        android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mobilenumber = (Tahoma) findViewById(R.id.mobilenumber);
        mobilenumber.setText(pref_master.getUser_number());
        firstname = (EditText) findViewById(R.id.edfirstname);
        lastname = (EditText) findViewById(R.id.edlastname);
        password = (EditText) findViewById(R.id.edpassword);
        emailid = (EditText) findViewById(R.id.edemailid);
        next = (RelativeLayout) findViewById(R.id.next);
        input_fname=(TextInputLayout)findViewById(R.id.input_fname);
        input_lname=(TextInputLayout)findViewById(R.id.input_lname);
        input_email=(TextInputLayout)findViewById(R.id.input_email);
        input_password=(TextInputLayout)findViewById(R.id.input_password);


        firstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                input_fname.setErrorEnabled(false);

            }
        });


        lastname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                input_lname.setErrorEnabled(false);

            }
        });


        emailid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                input_email.setErrorEnabled(false);

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                input_password.setErrorEnabled(false);

            }
        });




        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            DialogBox.setNoData(context, getString(R.string.location_enable), runnable);
        } else {

        }
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queue = Volley.newRequestQueue(context);
                if (firstname.getText().toString().trim().length() == 0) {

                    input_fname.setError(getString(R.string.enter_firstname));
                } else if (lastname.getText().toString().trim().length() == 0) {
                    lastname.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    input_lname.setError(getString(R.string.enter_lastname));
                    input_fname.setErrorEnabled(false);

                } else if (emailid.getText().toString().trim().length() != 0) {
                    if (emailid.getText().toString().contains(" ")) {
                        emailid.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                        input_email.setError(getString(R.string.enter_email));
                    } else if (!isValidEmailAddress(emailid.getText().toString())) {
                        emailid.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                        input_email.setError(getString(R.string.enter_valid_email));
                    } else if (password.getText().toString().trim().length() == 0) {
                        input_email.setErrorEnabled(false);
                        password.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                        input_password.setError(getString(R.string.error_password));
                    } else if (password.getText().toString().trim().length() < 6) {
                        password.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                        input_password.setError(getString(R.string.error_password_length));
                    } else {
                        input_password.setErrorEnabled(false);
                        makeJsonObjReqCheckmobile();
                    }
                } else if (emailid.getText().toString().contains(" ")) {
                    emailid.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    input_email.setError(getString(R.string.enter_email));
                } else if (password.getText().toString().trim().length() == 0) {
                    input_email.setErrorEnabled(false);
                    password.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                    input_password.setError(getString(R.string.error_password));
                } else if (password.getText().toString().trim().length() < 6) {
                    password.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    input_password.setError(getString(R.string.error_password_length));
                } else {
                    input_password.setErrorEnabled(false);
                    makeJsonObjReqCheckmobile();
                }
            }
        });

        MyApplication.getInstance().trackScreenView("Registration");

    }


    private void makeJsonObjReq() {


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


    public void getNewloCation() {
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

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    locationManager.removeUpdates(locationListener);
                    Map<String, String> postParam = new HashMap<String, String>();
                    postParam.put("mobile", pref_master.getUser_number());
                    postParam.put("password", password.getText().toString());
                    postParam.put("fnm", firstname.getText().toString());
                    postParam.put("lnm", lastname.getText().toString());
                    postParam.put("email", emailid.getText().toString());
                    postParam.put("deviceid", android_id);
                    postParam.put("dtoken", FirebaseInstanceId.getInstance().getToken());
                    postParam.put("latlong", location.getLatitude() + "," + location.getLongitude());
                    Log.e("parameter", " " + new JSONObject(postParam));


                    final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                            Config.register, new JSONObject(postParam),
                            new Response.Listener<JSONObject>() {


                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.e("Reposnse_Registor", response.toString());

                                    progress_dialog.dismiss();

                                    try {
                                        Log.e("Status", response.getString("status"));

                                        if (response.getString("status").equals("200")) {


                                            Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();


                                            JSONArray jsonArray = new JSONArray(response.getString("data"));

                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jobj1 = jsonArray.getJSONObject(i);

                                                pref_master.setUID(jobj1.getString("uid"));
                                            }


                                            Intent i = new Intent(context, Dashboard.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(i);

                                        } else if (response.getString("status").equals("400")) {


                                            String activity = response.getString("activity");

                                            if (activity.equals("email-exist")) {

                                                //  DialogBox.setEmailExits(Registration.this, "This Email Address is Already Registered !");

                                                Toast.makeText(getApplicationContext(), getString(R.string.email_already_register), Toast.LENGTH_SHORT).show();
                                            }  else if (activity.equals("device-exist")) {
                                                DialogBox.setEmailExits(Registration.this, getString(R.string.mobile_already_register));
                                            }else if (activity.equals("inactive")) {
                                                DialogBox.setEmailExits(Registration.this, getString(R.string.mobile_already_register));
                                            } else {
                                                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(), getString(R.string.location_enable), Toast.LENGTH_LONG).show();
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
            // to handle the case where the user grants the permission. See the docume
            // ntation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
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
                    postParam.put("password", password.getText().toString());
                    postParam.put("fnm", firstname.getText().toString());
                    postParam.put("lnm", lastname.getText().toString());
                    postParam.put("email", emailid.getText().toString());
                    postParam.put("deviceid", android_id);
                    postParam.put("dtoken", FirebaseInstanceId.getInstance().getToken());
                    postParam.put("latlong", location.getLatitude() + "," + location.getLongitude());
                    Log.e("parameter", " " + new JSONObject(postParam));


                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                            Config.register, new JSONObject(postParam),
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.e("Reposnse_Registor", response.toString());
                                    progress_dialog.dismiss();

                                    try {
                                        Log.e("Status", response.getString("status"));

                                        if (response.getString("status").equals("200")) {


                                            Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();


                                            JSONArray jsonArray = new JSONArray(response.getString("data"));

                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jobj1 = jsonArray.getJSONObject(i);

                                                pref_master.setUID(jobj1.getString("uid"));
                                            }


                                            Intent i = new Intent(context, Dashboard.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(i);

                                        } else if (response.getString("status").equals("400")) {


                                            String activity = response.getString("activity");

                                            if (activity.equals("email-exist")) {
                                                Toast.makeText(getApplicationContext(), getString(R.string.email_already_register), Toast.LENGTH_SHORT).show();
                                                //  DialogBox.setEmailExits(Registration.this, "This Email Address is Already Registered !");

                                            } else if (activity.equals("inactive")) {
                                                DialogBox.setEmailExits(Registration.this, getString(R.string.mobile_already_register));
                                            } else if (activity.equals("device-exist")) {
                                                DialogBox.setEmailExits(Registration.this, getString(R.string.mobile_already_register));
                                            } else {
                                                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
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
                    getNewloCation();
                }
            }
        });
    }

    private void makeJsonObjReqCheckmobile() {

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
        postParam.put("mobile", pref_master.getUser_number());
        postParam.put("utype", "Rider");
        postParam.put("deviceid", android_id);

        Log.e("parameter", " " + new JSONObject(postParam));


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Config.checkmobile, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Reposnse", response.toString());

                        progress_dialog.dismiss();


                        try {
                            Log.e("Status", response.getString("status"));

                            if (response.getString("status").equals("200")) {


                                String activity = response.getString("activity");

                                if (activity.equals("allow-new")) {

                                    makeJsonObjReq();

                                } else if (activity.equals("allow-login")) {

                                }
                            } else if (response.getString("status").equals("400")) {
                                String activity = response.getString("activity");

                                if (activity.equals("email-exist")) {

                                    //  DialogBox.setEmailExits(Registration.this, "This Email Address is Already Registered !");
                                    Toast.makeText(getApplicationContext(), getString(R.string.email_already_register), Toast.LENGTH_SHORT).show();

                                } else if (activity.equals("device-exist")) {
                                    DialogBox.setEmailExits(Registration.this, getString(R.string.mobile_already_register));
                                } else if (activity.equals("inactive")) {
                                    DialogBox.setEmailExits(Registration.this, getString(R.string.mobile_already_register));
                                } else {
                                    Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.error_response), Toast.LENGTH_SHORT);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                DialogBox.setNoData(context, getString(R.string.location_enable), runnable);
            }
            /*Toast.makeText(context, "GPS is disabled!", Toast.LENGTH_LONG).show();*/
            else {

            }
        } else {

        }
    }
}
