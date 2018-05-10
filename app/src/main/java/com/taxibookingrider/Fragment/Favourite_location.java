package com.taxibookingrider.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.taxibookingrider.Activity.Dashboard;
import com.taxibookingrider.Adapter.FavDropDownListAdapter;
import com.taxibookingrider.Adapter.Favourite_locationAdapter;
import com.taxibookingrider.Controller.Config;
import com.taxibookingrider.Controller.DialogBox;
import com.taxibookingrider.Controller.GraduallyTextView;
import com.taxibookingrider.Controller.Progress_dialog;
import com.taxibookingrider.Model.FavLocation;
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
 * Created by Admin on 1/8/2018.
 */

public class Favourite_location extends Fragment {

    RecyclerView fav_list;
    Context context;
    public static ArrayList<FavLocation> array_fav = new ArrayList<>();
    public static Favourite_locationAdapter fav_list_adapter;
    RequestQueue queue;
    //Progress_dialog progress_dialog;
    Pref_Master pref_master;
    static String fav_id;
    String nextkey = "";
    String nextkey1 = "";
    FavDropDownListAdapter favDropDownListAdapter;
    android.support.v7.app.AlertDialog progress_dialog;
    GraduallyTextView txt_load;
    ImageView img_gif;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        context = getActivity();

        TextView headertext = getActivity().findViewById(R.id.headertext);
        headertext.setText(getString(R.string.favourite_location));

        //progress_dialog=new Progress_dialog();
        pref_master = new Pref_Master(context);
        queue = Volley.newRequestQueue(getActivity());

        fav_list = (RecyclerView) view.findViewById(R.id.fav_list);

        array_fav.clear();

        fav_list_adapter = new Favourite_locationAdapter(context, array_fav);
        fav_list_adapter.setLoadMoreListener(new Favourite_locationAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                fav_list.post(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            if (nextkey.equals("")) {
                                Log.e("Null", "Null");
                            } else {
                                makeJsonObjReqRELOAD();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });


        fav_list.setHasFixedSize(true);
        fav_list.setLayoutManager(new LinearLayoutManager(context));
        fav_list.setAdapter(fav_list_adapter);

        makeJsonObjReq();

        return view;
    }

    @Override
    public void onResume() {

        super.onResume();

        if (getView() == null) {
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_BACK) {

                    Intent intent = new Intent(getActivity(), Dashboard.class);
                    startActivity(intent);
                }

                return false;
            }
        });

        MyApplication.getInstance().trackScreenView("Favourite_location");


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
        postParam.put("riderid", pref_master.getUID());

        Log.e("parameter", " " + new JSONObject(postParam));


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Config.getfavlocation, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Reposnse", response.toString());
                       progress_dialog.dismiss();
                        try {
                            Log.e("Status", response.getString("status"));

                            if (response.getString("status").equals("200")) {

                                JSONObject jobj = new JSONObject(String.valueOf(response.getString("data")));

                                JSONArray jsonArray = new JSONArray(jobj.getString("trip"));

                                nextkey = jobj.getString("nextkey");
                                nextkey1 = jobj.getString("nextkey1");


                                if (jsonArray.length() == 0) {
                                    DialogBox.setNoData(context, getString(R.string.no_favourite_location), runnable);
                                }

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jobj1 = jsonArray.getJSONObject(i);
                                    Log.e("Address", jobj1.getString("addrs"));
                                    FavLocation favLocation = new FavLocation("post");
                                    favLocation.setId(jobj1.getString("favid"));
                                    favLocation.setAddress(jobj1.getString("addrs"));
                                    favLocation.setLat(jobj1.getString("lat"));
                                    favLocation.setLng(jobj1.getString("long"));
                                    array_fav.add(favLocation);

                                    fav_id = jobj1.getString("favid");

                                }

                            } else if (response.getString("status").equals("400")) {

                                Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                fav_list_adapter.setMoreDataAvailable(false);
                                fav_list_adapter.notifyDataChanged();
                            }

                            fav_list_adapter.notifyDataSetChanged();


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



    private void makeJsonObjReqRELOAD() throws JSONException {


        array_fav.add(new FavLocation("load"));
        fav_list_adapter.notifyItemInserted(array_fav.size() - 1);


        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("riderid", pref_master.getUID());
        postParam.put("nextkey", nextkey);
        postParam.put("nextkey1", nextkey1);


        Log.e("parameter", " " + new JSONObject(postParam));


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Config.getfavlocation, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Reposnse", response.toString());

                        array_fav.remove(array_fav.size() - 1);

                        try {
                            Log.e("Status", response.getString("status"));

                            if (response.getString("status").equals("200")) {

                                JSONObject jobj = new JSONObject(String.valueOf(response.getString("data")));

                                JSONArray jsonArray = new JSONArray(jobj.getString("trip"));

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jobj1 = jsonArray.getJSONObject(i);
                                    Log.e("Address", jobj1.getString("addrs"));
                                    FavLocation favLocation = new FavLocation("post");
                                    favLocation.setId(jobj1.getString("favid"));
                                    favLocation.setAddress(jobj1.getString("addrs"));
                                    favLocation.setLat(jobj1.getString("lat"));
                                    favLocation.setLng(jobj1.getString("long"));
                                    array_fav.add(favLocation);

                                }


                            } else if (response.getString("status").equals("400")) {

                                Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                fav_list_adapter.setMoreDataAvailable(false);
                                fav_list_adapter.notifyDataChanged();
                            }

                            fav_list_adapter.notifyDataSetChanged();


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
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        jsonObjReq.setTag("POST");
        queue.add(jsonObjReq);
    }



    public void RemoveTrip(final int position, Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        Pref_Master pref_master = new Pref_Master(context);

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
        postParam.put("favid", fav_id);

        Log.e("parameter", " " + new JSONObject(postParam));


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Config.removefavlocation, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Reposnse", response.toString());

                        progress_dialog.dismiss();

                        try {
                            Log.e("Status", response.getString("status"));

                            if (response.getString("status").equals("200")) {

                                array_fav.remove(array_fav.get(position));
                                fav_list_adapter.notifyDataSetChanged();


                            } else if (response.getString("status").equals("400")) {

                                Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_SHORT).show();

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
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }


        };
        jsonObjReq.setTag("POST");
        queue.add(jsonObjReq);

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent i = new Intent(getActivity(), Dashboard.class);
            startActivity(i);
        }
    };


}
