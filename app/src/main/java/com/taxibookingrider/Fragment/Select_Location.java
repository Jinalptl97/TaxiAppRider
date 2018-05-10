package com.taxibookingrider.Fragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.taxibookingrider.Activity.Dashboard;
import com.taxibookingrider.Adapter.Destination_list;
import com.taxibookingrider.Controller.Config;
import com.taxibookingrider.Model.Address;
import com.taxibookingrider.MyApplication;
import com.taxibookingrider.R;
import com.taxibookingrider.Utils.Pref_Master;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Select_Location extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    RecyclerView destination_list;
    Context context;
    public AutoCompleteTextView autotv;
    ArrayList<Address> array_destination = new ArrayList<>();
    Destination_list destinationList;
    ImageView back;
    RelativeLayout appdrawers;
    RelativeLayout chooselocationdialog;
    String locality, city;
    private static final String TAG_RESULT = "predictions";
    ArrayList<Address> names;
    Pref_Master pref_master;
   public static EditText fromaddress;
    Select_Location select_location;
    GoogleApiClient mGoogleApiClient;
    String url;
    private FusedLocationProviderClient mFusedLocationClient;
    View view;
    LocationManager locationManager;
    LocationListener locationListener;
    RequestQueue queue;
    String country;
    CardView card_list;
    Location location1;
   /* String browserKey = "AIzaSyCbUNuuKMdjgFrGS2VS4Bzg95lPnAPpY8E";*/

    String browserKey = "AIzaSyDUly0o7nRCYqMQ43CCHZix2DsFJPu-PcI";
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE=100;

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


                    RelativeLayout maplayout, appdrawer, locationlayout;
                    maplayout = (RelativeLayout) getActivity().findViewById(R.id.maplayout);
                    maplayout.setVisibility(View.VISIBLE);
                    appdrawer = (RelativeLayout) getActivity().findViewById(R.id.appdrawer);
                    appdrawer.setVisibility(View.VISIBLE);

                    locationlayout = (RelativeLayout) getActivity().findViewById(R.id.locationlayout);
                    locationlayout.setVisibility(View.VISIBLE);

                    appdrawers.setVisibility(View.GONE);

                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.remove(Select_Location.this);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    ft.commit();


                    // handle back button's click listener
                }

                return false;
            }
        });

        MyApplication.getInstance().trackScreenView("Select_Location");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_select__location, container, false);


        RelativeLayout maplayout, appdrawer, locationlayout;
        maplayout = (RelativeLayout) getActivity().findViewById(R.id.maplayout);
        maplayout.setVisibility(View.GONE);
        appdrawer = (RelativeLayout) getActivity().findViewById(R.id.appdrawer);
        appdrawer.setVisibility(View.GONE);

        locationlayout = (RelativeLayout) getActivity().findViewById(R.id.locationlayout);
        locationlayout.setVisibility(View.GONE);


        context = getActivity();
        Dashboard.frag = 1;
        destination_list = (RecyclerView) view.findViewById(R.id.destination_list);
        back = (ImageView) view.findViewById(R.id.notify_bar);
        appdrawers = (RelativeLayout) view.findViewById(R.id.appdrawerselectfrag);
        fromaddress = (EditText) view.findViewById(R.id.fromaddress);
        queue = Volley.newRequestQueue(getActivity());
        autotv = (AutoCompleteTextView) view.findViewById(R.id.autotv);
        pref_master = new Pref_Master(getActivity());
        card_list=(CardView)view.findViewById(R.id.card_list);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        Bundle bundle = this.getArguments();

        fromaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoAddress();
            }

        });
        if (bundle != null) {

        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }


        if (mGoogleApiClient.isConnected()) {
        }

         autotv.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 destination_list.setVisibility(View.VISIBLE);
             }
         });

        autotv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    autotv.setHint("");
                }  else {
                    autotv.setHint(R.string.Choose_Destination);
                }
            }
        });
        autotv.addTextChangedListener(new TextWatcher() {



            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                if (s.toString().length() <= 10) {
                    names = new ArrayList<Address>();
                    upDatelist(s.toString() + " " +city );

                }
            }
        });

        appdrawers.setVisibility(View.VISIBLE);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RelativeLayout maplayout, appdrawer, locationlayout;
                maplayout = (RelativeLayout) getActivity().findViewById(R.id.maplayout);
                maplayout.setVisibility(View.VISIBLE);
                appdrawer = (RelativeLayout) getActivity().findViewById(R.id.appdrawer);
                appdrawer.setVisibility(View.VISIBLE);

                locationlayout = (RelativeLayout) getActivity().findViewById(R.id.locationlayout);
                locationlayout.setVisibility(View.VISIBLE);

                chooselocationdialog=(RelativeLayout)getActivity().findViewById(R.id.chooselocationdialog);
                chooselocationdialog.setVisibility(View.VISIBLE);

                appdrawers.setVisibility(View.GONE);

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(Select_Location.this);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                ft.commit();
            }
        });




        locationlayout = (RelativeLayout) getActivity().findViewById(R.id.locationlayout);
        appdrawer = (RelativeLayout) getActivity().findViewById(R.id.appdrawer);
        maplayout = (RelativeLayout) getActivity().findViewById(R.id.maplayout);
        chooselocationdialog=(RelativeLayout)getActivity().findViewById(R.id.chooselocationdialog);
        chooselocationdialog.setVisibility(View.GONE);

        locationlayout.setVisibility(View.GONE);
        appdrawer.setVisibility(View.GONE);
        maplayout.setVisibility(View.GONE);




        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        destination_list.setLayoutManager(layoutManager);


        destinationList = new Destination_list(view, getActivity(), array_destination);
        destination_list.setAdapter(destinationList);


        return view;
    }

    public void setfragment(String title, Fragment fragment) {

        android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }

