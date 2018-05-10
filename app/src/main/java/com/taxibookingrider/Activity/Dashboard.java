package com.taxibookingrider.Activity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.services.s3.AmazonS3Client;
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
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.taxibookingrider.Adapter.FavDropDownListAdapter;
import com.taxibookingrider.Controller.Constants;
import com.taxibookingrider.Controller.DialogBox;
import com.taxibookingrider.Controller.DirectionsJSONParser;
import com.taxibookingrider.Controller.GraduallyTextView;
import com.taxibookingrider.Controller.Progress_dialog;
import com.taxibookingrider.Controller.Tahoma;
import com.taxibookingrider.Model.Customer;
import com.taxibookingrider.Controller.Config;
import com.taxibookingrider.Controller.TahomaItalic;
import com.taxibookingrider.Fragment.Favourite_location;
import com.taxibookingrider.Fragment.Legal;
import com.taxibookingrider.Fragment.Select_Location;
import com.taxibookingrider.Fragment.Your_trip;
import com.taxibookingrider.Model.FavLocation;
import com.taxibookingrider.MyApplication;
import com.taxibookingrider.R;
import com.taxibookingrider.Utils.Pref_Master;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.taxibookingrider.Fragment.Select_Location.fromaddress;

public class Dashboard extends AppCompatActivity {


    DrawerLayout drawer;
    RecyclerView list;
    U_NavigationAdapter adapter;
    ImageView notify_bar;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    float bearing;
    String CurrentDate;
    ArrayList<String> array_menu = new ArrayList<>();
    ArrayList<Integer> array_icon = new ArrayList<>();
    SupportMapFragment mapFragment;
    LatLng latlngOne;
    int radius = 2;
    RelativeLayout maplayout;
    public static Location location1;
    static Location locationss;
    GoogleMap map;
    Marker mk = null;
    Pref_Master pref_master;
    Context context = this;
    ArrayList<Customer> arrayList = new ArrayList<>();

