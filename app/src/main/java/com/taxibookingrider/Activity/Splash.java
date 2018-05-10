package com.taxibookingrider.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.taxibookingrider.BuildConfig;
import com.taxibookingrider.Controller.Config;
import com.taxibookingrider.Controller.Constants;
import com.taxibookingrider.Controller.DialogBox;
import com.taxibookingrider.Controller.GraduallyTextView;
import com.taxibookingrider.Controller.Progress_dialog;
import com.taxibookingrider.MyApplication;
import com.taxibookingrider.R;
import com.taxibookingrider.Utils.Constant;
import com.taxibookingrider.Utils.Pref_Master;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

public class Splash extends AppCompatActivity {

    ImageView img;
    Pref_Master pref_master;
    Context context = this;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 55;
    int splash = 0;
    RequestQueue queue;
  /*  Progress_dialog progress_dialog;*/
    String android_id;
    int location = 0;
    String version;
    String abc="";
    android.support.v7.app.AlertDialog progress_dialog;
    GraduallyTextView txt_load;
    ImageView img_gif;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);



        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
      /*  progress_dialog=new Progress_dialog();*/
        pref_master = new Pref_Master(context);
        img = (ImageView) findViewById(R.id.img);
        queue = Volley.newRequestQueue(this);

        android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Log.e("DeviceID", android_id);

        checkAndRequestPermissions();

        getSettings();

        MyApplication.getInstance().trackScreenView("Splash");

    }


    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }


        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.e("DataRequest", "" + requestCode);
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {

            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++) {

                    if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            Log.e("msg", "location granted");

                            final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

                            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                                DialogBox.setNoData(context, "Location is Disabled Please Enable The Location", runnable);
                            } else {
                                makeJsonObjReq();
                            }

                        } else {
                            finish();
                            Log.e("msg", "location not granted");
                        }
                    }
                }
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("Result Code", "" + requestCode);
        if (requestCode == 123) {
            final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                DialogBox.setNoData(context, "Location is Disabled Please Enable The Location", runnable);
            }
            /*Toast.makeText(context, "GPS is disabled!", Toast.LENGTH_LONG).show();*/
            else {

                makeJsonObjReq();

            }
        } else {
            finish();
        }
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            Intent callGPSSettingIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(callGPSSettingIntent, 123);
        }
    };


    private void getSettings() {


        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("settingtype", "all");

        Log.e("parameter", " " + new JSONObject(postParam));


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Config.getSetting, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Reposnse_GetSetting", response.toString());

                        try {
                            Log.e("Status", response.getString("status"));

                            if (response.getString("status").equals("200")) {


                                JSONArray jsonArray = new JSONArray(response.getString("data"));

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jobj1 = jsonArray.getJSONObject(i);
                                    String sid = jobj1.getString("sid");

                                    if (sid.equals("1")) {

                                        Constants.BUCKET_NAME = jobj1.getString("sval");

                                    }else if(sid.equals("2"))
                                    {
                                        Constants.ACCESSKEY = jobj1.getString("sval");
                                    } else if(sid.equals("3")) {
                                        Constants.SECRETKEY = jobj1.getString("sval");
                                    }else if (sid.equals("4")) {
                                        Constants.COGNITO_POOL_ID = jobj1.getString("sval");
                                    } else if (sid.equals("6")) {

                                        Constants.COGNITO_POOL_REGION = jobj1.getString("sval");
                                        Constants.BUCKET_REGION = jobj1.getString("sval");

                                    } else if (sid.equals("7")) {
                                        pref_master.setUserfolder(jobj1.getString("sval"));
                                    } else if (sid.equals("8")) {
                                        pref_master.setAwsurl(jobj1.getString("sval"));
                                    } else if (sid.equals("10")) {
                                        pref_master.setDefaultcancel(jobj1.getString("sval"));
                                    }else if(sid.equals("20"))
                                    {
                                        Constants.MOBILE_LENGTH=jobj1.getString("sval");
                                    }else if(sid.equals("21"))
                                    {
                                        Constants.FOURSETER=jobj1.getString("snm");
                                    }else if(sid.equals("22"))
                                    {
                                        Constants.SIXSETER=jobj1.getString("snm");
                                    }else if(sid.equals("23"))
                                    {
                                        Constants.RENTAL=jobj1.getString("snm");
                                    }


                                }

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
                VolleyLog.e("Error", "Error: " + error.getMessage());

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Config.getHeaderParam(context);
            }
        };
        jsonObjReq.setTag("POST");
        queue.add(jsonObjReq);

    }



    private void makeJsonObjReq() {
       /* progress_dialog.show(getSupportFragmentManager(),"");*/
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
        postParam.put("deviceid", android_id);

        Log.e("parameter", " " + new JSONObject(postParam));
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Config.checkversion, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Reposnse", response.toString());


                        try {
                            Log.e("Status", response.getString("status"));

                            if (response.getString("status").equals("200")) {

                                JSONArray jsonArray = new JSONArray(response.getString("data"));

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jobj1 = jsonArray.getJSONObject(i);
                                    version = jobj1.getString("version");
                                }

                                String versionCode = String.valueOf(BuildConfig.VERSION_NAME);

                                if (versionCode.equals(version)) {

                                    if (pref_master.getTripid().equals("")) {
                                        progress_dialog.dismiss();
                                        if (pref_master.getUID().equals("0")) {
                                            abc = (pref_master.getLanguage());
                                            new Config().Change_Language(context, pref_master.getLanguage().equals("ar") ? "1" : "0");
                                            pref_master.setLanguage(abc);
                                            Intent i = new Intent(getApplicationContext(), First_Screen.class);
                                            i.putExtra("splash", splash);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(i);

                                        } else {
                                            abc = (pref_master.getLanguage());
                                            new Config().Change_Language(context, pref_master.getLanguage().equals("ar") ? "1" : "0");
                                            pref_master.setLanguage(abc);
                                            Intent i = new Intent(getApplicationContext(), Dashboard.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(i);

                                        }

                                    } else {
                                        FirebaseDatabase.getInstance().getReference("cars").child("trip").child(pref_master.getTripid()).child("tripPage").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                if (dataSnapshot.getValue() == null) {
                                                   /*progress_dialog.dismiss();*/
                                                    if (pref_master.getUID().equals("0")) {
                                                        abc = (pref_master.getLanguage());
                                                        new Config().Change_Language(context, pref_master.getLanguage().equals("ar") ? "1" : "0");
                                                        pref_master.setLanguage(abc);
                                                        Intent i = new Intent(getApplicationContext(), First_Screen.class);
                                                        i.putExtra("splash", splash);
                                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(i);

                                                    } else {
                                                        abc = (pref_master.getLanguage());
                                                        new Config().Change_Language(context, pref_master.getLanguage().equals("ar") ? "1" : "0");
                                                        pref_master.setLanguage(abc);
                                                        Intent i = new Intent(getApplicationContext(), Dashboard.class);
                                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(i);

                                                    }
                                                } else if (dataSnapshot.getValue().equals("1")) {
                                                    abc = (pref_master.getLanguage());
                                                    new Config().Change_Language(context, pref_master.getLanguage().equals("ar") ? "1" : "0");
                                                    pref_master.setLanguage(abc);
                                                    progress_dialog.dismiss();
                                                    DialogBox.setAccepted(context, getString(R.string.driver_accept_trip));
                                                } else if (dataSnapshot.getValue().equals("2")) {
                                                    abc = (pref_master.getLanguage());
                                                    new Config().Change_Language(context, pref_master.getLanguage().equals("ar") ? "1" : "0");
                                                    pref_master.setLanguage(abc);
                                                    progress_dialog.dismiss();
                                                    DialogBox.setStartTrip(context, getString(R.string.trip_started));
                                                } else if (dataSnapshot.getValue().equals("3")) {
                                                    abc = (pref_master.getLanguage());
                                                    new Config().Change_Language(context, pref_master.getLanguage().equals("ar") ? "1" : "0");
                                                    pref_master.setLanguage(abc);
                                                  progress_dialog.dismiss();
                                                    DialogBox.setEndTrip(context, getString(R.string.trip_end));
                                                } else if (dataSnapshot.getValue().equals("4")) {
                                                   progress_dialog.dismiss();
                                                    abc = (pref_master.getLanguage());
                                                    new Config().Change_Language(context, pref_master.getLanguage().equals("ar") ? "1" : "0");
                                                    pref_master.setLanguage(abc);
                                                    Intent i = new Intent(getApplicationContext(), Ride_rating.class);
                                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(i);
                                                } else {
                                                  progress_dialog.dismiss();
                                                    if (pref_master.getUID().equals("0")) {
                                                        abc = (pref_master.getLanguage());
                                                        new Config().Change_Language(context, pref_master.getLanguage().equals("ar") ? "1" : "0");
                                                        pref_master.setLanguage(abc);
                                                        Intent i = new Intent(getApplicationContext(), First_Screen.class);
                                                        i.putExtra("splash", splash);
                                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(i);

                                                    } else {
                                                        abc = (pref_master.getLanguage());
                                                        new Config().Change_Language(context, pref_master.getLanguage().equals("ar") ? "1" : "0");
                                                        pref_master.setLanguage(abc);
                                                        Intent i = new Intent(getApplicationContext(), Dashboard.class);
                                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(i);

                                                    }

                                                }
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }


                                } else {
                                   progress_dialog.dismiss();
                                    DialogBox.setUpdateApp(context,getString(R.string.update_app));
                                }

                            } else if (response.getString("status").equals("400")) {
                               progress_dialog.dismiss();
                                DialogBox.setPaybyRegistor(context, getString(R.string.loginother));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            /*   progress_dialog.dismiss();*/
                VolleyLog.e("Error", "Error: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                return Config.getHeaderParam(context);
            }
        };
        jsonObjReq.setTag("POST");
        queue.add(jsonObjReq);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }


}