public void AutoAddress() {
        try {

        LatLngBounds latLngBounds = new LatLngBounds(
        new LatLng(Dashboard.location1.getLatitude(), Dashboard.location1.getLongitude()),
        new LatLng(Dashboard.location1.getLatitude(), Dashboard.location1.getLongitude()));


/* AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
.setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)

.setCountry("IN")
.build();*/


        Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
        .setBoundsBias(latLngBounds)
        .build(getActivity());
       getActivity().startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
// TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
// TODO: Handle the error.
        }
        }


    public void upDatelist(String place) {
        array_destination.clear();
        queue = Volley.newRequestQueue(getActivity());

        String input = "";

        try {
            input = "input=" + URLEncoder.encode(place, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }


        String output = "json";
        String parameter = input + "&types=geocode&sensor=true&key=" + browserKey;

        url = "https://maps.googleapis.com/maps/api/place/autocomplete/"
                + output + "?" + parameter + "&" + "components=country:in";

        // SG for singapore
        // IN for india

        Log.e("URL", url);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jsonData = response.toString();
                        Log.e("Response", jsonData);

                        try {
                            JSONObject object = new JSONObject(response);

                            JSONArray ja = object.getJSONArray(TAG_RESULT);


                            for (int i = 0; i < ja.length(); i++) {
                                JSONObject c = ja.getJSONObject(i);
                                String description = c.getString("description");
                                Address address = new Address();
                                address.setAddress(description);
                                array_destination.add(address);

                            }
                            destination_list.setAdapter(destinationList);
                            destinationList.notifyDataSetChanged();


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

// Add the request to the RequestQueue.
        queue.add(stringRequest);


    }




    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                    Geocoder geocoder = new Geocoder(getActivity());

                    try {
                        List<android.location.Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addressList != null && addressList.size() > 0) {
                            String loc = addressList.get(0).getAddressLine(0);
                            String cou = addressList.get(0).getCountryName();
                            String citys = addressList.get(0).getLocality();
                            if (!loc.isEmpty() && !cou.isEmpty())
                                locality = loc;
                            country = cou;
                            city = citys;
                            fromaddress.setText(loc);
                            pref_master.setFromaddress(loc);

                            Log.e("LocalityMain", locality);
                            Log.e("Country", country);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    getNewlocation();
                }


            }
        });
    }


    public void getNewlocation() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {


                if (location != null) {
                    locationManager.removeUpdates(locationListener);
                    Geocoder geocoder = new Geocoder(getActivity());

                    try {
                        List<android.location.Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addressList != null && addressList.size() > 0) {
                            String loc = addressList.get(0).getAddressLine(0);
                            String cou = addressList.get(0).getCountryName();
                            String citys = addressList.get(0).getLocality();
                            if (!loc.isEmpty() && !cou.isEmpty() && !city.isEmpty())
                                locality = loc;
                            country = cou;
                            city = citys;
                            fromaddress.setText(loc);
                            pref_master.setFromaddress(loc);

                            Log.e("LocalityMain", locality);
                            Log.e("Country", country);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getActivity(), getString(R.string.location_not_found), Toast.LENGTH_LONG).show();
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


        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
