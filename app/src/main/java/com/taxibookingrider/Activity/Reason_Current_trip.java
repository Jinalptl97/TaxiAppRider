package com.taxibookingrider.Activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.taxibookingrider.Adapter.Reason_adapter;
import com.taxibookingrider.Controller.Config;
import com.taxibookingrider.Controller.DialogBox;
import com.taxibookingrider.Controller.GraduallyTextView;
import com.taxibookingrider.Controller.Progress_dialog;
import com.taxibookingrider.Controller.TahomaBold;
import com.taxibookingrider.Model.Chat;
import com.taxibookingrider.Model.Reason;
import com.taxibookingrider.MyApplication;
import com.taxibookingrider.R;
import com.taxibookingrider.Utils.Pref_Master;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 1/9/2018.
 */

public class Reason_Current_trip extends AppCompatActivity {

    ImageView popup;
    Context context = this;
    LinearLayout ll_des_item;
    RecyclerView reason_list;
    Reason_adapter adapter;
    ArrayList<Reason> arrayList = new ArrayList<>();
    RelativeLayout submitcancelrequest;
    int value = 0;
    String tripid;
    RequestQueue queue;
   // Progress_dialog progress_dialog;
    TahomaBold amount;
    ImageView notify_bar;
    JSONArray jsonArray;
    JSONObject object;
    ArrayList<Chat> chatArrayList = new ArrayList<>();
    Pref_Master pref_master;
    android.support.v7.app.AlertDialog progress_dialog;
    GraduallyTextView txt_load;
    ImageView img_gif;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_reason_current_trip);
        pref_master = new Pref_Master(this);
        queue = Volley.newRequestQueue(this);
        popup = (ImageView) findViewById(R.id.popup);
       // progress_dialog=new Progress_dialog();
        submitcancelrequest = findViewById(R.id.submitcancelrequest);
        amount = findViewById(R.id.amount);
        amount.setText(pref_master.getDefaultcancel());
        Intent intent = getIntent();
        notify_bar = findViewById(R.id.notify_bar);
        tripid = intent.getStringExtra("tripid");
        popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value == 0) {
                    ll_des_item.setVisibility(View.VISIBLE);
                }

            }
        });

        ll_des_item = (LinearLayout) findViewById(R.id.ll_des_item);

        notify_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        reason_list = (RecyclerView) findViewById(R.id.reason_list);
        int numberOfColumns = 1;
        reason_list.setLayoutManager(new GridLayoutManager(context, numberOfColumns));

        adapter = new Reason_adapter(context, arrayList);
        reason_list.setAdapter(adapter);

        getMessage();
        prepareMovieData();

        submitcancelrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Reason_adapter.reason != null) {
                    if (Reason_adapter.reason.equals("")) {
                        DialogBox.setPayby(context, getString(R.string.select_reason));
                    } else {
                        makeJsonObjReq();
                    }
                } else {
                    DialogBox.setPayby(context, getString(R.string.select_reason));
                }


            }
        });


        MyApplication.getInstance().trackScreenView("Reason_Current_trip");

    }


    public void makeJsonObjReq() {

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
        postParam.put("reason", Reason_adapter.reason);
        postParam.put("cancelamt", pref_master.getDefaultcancel());
        postParam.put("tripid", tripid);
        postParam.put("driverid", pref_master.getDriverid());
        postParam.put("dtoken", pref_master.getDrivertoken());


        Log.e("parameter", " " + new JSONObject(postParam));

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Config.canceltrip, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Reposnse", response.toString());
                        progress_dialog.dismiss();
                        try {
                            Log.e("Status", response.getString("status"));

                            if (response.getString("status").equals("200")) {



                                NotificationManager nManager = ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
                                nManager.cancelAll();

                                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("cars").child("trip").child(pref_master.getTripid());
                                reference.removeValue();

                                pref_master.setTripid("");
                                Reason_adapter.reason = "";

                                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                                makeJsonObjReq22();


                            } else {
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


    public void makeJsonObjReq22() throws JSONException {

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


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("tripid", tripid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject2 = new JSONObject();
        try {
            jsonObject2.put("riderid", pref_master.getUID());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONObject jsonObject3 = new JSONObject();
        try {
            jsonObject3.put("driverid", pref_master.getDriverid());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Map<String, JSONObject> postParam = new HashMap<String, JSONObject>();
        postParam.put("tripid", jsonObject);
        postParam.put("riderid", jsonObject2);
        postParam.put("driverid", jsonObject3);

        jsonArray = new JSONArray();


        for (int i = 0; i < chatArrayList.size(); i++) {
            object = new JSONObject();
            Log.e("MessageCount", "" + chatArrayList.size());
            Log.e("MessageData", "" + chatArrayList.get(i).getMessage());

            object.put("m", chatArrayList.get(i).getMessage());
            object.put("s", chatArrayList.get(i).getSender());
            object.put("r", chatArrayList.get(i).getReceiver());
            object.put("t", chatArrayList.get(i).getTimestamp());
            jsonArray.put(object);
        }
        JSONObject msg = new JSONObject();
        try {
            msg.put("messages", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        postParam.put("messagearray", msg);

        Log.e("parameter", " " + new JSONObject(postParam));

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Config.addmsg, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Reposnse", response.toString());
                        progress_dialog.dismiss();
                        try {
                            Log.e("Status", response.getString("status"));

                            if (response.getString("status").equals("200")) {

                                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(), Dashboard.class);
                                startActivity(i);

                            } else {
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
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Config.getHeaderParam(context);
            }
        };
        jsonObjReq.setTag("POST");
        queue.add(jsonObjReq);

    }

    private void prepareMovieData() {

        Reason reason = new Reason("Waiting Too Long", "0");
        arrayList.add(reason);

        reason = new Reason("Driver Asked", "0");
        arrayList.add(reason);

        reason = new Reason("Found Another", "0");
        arrayList.add(reason);
    }

    private void getMessage() {
        Log.e("getMessage", tripid);
        FirebaseDatabase.getInstance().getReference("cars").child("trip").child(tripid).child("msg").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot chilSnapshot : dataSnapshot.getChildren()) {

                    Chat chat = new Chat();
                    chat.setMessage(String.valueOf(chilSnapshot.child("message").getValue()));
                    chat.setReceiver(String.valueOf(chilSnapshot.child("receiver").getValue()));
                    chat.setSender(String.valueOf(chilSnapshot.child("sender").getValue()));
                    chat.setTimestamp(Long.parseLong(String.valueOf(chilSnapshot.child("timestamp").getValue())));
                    chatArrayList.add(chat);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
