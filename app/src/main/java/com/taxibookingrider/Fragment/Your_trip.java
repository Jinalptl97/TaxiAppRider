package com.taxibookingrider.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.taxibookingrider.Activity.Dashboard;
import com.taxibookingrider.Adapter.Your_tripAdapter;
import com.taxibookingrider.Controller.Config;
import com.taxibookingrider.Controller.DialogBox;
import com.taxibookingrider.Controller.GraduallyTextView;
import com.taxibookingrider.Controller.Progress_dialog;
import com.taxibookingrider.Model.YouTripModel;
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

public class Your_trip extends Fragment {

    RecyclerView your_trip_list;
    Context context;
    ArrayList<YouTripModel> array_trip = new ArrayList<>();
    Your_tripAdapter your_tripAdapter;
   // Progress_dialog progress_dialog;
    Pref_Master pref_master;
    RequestQueue queue;
    String nextkey;
    android.support.v7.app.AlertDialog progress_dialog;
    GraduallyTextView txt_load;
    ImageView img_gif;




    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_your_trip, container, false);
        context = getActivity();

        TextView headertext = getActivity().findViewById(R.id.headertext);
        headertext.setText("Your Trip");

        pref_master = new Pref_Master(context);
        Log.e("trip number","::"+ pref_master.getTripid());
        your_trip_list = (RecyclerView) view.findViewById(R.id.your_trip_list);
        //progress_dialog=new Progress_dialog();
        queue = Volley.newRequestQueue(context);

        your_tripAdapter = new Your_tripAdapter(context, array_trip);


        your_tripAdapter.setLoadMoreListener(new Your_tripAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                your_trip_list.post(new Runnable() {
                    @Override
                    public void run() {

                        if (nextkey.equals("")) {

                        } else {
                            makeJsonObjReqRELOAD();
                        }

                    }
                });
            }
        });


        your_trip_list.setHasFixedSize(true);
        your_trip_list.setLayoutManager(new LinearLayoutManager(context));
        your_trip_list.setAdapter(your_tripAdapter);

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

                   RelativeLayout maplayout, locationlayout, choosedialog;
                    maplayout = (RelativeLayout) getActivity().findViewById(R.id.maplayout);
                    maplayout.setVisibility(View.VISIBLE);

                    locationlayout = (RelativeLayout) getActivity().findViewById(R.id.locationlayout);
                    locationlayout.setVisibility(View.VISIBLE);

                    choosedialog = (RelativeLayout) getActivity().findViewById(R.id.chooselocationdialog);
                    choosedialog.setVisibility(View.VISIBLE);

                    TextView header = getActivity().findViewById(R.id.headertext);
                    header.setText("Home");


                    Intent intent = new Intent(getActivity(), Dashboard.class);
                        startActivity(intent);

                    /*ImageView toplogo = getActivity().findViewById(R.id.toplogo);
                    toplogo.setVisibility(View.VISIBLE);*/


                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.remove(Your_trip.this);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    ft.commit();
// handle back button's click listener
                }

                return false;
            }
        });

        MyApplication.getInstance().trackScreenView("Your_trip");
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
                Config.ridertriplist, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("ReposnseFirst", response.toString());
                        progress_dialog.dismiss();
                        try {
                            Log.e("Status", response.getString("status"));

                            if (response.getString("status").equals("200")) {



                                JSONObject jobj = new JSONObject(String.valueOf(response.getString("data")));

                                JSONArray jsonArray = new JSONArray(jobj.getString("trip"));

                                if (jsonArray.length() == 0) {

                                }
                                nextkey = jobj.getString("nextkey");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jobj1 = jsonArray.getJSONObject(i);

                                    YouTripModel youTripModel = new YouTripModel("post");
                                    youTripModel.setTripid(jobj1.getString("tripid"));
                                    youTripModel.setDate(jobj1.getString("date"));
                                    youTripModel.setTime(jobj1.getString("time"));
                                    youTripModel.setFrom(jobj1.getString("from"));
                                    youTripModel.setTo(jobj1.getString("to"));
                                    youTripModel.setPayment(jobj1.getString("payment"));
                                    youTripModel.setFare(jobj1.getString("finalfare"));
                                    youTripModel.setCartype(jobj1.getString("cartype"));
                                    youTripModel.setPaymentamt(jobj1.getString("paymentamt"));
                                    youTripModel.setWamt(jobj1.getString("wamt"));
                                    youTripModel.setFlat(jobj1.getString("flat"));
                                    youTripModel.setFlong(jobj1.getString("flong"));
                                    youTripModel.setTlat(jobj1.getString("tlat"));
                                    youTripModel.setTlong(jobj1.getString("tlong"));
                                    youTripModel.setFlag(jobj1.getString("flag"));
                                    array_trip.add(youTripModel);
                                }

                            } else if (response.getString("status").equals("400")) {

                               /* Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_SHORT).show();*/

                                DialogBox.setNoData(context, response.getString("message"), runnable);
                                your_tripAdapter.setMoreDataAvailable(false);
                                your_tripAdapter.notifyDataChanged();
                            }

                            your_tripAdapter.notifyDataSetChanged();


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


    private void makeJsonObjReqRELOAD() {

        array_trip.add(new YouTripModel("load"));
        your_tripAdapter.notifyItemInserted(array_trip.size() - 1);

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("riderid", pref_master.getUID());

        Log.e("parameter", " " + new JSONObject(postParam));


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Config.ridertriplist, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("ReposnseSecond", response.toString());

                        array_trip.remove(array_trip.size() - 1);

                        try {
                            Log.e("Status", response.getString("status"));

                            if (response.getString("status").equals("200")) {

                                JSONObject jobj = new JSONObject(String.valueOf(response.getString("data")));

                                JSONArray jsonArray = new JSONArray(jobj.getString("trip"));
                                nextkey = jobj.getString("nextkey");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jobj1 = jsonArray.getJSONObject(i);

                                    YouTripModel youTripModel = new YouTripModel("post");
                                    youTripModel.setDate(jobj1.getString("date"));
                                    youTripModel.setTime(jobj1.getString("time"));
                                    youTripModel.setFrom(jobj1.getString("from"));
                                    youTripModel.setTo(jobj1.getString("to"));
                                    youTripModel.setPayment(jobj1.getString("payment"));
                                    youTripModel.setFare(jobj1.getString("finalfare"));
                                    youTripModel.setWamt(jobj1.getString("wamt"));
                                    youTripModel.setCartype(jobj1.getString("cartype"));
                                    array_trip.add(youTripModel);

                                }

                            } else if (response.getString("status").equals("400")) {

                                Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                your_tripAdapter.setMoreDataAvailable(false);
                                your_tripAdapter.notifyDataChanged();
                            }

                            your_tripAdapter.notifyDataSetChanged();


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


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent i = new Intent(getActivity(), Dashboard.class);
            startActivity(i);
        }
    };

}
