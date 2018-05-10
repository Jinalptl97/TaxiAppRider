package com.taxibookingrider.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputLayout;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.taxibookingrider.Controller.Config;
import com.taxibookingrider.Controller.GraduallyTextView;
import com.taxibookingrider.Controller.Progress_dialog;
import com.taxibookingrider.MyApplication;
import com.taxibookingrider.R;
import com.taxibookingrider.Utils.Pref_Master;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OTPscreen extends AppCompatActivity {


    RelativeLayout otpsubmit;
    EditText editextotp;
    //Progress_dialog progress_dialog;
    RequestQueue queue;
    Pref_Master pref_master;
    Context context;
    String otp;
    RelativeLayout resend;
    TextInputLayout input_layout_otp;

    android.support.v7.app.AlertDialog progress_dialog;
    GraduallyTextView txt_load;
    ImageView img_gif;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpscreen);
        Intent intent = getIntent();
        final String id = intent.getStringExtra("otp");
        pref_master = new Pref_Master(this);
        resend = (RelativeLayout) findViewById(R.id.skip);
        input_layout_otp=(TextInputLayout)findViewById(R.id.input_layout_otp);

        Log.e("OTP", id);


        editextotp = (EditText) findViewById(R.id.editextotp);
        otpsubmit = (RelativeLayout) findViewById(R.id.next);
        //progress_dialog=new Progress_dialog();
        queue = Volley.newRequestQueue(this);


        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Resend_OTP();
            }
        });

        otpsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editextotp.getText().toString().trim().length() == 0) {
                    input_layout_otp.setError(getResources().getString(R.string.enter_otp));
                } else {

                    if (editextotp.getText().toString().equals(id) || editextotp.getText().toString().equals(otp)) {
                        input_layout_otp.setErrorEnabled(false);
                        Intent i = new Intent(getApplicationContext(), Registration.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    } else {
                        editextotp.setError(getResources().getString(R.string.otp_verify));
                      //  Toast.makeText(getApplicationContext(), "Please Check OTP", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
        MyApplication.getInstance().trackScreenView("OTPscreen");


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


}
