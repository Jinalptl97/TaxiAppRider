package com.taxibookingrider.Fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.taxibookingrider.Activity.Profile;
import com.taxibookingrider.Activity.ResetPasswordOTPscreen;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordUpdateprofileold extends AppCompatActivity {


    EditText enterpassword;
    RelativeLayout enter, skip;
    LinearLayout enterwhole;
    RequestQueue queue;
    Pref_Master pref_master;
    //Progress_dialog progress_dialog;
    Context context = this;
    String otpcheck;
    private String otp;
    TextInputLayout input_new_password1;
    android.support.v7.app.AlertDialog progress_dialog;
    GraduallyTextView txt_load;
    ImageView img_gif;


    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), Profile.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        setContentView(R.layout.fragment_change_password_updateprofileold);

        enterpassword = (EditText) findViewById(R.id.enterpassword);

        enter = (RelativeLayout) findViewById(R.id.enter);
        skip = (RelativeLayout) findViewById(R.id.skip);
        enterwhole = (LinearLayout) findViewById(R.id.wholepassword);
        pref_master = new Pref_Master(this);
       // progress_dialog=new Progress_dialog();
        input_new_password1=(TextInputLayout)findViewById(R.id.input_new_password1);
        otpcheck = "no";

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (enterpassword.getText().toString().trim().length() == 0) {
                    input_new_password1.setError(getString(R.string.enter_old_password));
                } else if (enterpassword.getText().toString().trim().length() < 6) {
                    input_new_password1.setError(getString(R.string.error_password_length));
                } else if (pref_master.getPassword().equals(enterpassword.getText().toString())) {
                    makeJsonObjReq();
                } else {
                    input_new_password1.setError(getString(R.string.invalid_old_password));
                }

            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Resend_OTP();
            }
        });
        MyApplication.getInstance().trackScreenView("ChangePasswordUpdateprofileold");


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


    private void makeJsonObjReq() {

        queue = Volley.newRequestQueue(getApplicationContext());

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
        postParam.put("mobile", pref_master.getUser_number());
        postParam.put("password", enterpassword.getText().toString());
        postParam.put("utype", "Rider");
        postParam.put("reason", "change-password");
        postParam.put("otpcheck", otpcheck);


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

                                Intent i = new Intent(getApplicationContext(), EnterPasswordUpdateprofile.class);
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

}
