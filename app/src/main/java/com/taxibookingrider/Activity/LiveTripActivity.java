package com.taxibookingrider.Activity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.taxibookingrider.Controller.Config;
import com.taxibookingrider.Controller.DialogBox;
import com.taxibookingrider.Controller.DirectionsJSONParser;
import com.taxibookingrider.Controller.GraduallyTextView;
import com.taxibookingrider.Controller.Progress_dialog;
import com.taxibookingrider.MyApplication;
import com.taxibookingrider.R;
import com.taxibookingrider.Utils.Pref_Master;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiveTripActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    MapFragment mapFragment;
    GoogleApiClient mGoogleApiClient;
    Pref_Master pref_master;
    GoogleMap map;
    Float bearing;
    LatLng latlngOne;
    Location location, locationss;
    int markercount = 0;
    Context context = this;
    Marker mk = null;
    RequestQueue queue;
    String tripid, riderid, fare, flag, km, eta, coupon, cprate, total, flong, flat, rname, cncharge, flagval, driverid, tlat, tlong;
    //Progress_dialog progress_dialog;
    android.support.v7.app.AlertDialog progress_dialog;
    GraduallyTextView txt_load;
    ImageView img_gif;
    LatLng latLng;
    RelativeLayout sharelayout, soslayout;

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

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_trip);

        mapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));
        pref_master = new Pref_Master(getApplicationContext());
        queue = Volley.newRequestQueue(this);
        sharelayout = findViewById(R.id.sharelayout);
        soslayout = findViewById(R.id.soslayout);
        //progress_dialog=new Progress_dialog();


        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            DialogBox.setNoData(context, getString(R.string.location_enable), runnable);
        } else {

        }


        sharelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String urll = "http://maps.google.com/?q=" + location.getLatitude() + "," + location.getLongitude();
                Intent sendIntent = new Intent();
                sendIntent.setType("text/plain");
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, urll);
                startActivity(sendIntent);

            }
        });

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        MyApplication.getInstance().trackScreenView("LiveTripActivity");



    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


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
        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location!=null){
            makeJsonObjReq();
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void live() {


        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;

                FirebaseDatabase.getInstance().getReference("cars").child("driverDetail").child(pref_master.getDriverid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Double lat = Double.parseDouble("" + dataSnapshot.child("lat").getValue());
                        Double log = Double.parseDouble("" + dataSnapshot.child("lng").getValue());
                        bearing = Float.parseFloat(" " + dataSnapshot.child("bearing").getValue());

                        locationss = new Location("");
                        locationss.setLatitude(lat);
                        locationss.setLongitude(log);
                        latlngOne = new LatLng(lat, log);

                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlngOne, 18);
                        map.animateCamera(cameraUpdate);


                        if (markercount == 0) {
                            if (dataSnapshot.child("car").getValue().equals("4 Seater")) {
                                mk = map.addMarker(new MarkerOptions()
                                        .position(new LatLng(lat, log))
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_pin)));

                            } else if (dataSnapshot.child("car").getValue().equals("6 Seater")) {
                                mk = map.addMarker(new MarkerOptions()
                                        .position(new LatLng(lat, log))
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_pin)));
                            }
                            map.setMapStyle(
                                    MapStyleOptions.loadRawResourceStyle(
                                            getApplicationContext(), R.raw.map_style));

                            markercount = 1;
                        } else if (markercount == 1) {
                            animateMarker(locationss, mk, bearing);
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });

    }


    public static void animateMarker(final Location destination, final Marker marker, final float Bearing) {
        if (marker != null) {
            final LatLng startPosition = marker.getPosition();
            final LatLng endPosition = new LatLng(destination.getLatitude(), destination.getLongitude());

            final float startRotation = marker.getRotation();

            final LatLngInterpolator latLngInterpolator = new LatLngInterpolator.LinearFixed();
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(1000); // duration 1 second
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        float v = animation.getAnimatedFraction();
                        LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                        marker.setPosition(newPosition);
                        marker.setRotation(computeRotation(v, startRotation, Bearing));
                        marker.setFlat(true);
                    } catch (Exception ex) {
                        // I don't care atm..
                    }
                }
            });

            valueAnimator.start();
        }
    }

    private interface LatLngInterpolator {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class LinearFixed implements LatLngInterpolator {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;
                // Take the shortest path across the 180th meridian.
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }
    }

    private static float computeRotation(float fraction, float start, float end) {
        float normalizeEnd = end - start; // rotate start to 0
        float normalizedEndAbs = (normalizeEnd + 360) % 360;

        float direction = (normalizedEndAbs > 180) ? -1 : 1; // -1 = anticlockwise, 1 = clockwise
        float rotation;
        if (direction > 0) {
            rotation = normalizedEndAbs;
        } else {
            rotation = normalizedEndAbs - 360;
        }

        float result = fraction * rotation + start;
        return (result + 360) % 360;
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
        postParam.put("tripid", pref_master.getTripid());
        postParam.put("riderid", pref_master.getUID());


        Log.e("parameter", " " + new JSONObject(postParam));


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Config.tripinfo, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("ReposnseGetTripINfo", response.toString());
                       progress_dialog.dismiss();
                        try {
                            Log.e("Status", response.getString("status"));

                            if (response.getString("status").equals("200")) {


                                JSONArray jsonArray = new JSONArray(response.getString("data"));

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jobj1 = jsonArray.getJSONObject(i);

                                    tripid = jobj1.getString("tripid");
                                    riderid = jobj1.getString("riderid");
                                    fare = jobj1.getString("fare");
                                    flag = jobj1.getString("flag");
                                    flagval = jobj1.getString("flagval");
                                    km = jobj1.getString("km");
                                    eta = jobj1.getString("eta");
                                    coupon = jobj1.getString("coupon");
                                    cprate = jobj1.getString("cprate");
                                    total = jobj1.getString("total");
                                    flat = jobj1.getString("flat");
                                    flong = jobj1.getString("flong");
                                    rname = jobj1.getString("rname");
                                    cncharge = jobj1.getString("cncharge");
                                    driverid = jobj1.getString("driverid");
                                    tlat = jobj1.getString("tlat");
                                    tlong = jobj1.getString("tlong");
                                    live();

                                    latLng = new LatLng(Double.parseDouble(tlat), Double.parseDouble(tlong));


                                /*    String url = getMapsApiDirectionsUrl(latLng, new LatLng(location.getLatitude(), location.getLongitude()));
                                    ReadTask downloadTask = new ReadTask();
                                    // Start downloading json data from Google Directions API
                                    downloadTask.execute(url);*/

                                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                                        @Override
                                        public void onMapReady(GoogleMap googleMap) {

                                            googleMap.addMarker(new MarkerOptions()
                                                    .position(new LatLng(Double.parseDouble(tlat), Double.parseDouble(tlong)))
                                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.des_marker)));

                                            googleMap.addMarker(new MarkerOptions()
                                                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.des_marker)));


                                        }
                                    });


                                }


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

    private String getMapsApiDirectionsUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        Log.e("URL", "" + url);

        return url;
    }


    private class ReadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            // TODO Auto-generated method stub
            String data = "";
            try {
                MapHttpConnection http = new MapHttpConnection();
                data = http.readUr(url[0]);


            } catch (Exception e) {
                // TODO: handle exception
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }

    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";


            if (result.size() < 1) {


                map.clear();
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
                onConnected(null);
                return;


            } else {

                if (result.size() > 1) {

                    progress_dialog.dismiss();
                }

                // Traversing through all the routes
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        if (j == 0) {    // Get distance from the list
                            distance = (String) point.get("distance");
                            pref_master.setDistance(distance);
                            continue;
                        } else if (j == 1) { // Get duration from the list
                            duration = (String) point.get("duration");


                            String[] separated = duration.split(" ");
                            separated[0] = separated[0].trim();

                            Log.e("Distancesss", separated[0]);
                            pref_master.setTripeta(separated[0]);
                            continue;
                        }

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(5);
                    lineOptions.color(Color.BLACK);

                }


                Log.e("Distancebetween", " " + distance);
                map.addPolyline(lineOptions);



            }


        }
    }


    public class MapHttpConnection {
        public String readUr(String mapsApiDirectionsUrl) throws IOException {
            String data = "";
            InputStream istream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(mapsApiDirectionsUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                istream = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(istream));
                StringBuffer sb = new StringBuffer();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                data = sb.toString();
                br.close();


            } catch (Exception e) {
                Log.d("Exception reading url", e.toString());
            } finally {
                istream.close();
                urlConnection.disconnect();
            }
            return data;

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
}