    RequestQueue queue;
    RelativeLayout relativeLayout;
    RecyclerView destination_list;
    ImageView popup;
    GoogleApiClient mGoogleApiClient;
    int value = 0;
    int linedraw = 0;
    String locality, country, cartype;
    RelativeLayout ll_des_item, car1, car3;
    LinearLayout car2;
    CircleImageView profileimage;
    Button myLocationButton;
    RelativeLayout chooseloationdialog;
    RelativeLayout locationlayout, appdrawer;
    TextView resutText;
    Tahoma carlayoutsubmit;
    Tahoma chooselocation;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    TextView ridername, rideremail, ridernumber;
    int carclick = 0;
    AmazonS3Client amazonS3;
    FragmentManager manager;
    LocationListener locationListener;
    Bundle extras;
    Marker marker1, marker2;
    int connected = 0;
    LatLng latLng;
    Double lat, lng;
    GeoQuery geoQuery;
    public static int frag = 0;
    RelativeLayout carlayout;
    String key = "";
    ImageView toplogo;
    TextView headertext;
    int nextkey = 0;
    GeoLocation geoLocation;
    LocationManager locationManager;
    ArrayList<FavLocation> array_fav = new ArrayList<>();
    private FusedLocationProviderClient mFusedLocationClient;
    FavDropDownListAdapter favDropDownListAdapter;
    boolean doubleBackToExitPressedOnce = false;
    java.util.Date futureDate;
    RelativeLayout rel_seater;
    //Progress_dialog progress_dialog;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 100;
    ImageView carimgsedan, carimsuv, carimageMini;
    android.support.v7.app.AlertDialog progress_dialog;
    GraduallyTextView txt_load;
    ImageView img_gif;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            if (frag == 1) {
                Intent i = new Intent(getApplicationContext(), Dashboard.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                frag = 0;
            }
            Toast.makeText(this, "Press again to exist", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }


        if (linedraw == 1) {

            chooseloationdialog.setVisibility(View.VISIBLE);
            carlayout.setVisibility(View.GONE);
            if (carlayout.getVisibility() == View.GONE) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                locationlayout.setLayoutParams(params);
            }

            map.clear();
            connected = 1;
            linedraw = 0;
            getLocation();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            final LocationManager managers = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            if (!managers.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                DialogBox.setNoData(context, getString(R.string.location_enable), runnable);
            }
            /*Toast.makeText(context, "GPS is disabled!", Toast.LENGTH_LONG).show();*/
            else {
            }
        } else {

        }

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Log.e("inside the location", ":::");
                Place place = PlaceAutocomplete.getPlace(context, data);
                fromaddress.setText("" + place.getAddress());
                pref_master.setFromaddress("" + place.getAddress());
                pref_master.setFromlocationtrip(place.getLatLng().latitude + "," + place.getLatLng().longitude);

            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu__drawer);

        list = (RecyclerView) findViewById(R.id.list);
        carlayoutsubmit = (Tahoma) findViewById(R.id.carlayoutsubmit);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        notify_bar = (ImageView) findViewById(R.id.notify_bar);
        headertext = findViewById(R.id.headertext);
        chooseloationdialog = (RelativeLayout) findViewById(R.id.chooselocationdialog);
        locationlayout = (RelativeLayout) findViewById(R.id.locationlayout);
        appdrawer = (RelativeLayout) findViewById(R.id.appdrawer);
        pref_master = new Pref_Master(context);
        // progress_dialog=new Progress_dialog();
        chooselocation = (Tahoma) findViewById(R.id.chooselocation);
        ridername = (TextView) findViewById(R.id.ridername);
        rideremail = (TextView) findViewById(R.id.rideremail);
        ridernumber = (TextView) findViewById(R.id.ridernumber);
        carimgsedan = (ImageView) findViewById(R.id.carimg);
        carimsuv = (ImageView) findViewById(R.id.carimg1);
        carimageMini = (ImageView) findViewById(R.id.carimg2);
        car1 = (RelativeLayout) findViewById(R.id.car1);
        car2 = (LinearLayout) findViewById(R.id.car2);
        car3 = (RelativeLayout) findViewById(R.id.car3);
        cartype = Constants.FOURSETER;

        Log.e("the first car type", "::" + Constants.RENTAL);


     /*   toplogo = findViewById(R.id.toplogo);*/
        car2.setBackgroundResource(R.color.reason_color);
       /* rel_seater=(RelativeLayout)findViewById(R.id.rel_seater);*/
        car3.setBackgroundResource(R.color.reason_color);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        getLocation();

        arrayList.clear();

        manager = getSupportFragmentManager();
        queue = Volley.newRequestQueue(this);
        getProfile();
        getSettings();
       /* toplogo.setVisibility(View.VISIBLE);*/
        carlayout = (RelativeLayout) findViewById(R.id.carlayout);
        extras = getIntent().getExtras();


        MyApplication.getInstance().trackScreenView("Dashboard");


        if (carlayout.getVisibility() == View.GONE) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            locationlayout.setLayoutParams(params);
        }

        if (pref_master.getAutostart() == 1) {

        } else {


            try {
                Intent intent1 = new Intent();
                String manufacturer = android.os.Build.MANUFACTURER;
                if ("xiaomi".equalsIgnoreCase(manufacturer)) {

                    DialogBox.setAutoStart(context, getString(R.string.auto), runnable2);

                } else if ("oppo".equalsIgnoreCase(manufacturer)) {

                    DialogBox.setAutoStart(context, getString(R.string.auto), runnable2);

                } else if ("vivo".equalsIgnoreCase(manufacturer)) {

                    DialogBox.setAutoStart(context, getString(R.string.auto), runnable2);

                } else if ("huawei".equalsIgnoreCase(manufacturer)) {

                    DialogBox.setAutoStart(context, getString(R.string.auto), runnable2);
                } else if ("asus".equalsIgnoreCase(manufacturer)) {

                    DialogBox.setAutoStart(context, getString(R.string.auto), runnable2);
                }

                List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent1, PackageManager.MATCH_DEFAULT_ONLY);
                if (list.size() > 0) {
                    startActivity(intent1);
                }
            } catch (Exception e) {

            }

        }
        locationlayout.setVisibility(View.GONE);


        final LocationManager managers = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (!managers.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            DialogBox.setNoData(context, getString(R.string.location_enable), getRunnable);
        } else {

        }



       /* cartype="";*/

        car1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LocationManager managers = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

                if (!managers.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    DialogBox.setNoData(context, getString(R.string.location_enable), getRunnable);
                } else {

                }

                carclick = 1;
                map.clear();
                cartype = Constants.RENTAL;
                Log.e("the car type value", "::" + cartype);
                getLocation();
                car1.setBackgroundResource(R.color.Yellow_header);
                carimageMini.setImageResource(R.drawable.mini_dark);
                car2.setBackgroundResource(R.color.reason_color);
                car3.setBackgroundResource(R.color.reason_color);

            }
        });


        car2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                Log.e("car1111111111111", "::");
                final LocationManager managers = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

                if (!managers.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    DialogBox.setNoData(context, getString(R.string.location_enable), getRunnable);
                } else {

                }

                carclick = 1;
                map.clear();
                cartype = Constants.FOURSETER;
                Log.e("the car type value", "::" + cartype);
                getLocation();
                car2.setBackgroundResource(R.color.Yellow_header);
                carimgsedan.setImageResource(R.drawable.sedan_dark);
                car1.setBackgroundResource(R.color.reason_color);
                car3.setBackgroundResource(R.color.reason_color);


            }
        });

        car3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LocationManager managers = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

                if (!managers.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    DialogBox.setNoData(context, getString(R.string.location_enable), getRunnable);
                } else {

                }

                carclick = 1;
                map.clear();
                cartype = Constants.SIXSETER;
                Log.e("the car type value", "::" + cartype);
                getLocation();
                car3.setBackgroundResource(R.color.Yellow_header);
                carimsuv.setImageResource(R.drawable.suv_dark);
                car1.setBackgroundResource(R.color.reason_color);
                car2.setBackgroundResource(R.color.reason_color);

            }
        });


        carlayoutsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (carclick == 0) {
                    DialogBox.setPayby(Dashboard.this, getString(R.string.choose_car_type));
                } else {
                    carclick = 0;
                    pref_master.setCtype(cartype);
                    car1.setBackgroundResource(R.color.reason_color);
                    car2.setBackgroundResource(R.color.reason_color);
                    car2.setBackgroundResource(R.color.reason_color);
                    Log.e("cartype value", "::" + cartype);

                    Intent i = new Intent(getApplicationContext(), DriverList.class);
                    i.putExtra("cartype", cartype);
                    i.putExtra("driverid", "");
                    i.putExtra("tripid", "0");
                    i.putExtra("rej", "0");
                    startActivity(i);


                }


            }
        });

        WindowManager windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        int height = windowManager.getDefaultDisplay().getHeight();

        Log.e("Width", "" + width);
        Log.e("Height", "" + height);

        /*Toast.makeText(context, "" + height + " " + width, Toast.LENGTH_LONG).show();*/

        if (width <= 480) {
            final float scale = context.getResources().getDisplayMetrics().density;
            int px = (int) (87 * scale + 0.5f);
            int px2 = (int) (50 * scale + 0.5f);

            RelativeLayout rl = (RelativeLayout) findViewById(R.id.car1);
            rl.getLayoutParams().height = px2;
            rl.getLayoutParams().width = px;

            LinearLayout r2 = (LinearLayout) findViewById(R.id.car2);
            r2.getLayoutParams().height = px2;
            r2.getLayoutParams().width = px;

            RelativeLayout r3 = (RelativeLayout) findViewById(R.id.car3);
            r3.getLayoutParams().height = px2;
            r3.getLayoutParams().width = px;

        }


        chooseloationdialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                geoQuery.removeAllListeners();
                Bundle bundle = new Bundle();
                bundle.putString("loc", locality);
                Select_Location fragment = new Select_Location();
                FragmentTransaction transaction = manager.beginTransaction();
                fragment.setArguments(bundle);
                transaction.replace(R.id.frame, fragment);
                transaction.addToBackStack("Select Location");
                transaction.commit();

            }
        });

        notify_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // total_no_order();

                mDrawerLayout.openDrawer(GravityCompat.START);


                   /* mDrawerLayout.openDrawer(Gravity.LEFT);*/


            }
        });

        resutText = (TextView) findViewById(R.id.dragg_result);

        popup = (ImageView) findViewById(R.id.popup);
        popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("the fav list", ":::" + array_fav.size());

                favDropDownListAdapter.notifyDataSetChanged();
                if (value == 0) {
                    favDropDownListAdapter.notifyDataSetChanged();
                    ll_des_item.setVisibility(View.VISIBLE);
                    value = 1;
                } else {
                    ll_des_item.setVisibility(View.GONE);
                    value = 0;
                }
            }
        });
        profileimage = (CircleImageView) findViewById(R.id.profileimage);

        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), Profile.class);
                startActivity(i);
                drawer.closeDrawer(GravityCompat.START);

            }
        });
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            // mDrawerToggle = new ActionBarDrawerToggle(ServiceProvider_home.this, mDrawerLayout, R.string.openDrawer, R.string.openDrawer) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.setDrawerIndicatorEnabled(true);

        adapter = new U_NavigationAdapter();
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(context));
        list.setHasFixedSize(true);

        final int[] size = {array_menu.size()};
        String string = String.valueOf(size[0]);
        boolean arrraaay = Boolean.valueOf(string);

        list.setNestedScrollingEnabled(arrraaay);
        /*frame = (FrameLayout) findViewById(R.id.frame);*/


        /*setfragment("Home", new Home());*/

        ll_des_item = (RelativeLayout) findViewById(R.id.ll_des_item);
        relativeLayout = (RelativeLayout) findViewById(R.id.choosedialog);
        maplayout = (RelativeLayout) findViewById(R.id.maplayout);
        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));

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


        destination_list = (RecyclerView) findViewById(R.id.destination_list);

        array_fav.clear();

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        destination_list.setLayoutManager(layoutManager);

        favDropDownListAdapter = new FavDropDownListAdapter(context, array_fav);
        destination_list.setAdapter(favDropDownListAdapter);

        makeJsonObjReq();

        if (pref_master.getFirstname().equals("") || pref_master.getLastname().equals("")) {
            ridername.setVisibility(View.GONE);
        } else {
            ridername.setText(pref_master.getFirstname() + " " + pref_master.getLastname());
        }

        if (pref_master.getUser_number().equals("0")) {
            ridernumber.setVisibility(View.GONE);
        } else {
            ridernumber.setText(pref_master.getUser_number());
        }

        if (pref_master.getEmail().equals("")) {
            rideremail.setVisibility(View.GONE);
        } else {
            rideremail.setText(pref_master.getEmail());
        }
        Log.e("the data image", "::" + pref_master.getUserimage());
        Log.e("the data image", "::" + pref_master.getUID());
        Log.e("the data image", "::" + pref_master.getAwsurl());

        Picasso.with(context)
                .load(pref_master.getAwsurl() + pref_master.getUID() + "/" + pref_master.getUserimage())
                //extract as User instance method
                .placeholder(R.drawable.personal)
                .into(profileimage);


        myLocationButton = (Button) findViewById(R.id.myLocationButton);
        myLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location1.getLatitude(), location1.getLongitude()), 16);
                map.animateCamera(cameraUpdate, 800, new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onCancel() {

                    }
                });

            }
        });
    }

    public class U_NavigationHolder extends RecyclerView.ViewHolder {

        TextView u_navigation_row_name;
        ImageView image_row;
        LinearLayout ll_click;
        View v;


        public U_NavigationHolder(View itemView) {
            super(itemView);
            this.v = itemView;
            u_navigation_row_name = (TextView) itemView.findViewById(R.id.u_navigation_row_name);
            ll_click = (LinearLayout) itemView.findViewById(R.id.ll_click);
            image_row = (ImageView) itemView.findViewById(R.id.imageicons);
        }
    }

    public class U_NavigationAdapter extends RecyclerView.Adapter<U_NavigationHolder> {


        public U_NavigationAdapter() {
            array_icon.add(R.drawable.dashboard);
            array_icon.add(R.drawable.mytrip);
            array_icon.add(R.drawable.faourite);
            array_icon.add(R.drawable.share);
            array_icon.add(R.drawable.legal);
            array_icon.add(R.drawable.setting);
            array_icon.add(R.drawable.logout);

            array_menu.add(getResources().getString(R.string.dashboard));
            array_menu.add(getResources().getString(R.string.your_trip));
            array_menu.add(getResources().getString(R.string.favourite));
            array_menu.add(getResources().getString(R.string.share_app));
            array_menu.add(getResources().getString(R.string.legal));
            array_menu.add(getResources().getString(R.string.settings));
            array_menu.add(getResources().getString(R.string.logout));


        }

        @Override
        public U_NavigationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new U_NavigationHolder(LayoutInflater.from(context).inflate(R.layout.u_navigation_listrow, null));
        }

        @Override
        public void onBindViewHolder(final U_NavigationHolder holder, final int position) {
            holder.image_row.setImageResource(array_icon.get(position));
            holder.u_navigation_row_name.setText(array_menu.get(position));

            holder.ll_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (position) {

                        case 0:
                            Intent refresh = new Intent(context, Dashboard.class);
                            refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(refresh);
                            ((Activity) context).finish();
                            mDrawerLayout.closeDrawer(Gravity.START);

                            break;

                        case 1:
                            chooseloationdialog.setVisibility(View.GONE);
                            carlayout.setVisibility(View.GONE);
                            locationlayout.setVisibility(View.GONE);
                            maplayout.setVisibility(View.GONE);
                            setfragment("Your Trip", new Your_trip());
                            mDrawerLayout.closeDrawer(Gravity.START);

                            break;

                        case 2:
                            chooseloationdialog.setVisibility(View.GONE);
                            carlayout.setVisibility(View.GONE);
                            locationlayout.setVisibility(View.GONE);
                            maplayout.setVisibility(View.GONE);
                            setfragment("Favourite Location", new Favourite_location());
                            mDrawerLayout.closeDrawer(Gravity.START);
                            break;

                        case 3:
                            String urll = "https://play.google.com/store/apps/details?id=com.taxibookingrider";
                            Intent sendIntent = new Intent();
                            sendIntent.setType("text/plain");
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, urll);
                            startActivity(sendIntent);
                            mDrawerLayout.closeDrawer(Gravity.START);
                            break;


                        case 4:
                            chooseloationdialog.setVisibility(View.GONE);
                            carlayout.setVisibility(View.GONE);
                            locationlayout.setVisibility(View.GONE);
                            maplayout.setVisibility(View.GONE);
                            setfragment("Legal", new Legal());
                            mDrawerLayout.closeDrawer(Gravity.START);
                            break;

                        case 5:

                            LayoutInflater li = LayoutInflater.from(context);
                            View v = li.inflate(R.layout.dialog_language, null);
                            final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();
                            alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            alert.setCancelable(true);

                            final ArrayList<LinearLayout> Arr_ll_cat = new ArrayList<>();
                            final ArrayList<ImageView> Arr_Chk_cat = new ArrayList<>();

                            Arr_ll_cat.add(0, (LinearLayout) v.findViewById(R.id.ll_l_English));
                            Arr_ll_cat.add(1, (LinearLayout) v.findViewById(R.id.ll_l_Arabic));

                            Arr_Chk_cat.add(0, (ImageView) v.findViewById(R.id.chk_l_english));
                            Arr_Chk_cat.add(1, (ImageView) v.findViewById(R.id.chk_l_arabic));

                            TextView okbtn = v.findViewById(R.id.okbtn);
                            TextView cancel = v.findViewById(R.id.cancel);

                            final String[] pos_lang = {"0"};
                            if (pref_master.getLanguage().equals("ar")) {
                                Config.Select_Status(Arr_ll_cat, 1, Arr_Chk_cat);
                                pos_lang[0] = "1";
                            } else {
                                Config.Select_Status(Arr_ll_cat, 0, Arr_Chk_cat);
                                pos_lang[0] = "0";
                            }

                            Arr_ll_cat.get(0).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Config.Select_Status(Arr_ll_cat, 0, Arr_Chk_cat);
                                    pos_lang[0] = "0";
                                }
                            });

                            Arr_ll_cat.get(1).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Config.Select_Status(Arr_ll_cat, 1, Arr_Chk_cat);
                                    pos_lang[0] = "1";
                                }
                            });

                            okbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String languageToLoad = "en";
                                    if (pos_lang[0].equals("1")) {
                                        languageToLoad = "ar";
                                        pref_master.setLanguage("ar");


                                    } else {
                                        languageToLoad = "en";
                                        pref_master.setLanguage("en");
                                    }
                                    Locale locale = new Locale(languageToLoad);
                                    Locale.setDefault(locale);
                                    Configuration config = new Configuration();
                                    config.locale = locale;
                                    context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
                                    alert.dismiss();
                                    Intent refresh = new Intent(context, Dashboard.class);
                                    refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    context.startActivity(refresh);
                                    ((Activity) context).finish();
                                }
                            });
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alert.dismiss();
                                }
                            });
                            mDrawerLayout.closeDrawers();
                            break;


                        case 6:

                            Logout();

                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return array_menu.size();
        }
    }

    public void setfragment(String title, Fragment fragment) {
        /*toplogo.setVisibility(View.GONE);*/
        headertext.setVisibility(View.VISIBLE);
        headertext.setText(title);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }


    public static void animateMarker(final Location destination, final Marker marker, final Float Bearing) {
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


    private void getDriver(final Location location) {


        Log.e("inside driver", "::::");


        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {

                    map = googleMap;
                    map.setOnCameraIdleListener(onCameraIdleListener);
                    Log.e("inside driver", "::::" + cartype);
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("cars").child(cartype).child("driverAvailable");

                    GeoFire geoFire = new GeoFire(reference);
                    geoQuery = geoFire.queryAtLocation(new GeoLocation(location.getLatitude(), location.getLongitude()), radius);
                    geoQuery.removeAllListeners();
                    geoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onDataEntered(DataSnapshot childSnapshot, final GeoLocation location) {


                            if (childSnapshot.getKey() == null) {
                                key = "";
                            }
                            key = childSnapshot.getKey();

                            Log.e("KEYYS", key);
                            geoLocation = location;

                            Double lat = Double.valueOf(childSnapshot.child("l").child("0").getValue(Double.class));
                            Double log = Double.valueOf(childSnapshot.child("l").child("1").getValue(Double.class));

                            String myParentNode = childSnapshot.getKey();

                            locationss = new Location("");
                            locationss.setLatitude(lat);
                            locationss.setLongitude(log);
                            latlngOne = new LatLng(lat, log);


                            if (cartype.equals(Constants.RENTAL)) {
                                mk = map.addMarker(new MarkerOptions()
                                        .position(new LatLng(lat, log))
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_pin)));

                            } else if (cartype.equals(Constants.FOURSETER)) {
                                mk = map.addMarker(new MarkerOptions()
                                        .position(new LatLng(lat, log))
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_pin)));
                            } else if (cartype.equals(Constants.SIXSETER)) {
                                mk = map.addMarker(new MarkerOptions()
                                        .position(new LatLng(lat, log))
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_pin)));
                            }

                            locationlayout.setVisibility(View.VISIBLE);

                            map.getUiSettings().setCompassEnabled(false);
                            map.getUiSettings().setTiltGesturesEnabled(false);
                            map.setMaxZoomPreference(17);
                            appdrawer.setVisibility(View.VISIBLE);
                            /*map.setTrafficEnabled(true);*/
                            chooseloationdialog.setVisibility(View.VISIBLE);
                            Customer customer = new Customer();
                            customer.setLat(lat);
                            customer.setLog(log);
                            customer.setValue(myParentNode);
                            customer.setMarker(mk);
                            arrayList.add(customer);


                            if (arrayList.size() > 0) {
                                progress_dialog.dismiss();
                                maplayout.setVisibility(View.VISIBLE);

                            }

                        }

                        @Override
                        public void onDataExited(DataSnapshot dataSnapshot) {
                            for (int i = 0; i < arrayList.size(); i++) {
                                if (dataSnapshot.getKey().equals(arrayList.get(i).getValue())) {
                                    Marker marker = null;
                                    marker = arrayList.get(i).getMarker();
                                    marker.remove();

                                }
                            }
                        }

                        @Override
                        public void onDataMoved(DataSnapshot dataSnapshot, GeoLocation location) {

                        }

                        @Override
                        public void onDataChanged(DataSnapshot dataSnapshot, GeoLocation location) {


                        }

                        @Override
                        public void onGeoQueryReady() {


                            Log.e("Done", "Done");

                            Log.e("ArralistSize", "" + arrayList.size());


                            if (arrayList.size() == 0) {

                                if (carclick == 1) {
                                    if (cartype.equals(Constants.FOURSETER)) {
                                        DialogBox.setNoDriverDriverList(context, context.getString(R.string.nodriver));
                                    } else if (cartype.equals(Constants.SIXSETER)) {
                                        DialogBox.setNoDriverDriverList(context, context.getString(R.string.nodriver));
                                    } else if (cartype.equals(Constants.RENTAL)) {
                                        DialogBox.setNoDriverDriverList(context, context.getString(R.string.nodriver));
                                    }
                                }  else {
                                    if (cartype.equals(Constants.FOURSETER)) {
                                        cartype = Constants.SIXSETER;
                                        getLocation();
                                    } else if (cartype.equals(Constants.SIXSETER)) {
                                        cartype = Constants.RENTAL;
                                        getLocation();

                                    } else if (cartype.equals(Constants.RENTAL)) {

                                        if (carclick == 1) {
                                            DialogBox.setNoDriverDriverList(context, context.getString(R.string.nodriver));
                                        } else {
                                            progress_dialog.dismiss();
                                            maplayout.setVisibility(View.VISIBLE);
                                            chooseloationdialog.setVisibility(View.VISIBLE);
                                            locationlayout.setVisibility(View.VISIBLE);
                                            appdrawer.setVisibility(View.VISIBLE);
                                            DialogBox.setNoDriver(Dashboard.this, getString(R.string.nodriver));
                                        }
                                    }


                                }
                            }


                                live();


                            }

                            @Override
                            public void onGeoQueryError (DatabaseError error){

                            }
                        });


                    }
                });


            }
        }


    private void live() {


        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {


                    map = googleMap;


                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("cars").child(cartype).child("driverAvailable");
                    Log.e("the firebase cartype", "::" + cartype);
                    GeoFire geoFire = new GeoFire(reference);
                    GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(location1.getLatitude(), location1.getLongitude()), radius);
                    geoQuery.removeAllListeners();
                    geoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onDataEntered(final DataSnapshot childSnapshot, GeoLocation location) {

                            FirebaseDatabase.getInstance().getReference("cars").child("driverDetail").child(childSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {


                                    if (dataSnapshot.child("driverstatus").getValue().equals("1")) {

                                        final Double lat = Double.valueOf(String.valueOf(dataSnapshot.child("lat").getValue()));
                                        final Double log = Double.valueOf(String.valueOf(dataSnapshot.child("lng").getValue()));

                                        bearing = Float.parseFloat(" " + dataSnapshot.child("bearing").getValue());


                                        String myParentNode = childSnapshot.getKey();

                                        locationss = new Location("");
                                        locationss.setLatitude(lat);
                                        locationss.setLongitude(log);
                                        latlngOne = new LatLng(lat, log);

                                        for (int i = 0; i < arrayList.size(); i++) {
                                            if (myParentNode.equals(arrayList.get(i).getValue())) {
                                                animateMarker(locationss, arrayList.get(i).getMarker(), bearing);
                                            }
                                        }
                                    } else {
                                        for (int i = 0; i < arrayList.size(); i++) {
                                            if (childSnapshot.getKey().equals(arrayList.get(i).getValue())) {

                                                Marker marker = null;
                                                marker = arrayList.get(i).getMarker();
                                                marker.remove();
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }

                        @Override
                        public void onDataExited(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onDataMoved(DataSnapshot dataSnapshot, GeoLocation location) {

                        }

                        @Override
                        public void onDataChanged(DataSnapshot dataSnapshot, GeoLocation location) {


                        }

                        @Override
                        public void onGeoQueryReady() {
                            progress_dialog.dismiss();

                        }

                        @Override
                        public void onGeoQueryError(DatabaseError error) {

                        }
                    });


                }
            });


        }

    }


    private void configureCameraIdle() {
        onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {


            }
        };
    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    public Bitmap resizeMapIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }


    public void getProfile() {

        queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.getProfile + pref_master.getUID() + "/" + "Rider",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jsonData = response.toString();
                        Log.e("Response", jsonData);

                        Log.e("JSON", jsonData);

                        try {
                            JSONObject object = new JSONObject(response);

                            JSONArray jsonArray = new JSONArray(object.getString("data"));


                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jobj1 = jsonArray.getJSONObject(i);

                                    pref_master.setFirstname(jobj1.getString("fname"));
                                    pref_master.setLastname(jobj1.getString("lname"));
                                    pref_master.setEmail(jobj1.getString("email"));
                                    pref_master.setUser_number(jobj1.getString("mobile"));
                                    pref_master.setPassword(jobj1.getString("password"));
                                    pref_master.setUserimage(jobj1.getString("upic"));


                                    if (pref_master.getFirstname().equals("") || pref_master.getLastname().equals("")) {
                                        ridername.setVisibility(View.GONE);
                                    } else {
                                        ridername.setText(pref_master.getFirstname() + " " + pref_master.getLastname());
                                    }

                                    if (pref_master.getUser_number().equals("0")) {
                                        ridernumber.setVisibility(View.GONE);
                                    } else {
                                        ridernumber.setText(pref_master.getUser_number());
                                    }

                                    if (pref_master.getEmail().equals("")) {
                                        rideremail.setVisibility(View.GONE);
                                    } else {
                                        rideremail.setText(pref_master.getEmail());
                                    }
                                    Picasso.with(context)
                                            .load(pref_master.getAwsurl() + pref_master.getUID() + "/" + pref_master.getUserimage()) //extract as User instance method
                                            .placeholder(R.drawable.personal)
                                            .into(profileimage);


                                } catch (JSONException e) {
                                    e.printStackTrace();
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


                maplayout.setVisibility(View.GONE);
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
                getLocation();
                Log.e("NoPoints", "NoPoints");
                return;
            } else {

                if (result.size() > 1) {
                    maplayout.setVisibility(View.VISIBLE);
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


                            String[] separated = distance.split(" ");
                            separated[0] = separated[0].trim();


                            Log.e("DistanceSubtring", separated[0]);

                            pref_master.setDistance(separated[0]);

                            continue;
                        } else if (j == 1) { // Get duration from the list
                            duration = (String) point.get("duration");
                            String[] separated = duration.split(" ");

                            separated[0] = separated[0].trim();
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


                // Drawing polyline in the Google Map for the i-th route
                map.addPolyline(lineOptions);
             /*   map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location1.getLatitude(), location1.getLongitude())));
                map.animateCamera(CameraUpdateFactory.zoomTo(10));*/


            }


            chooseloationdialog.setVisibility(View.GONE);


            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            builder.include(marker1.getPosition());
            builder.include(marker2.getPosition());

            LatLngBounds bounds = builder.build();

            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (width * 0.15);

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

            map.animateCamera(cu);


        }
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
                                    } else if (sid.equals("4")) {
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
                                    } else if (sid.equals("20")) {
                                        Constants.MOBILE_LENGTH = jobj1.getString("sval");
                                    } else if (sid.equals("2")) {
                                        Constants.ACCESSKEY = jobj1.getString("sval");
                                    } else if (sid.equals("3")) {
                                        Constants.SECRETKEY = jobj1.getString("sval");
                                    } else if (sid.equals("21")) {
                                        Constants.FOURSETER = jobj1.getString("snm");
                                    } else if (sid.equals("22")) {
                                        Constants.SIXSETER = jobj1.getString("snm");
                                    } else if (sid.equals("23")) {
                                        Constants.RENTAL = jobj1.getString("snm");
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


        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("riderid", pref_master.getUID());

        Log.e("parameter", " " + new JSONObject(postParam));

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Config.getfavlocation, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Reposnse", response.toString());

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

                                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();

                            }

                            favDropDownListAdapter.notifyDataSetChanged();


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


    private void Logout() {

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

        Log.e("parameter", " " + new JSONObject(postParam));
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Config.logout, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Reposnse", response.toString());
                        progress_dialog.dismiss();
                        try {
                            Log.e("Status", response.getString("status"));

                            if (response.getString("status").equals("200")) {

                                Toast.makeText(getApplicationContext(), "Logout Successfully", Toast.LENGTH_SHORT).show();

                                chooseloationdialog.setVisibility(View.GONE);
                                carlayout.setVisibility(View.GONE);
                                locationlayout.setVisibility(View.GONE);
                                Intent ii = new Intent(getApplicationContext(), First_Screen.class);
                                ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(ii);
                                mDrawerLayout.closeDrawer(Gravity.START);
                                // pref_master.clear_pref();
                                pref_master.setAuto(1);
                                pref_master.setCtype("");

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

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            finish();
        }
    };

    Runnable getRunnable = new Runnable() {
        @Override
        public void run() {

            Intent callGPSSettingIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(callGPSSettingIntent, 123);
        }
    };


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
                } else if ("asus".equalsIgnoreCase(manufacturer)) {
                    intent.setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.entry.FunctionActivity"));
                }

                List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                if (list.size() > 0) {
                    startActivity(intent);
                }
            } catch (Exception e) {

            }
        }
    };


    public void getFutureDate(Date currentDate, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.DATE, days);

        futureDate = cal.getTime();
    }

    public void getLocation() {
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

        getLastLocation();

    }


    public void getNewLocation() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    locationManager.removeUpdates(locationListener);
                    arrayList.clear();

                    location1 = location;

                    if (connected == 1) {
                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {

                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location1.getLatitude(), location1.getLongitude()), 18);
                                googleMap.animateCamera(cameraUpdate);

                                LatLng latLng = googleMap.getCameraPosition().target;
                                Geocoder geocoder = new Geocoder(Dashboard.this);

                                try {
                                    List<Address> addressList = geocoder.getFromLocation(location1.getLatitude(), location1.getLongitude(), 1);
                                    if (addressList != null && addressList.size() > 0) {
                                        String loc = addressList.get(0).getAddressLine(0);
                                        String cou = addressList.get(0).getCountryName();
                                        if (!loc.isEmpty() && !cou.isEmpty())
                                            locality = loc;
                                        country = cou;
                                        Log.e("LocalityMain", locality);
                                        Log.e("Country", country);
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                googleMap.setMapStyle(
                                        MapStyleOptions.loadRawResourceStyle(
                                                context, R.raw.map_style));

                            }
                        });

                        getDriver(location);

                        connected = 0;


                    } else {
                        if (extras != null) {

                            pref_master.setFromlocationtrip(location1.getLatitude() + "," + location1.getLongitude());


                            carlayout.setVisibility(View.VISIBLE);


                            if (carlayout.getVisibility() == View.VISIBLE) {
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                params.addRule(RelativeLayout.ABOVE, R.id.carlayout);
                                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                                locationlayout.setLayoutParams(params);


                            }


                            mapFragment.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(GoogleMap googleMap) {

                                    map = googleMap;

                                    lat = extras.getDouble("lat");
                                    lng = extras.getDouble("lng");
                                    String tag = extras.getString("tag");

                                    if (tag.equals("1")) {
                                        chooseloationdialog.setVisibility(View.GONE);
                                    }
                                    Log.e("Laatataa", "" + lat);
                                    Log.e("Lonnfd", " " + lng);

                                    chooseloationdialog.setVisibility(View.GONE);
                                    latLng = new LatLng(lat, lng);
                                    marker2 = googleMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(lat, lng))
                                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.des_marker)));


                                    String url = getMapsApiDirectionsUrl(latLng, new LatLng(location1.getLatitude(), location1.getLongitude()));
                                    ReadTask downloadTask = new ReadTask();
                                    // Start downloading json data from Google Directions API
                                    downloadTask.execute(url);
                                    linedraw = 1;

                                }
                            });
                            // and get whatever type user account id is
                        }

                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {


                                if (extras != null) {

                                    marker1 = googleMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(location1.getLatitude(), location1.getLongitude()))
                                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.currentmarker)));
                                }


                                LatLng latLng = googleMap.getCameraPosition().target;
                                Geocoder geocoder = new Geocoder(Dashboard.this);

                                try {
                                    List<Address> addressList = geocoder.getFromLocation(location1.getLatitude(), location1.getLongitude(), 1);
                                    if (addressList != null && addressList.size() > 0) {
                                        String loc = addressList.get(0).getAddressLine(0);
                                        String cou = addressList.get(0).getCountryName();
                                        if (!loc.isEmpty() && !cou.isEmpty())
                                            locality = loc;
                                        country = cou;
                                        pref_master.setFromaddress(loc);
                                        Log.e("LocalityMain", locality);
                                        Log.e("Country", country);
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                                googleMap.setMapStyle(
                                        MapStyleOptions.loadRawResourceStyle(
                                                context, R.raw.map_style));


                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location1.getLatitude(), location1.getLongitude()), 18);
                                googleMap.animateCamera(cameraUpdate);

                            }
                        });


                        getDriver(location);
                    }

                    LocationListener locationListener = new LocationListener() {
                        @Override
                        public void onLocationChanged(final Location location) {

                            mapFragment.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(GoogleMap googleMap) {
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
                                    googleMap.setMyLocationEnabled(true);
                                    googleMap.getUiSettings().setMyLocationButtonEnabled(false);

                                }
                            });


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


                    locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

                    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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


                } else {
                    Toast.makeText(getApplicationContext(), "Location Not Found Please Try Again", Toast.LENGTH_LONG).show();
                    Log.e("ChangePasswordNewUSer", "Location");
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


        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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


    public void getLastLocation() {


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


        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {

                    arrayList.clear();

                    location1 = location;

                    if (connected == 1) {
                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {

                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location1.getLatitude(), location1.getLongitude()), 18);
                                googleMap.animateCamera(cameraUpdate);

                                LatLng latLng = googleMap.getCameraPosition().target;
                                Geocoder geocoder = new Geocoder(Dashboard.this);

                                try {
                                    List<Address> addressList = geocoder.getFromLocation(location1.getLatitude(), location1.getLongitude(), 1);
                                    if (addressList != null && addressList.size() > 0) {
                                        String loc = addressList.get(0).getAddressLine(0);
                                        String cou = addressList.get(0).getCountryName();
                                        if (!loc.isEmpty() && !cou.isEmpty())
                                            locality = loc;
                                        country = cou;
                                        Log.e("LocalityMain", locality);
                                        Log.e("Country", country);
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                googleMap.setMapStyle(
                                        MapStyleOptions.loadRawResourceStyle(
                                                context, R.raw.map_style));

                            }
                        });

                        getDriver(location);

                        connected = 0;
                    } else {
                        if (extras != null) {

                            pref_master.setFromlocationtrip(location1.getLatitude() + "," + location1.getLongitude());


                            carlayout.setVisibility(View.VISIBLE);

                            if (carlayout.getVisibility() == View.VISIBLE) {
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                params.addRule(RelativeLayout.ABOVE, R.id.carlayout);
                                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                                locationlayout.setLayoutParams(params);

                            }


                            mapFragment.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(GoogleMap googleMap) {

                                    map = googleMap;

                                    lat = extras.getDouble("lat");
                                    lng = extras.getDouble("lng");
                                    Log.e("Laatataa", "" + lat);
                                    Log.e("Lonnfd", " " + lng);

                                    chooseloationdialog.setVisibility(View.GONE);
                                    latLng = new LatLng(lat, lng);
                                    marker2 = googleMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(lat, lng))
                                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.des_marker)));


                                    String url = getMapsApiDirectionsUrl(latLng, new LatLng(location1.getLatitude(), location1.getLongitude()));
                                    ReadTask downloadTask = new ReadTask();
                                    // Start downloading json data from Google Directions API
                                    downloadTask.execute(url);
                                    linedraw = 1;

                                }
                            });
                            // and get whatever type user account id is
                        }

                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {


                                if (extras != null) {

                                    marker1 = googleMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(location1.getLatitude(), location1.getLongitude()))
                                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.currentmarker)));
                                }


                                LatLng latLng = googleMap.getCameraPosition().target;
                                Geocoder geocoder = new Geocoder(Dashboard.this);

                                try {
                                    List<Address> addressList = geocoder.getFromLocation(location1.getLatitude(), location1.getLongitude(), 1);
                                    if (addressList != null && addressList.size() > 0) {
                                        String loc = addressList.get(0).getAddressLine(0);
                                        String cou = addressList.get(0).getCountryName();
                                        if (!loc.isEmpty() && !cou.isEmpty())
                                            locality = loc;
                                        country = cou;
                                        pref_master.setFromaddress(loc);
                                        Log.e("LocalityMain", locality);
                                        Log.e("Country", country);
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                                googleMap.setMapStyle(
                                        MapStyleOptions.loadRawResourceStyle(
                                                context, R.raw.map_style));


                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location1.getLatitude(), location1.getLongitude()), 18);
                                googleMap.animateCamera(cameraUpdate);

                            }
                        });


                        getDriver(location);
                    }

                    LocationListener locationListener = new LocationListener() {
                        @Override
                        public void onLocationChanged(final Location location) {

                            mapFragment.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(GoogleMap googleMap) {
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
                                    googleMap.setMyLocationEnabled(true);
                                    googleMap.getUiSettings().setMyLocationButtonEnabled(false);

                                }
                            });


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


                    locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

                    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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


                } else {
                    getNewLocation();
                }

            }
        });


    }

}
