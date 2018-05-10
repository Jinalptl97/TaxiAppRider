package com.taxibookingrider.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import com.taxibookingrider.Controller.Config;
import com.taxibookingrider.Controller.DialogBox;
import com.taxibookingrider.Controller.GraduallyTextView;
import com.taxibookingrider.Controller.Progress_dialog;
import com.taxibookingrider.MyApplication;
import com.taxibookingrider.R;
import com.taxibookingrider.Utils.Pref_Master;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by Admin on 1/9/2018.
 */

public class Ride_rating extends AppCompatActivity {

    Context context = this;
    EditText ratingcomment;
    RequestQueue queue;
    MaterialRatingBar ratingBar;
    RelativeLayout chatbox, rr_cancel_ride;
    Pref_Master pref_master;
    //Progress_dialog progress_dialog;
    float ratings = 0;
    int click = 0;
    android.support.v7.app.AlertDialog progress_dialog;
    GraduallyTextView txt_load;
    ImageView img_gif;



    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.fragment_ride_rating);
        ratingBar = findViewById(R.id.ratingbar);
        chatbox = findViewById(R.id.rr_comment);
        rr_cancel_ride = findViewById(R.id.rr_rating_submit);
        queue = Volley.newRequestQueue(this);
        ratingcomment = findViewById(R.id.ratingcomment);
       // progress_dialog = new Progress_dialog();
        pref_master = new Pref_Master(getApplicationContext());
//        InputMethodManager inputMethodManager = (InputMethodManager)
//                getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);


      /*  setRatingBarStart(ratingBar);*/


        if (ratingcomment.isClickable()) {
            Log.e("parameter", " Click");
        } else {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

        ratingcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInputFromInputMethod(getCurrentFocus().getWindowToken(), 0);

            }
        });


        rr_cancel_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingBar.getRating();
                if (ratingcomment.getText().toString().trim().length() == 0) {
                    DialogBox.setPayby(context, getString(R.string.enter_comment));
                } else {
                    makeJsonObjReq();
                }


            }
        });

        MyApplication.getInstance().trackScreenView("Ride_rating");


    }


    private void makeJsonObjReq() {

        int rating = Math.round(ratingBar.getRating());
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
        postParam.put("driverid", pref_master.getDriverid());
        postParam.put("utype", "Rider");
        postParam.put("riderid", pref_master.getUID());
        postParam.put("comment", ratingcomment.getText().toString());
        postParam.put("star", String.valueOf(rating));

        Log.e("parameter", " " + new JSONObject(postParam));


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Config.addrating, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Reposnse", response.toString());
                        progress_dialog.dismiss();
                        try {
                            Log.e("Status", response.getString("status"));

                            if (response.getString("status").equals("200")) {


                                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();

                                pref_master.setTripid("");
                                Intent i = new Intent(getApplicationContext(), Dashboard.class);
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
                return Config.getHeaderParam(context);
            }


        };
        jsonObjReq.setTag("POST");
        // Adding request to request queue
        queue.add(jsonObjReq);


    }

    private void setRatingBarStart(RatingBar rating_bar) {
       /* LayerDrawable stars = (LayerDrawable) rating_bar.getProgressDrawable();
        stars.getDrawable(2)
                .setColorFilter(getResources().getColor(R.color.profile_update),
                        PorterDuff.Mode.SRC_ATOP); // for filled stars

        stars.getDrawable(0)
                .setColorFilter(getResources().getColor(R.color.gray),
                        PorterDuff.Mode.SRC_ATOP); // for empty stars
*/
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratings = rating;


                if (rating < 3) {
                    ratingcomment.setClickable(true);
                    chatbox.setVisibility(View.VISIBLE);
                } else {
                    ratingcomment.setClickable(false);
                    chatbox.setVisibility(View.GONE);
                }
            }
        });

    }
}
