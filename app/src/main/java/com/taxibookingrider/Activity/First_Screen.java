package com.taxibookingrider.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.taxibookingrider.Controller.Config;
import com.taxibookingrider.Controller.Constants;
import com.taxibookingrider.Controller.GraduallyTextView;
import com.taxibookingrider.Controller.Progress_dialog;
import com.taxibookingrider.MyApplication;
import com.taxibookingrider.R;
import com.taxibookingrider.Utils.Constant;
import com.taxibookingrider.Utils.Pref_Master;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class First_Screen extends AppCompatActivity {

    RelativeLayout next;
    Context context = this;
    LinearLayout whole;
    Pref_Master pref_master;
    LinearLayout relativeLayout;
    int splash = 0;
    EditText edit_mobile;
    String android_id;
    android.support.v7.app.AlertDialog progress_dialog;
    GraduallyTextView txt_load;
    ImageView img_gif;
    RequestQueue queue;
    TextInputLayout input_layout_mobile;
    int Maxlength;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first__screen);

        next = (RelativeLayout) findViewById(R.id.next);
        whole = (LinearLayout) findViewById(R.id.whole);
        pref_master = new Pref_Master(context);
        input_layout_mobile=(TextInputLayout)findViewById(R.id.input_layout_mobile);
        relativeLayout = (LinearLayout) findViewById(R.id.whole);
        edit_mobile = (EditText) findViewById(R.id.edit_mobile);
      // progress_dialog=new Progress_dialog();
        queue = Volley.newRequestQueue(this);

        android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);


        Maxlength= Integer.parseInt(Constants.MOBILE_LENGTH);
        edit_mobile.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Maxlength)});

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            splash = (Integer) bd.get("splash");
        }


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_mobile.getText().toString().trim().length() == 0) {
                    input_layout_mobile.setError(getResources().getString(R.string.enter_mobile_number));
                } else if (edit_mobile.getText().toString().trim().length() < Maxlength) {
                    input_layout_mobile.setError(getResources().getString(R.string.enter_mobile_8_charcters));
                } else {
                    input_layout_mobile.setErrorEnabled(false);
                    makeJsonObjReq();
                }
            }
        });

        MyApplication.getInstance().trackScreenView("First_Screen");

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
        postParam.put("mobile", edit_mobile.getText().toString());
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

                                    Toast.makeText(getApplicationContext(), getString(R.string.otp_message), Toast.LENGTH_SHORT).show();

                                    pref_master.setUser_number(edit_mobile.getText().toString());
                                    Intent i = new Intent(getApplicationContext(), OTPscreen.class);
                                    i.putExtra("otp", response.getString("OTP"));
                                    startActivity(i);

                                } else if (activity.equals("allow-login")) {
                                    pref_master.setUser_number(edit_mobile.getText().toString());
                                    startActivity(new Intent(getApplicationContext(), Password.class));
                                }

                            } else if (response.getString("status").equals("400")) {

                                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();

                                String activity = response.getString("activity");
                                if (activity.equals("device-exist")) {
                                    pref_master.setUser_number(edit_mobile.getText().toString());
                                    Intent i = new Intent(getApplicationContext(), Change_password_for_newuser.class);
                                    startActivity(i);
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
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                return Config.getHeaderParam(context);
            }
        };
        jsonObjReq.setTag("POST");
        queue.add(jsonObjReq);
    }


    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {

            try {
                Intent intent = new Intent();
                String manufacturer = android.os.Build.MANUFACTURER;
                if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                    intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                    intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
                } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                    intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                } else if ("huawei".equalsIgnoreCase(manufacturer)) {
                    intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
                }

                List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                if (list.size() > 0) {
                    startActivity(intent);
                }
            } catch (Exception e) {

            }
        }
    };


}
