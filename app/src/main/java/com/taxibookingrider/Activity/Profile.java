package com.taxibookingrider.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;
import com.taxibookingrider.Controller.Config;
import com.taxibookingrider.Controller.Constants;
import com.taxibookingrider.Controller.GraduallyTextView;
import com.taxibookingrider.Controller.Progress_dialog;
import com.taxibookingrider.Controller.Tahoma;
import com.taxibookingrider.Controller.TahomaItalic;
import com.taxibookingrider.Controller.Util;
import com.taxibookingrider.Fragment.ChangePasswordUpdateprofileold;
import com.taxibookingrider.MyApplication;
import com.taxibookingrider.R;
import com.taxibookingrider.Utils.Pref_Master;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.taxibookingrider.Controller.Config.isValidEmailAddress;

/**
 * Created by Admin on 1/8/2018.
 */

public class Profile extends AppCompatActivity {
    Context context = this;
    TransferUtility transferUtility;
    AmazonS3Client amazonS3;
    Pref_Master pref_master;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private int PICK_IMAGE_REQUEST = 1;
    Uri uri;
    int imagevalue = 0;
    String image;
    private static final int CAMERA_REQUEST = 1888;
    String ipaddress;
    RelativeLayout whole;
    String path, copyimagepath, imageFilePath;
    //Progress_dialog progress_dialog;
    RequestQueue queue;
    android.support.v7.app.AlertDialog alert;
    ImageView notify_bar;
    File file;
    RelativeLayout submitupdate;
    LinearLayout profilelayout;
    ImageView imguploadbutton;
            CircleImageView img_user;
    ScrollView scrollView;
    String firstname, lastname, mobile, email, password, verify;
    EditText updatefirstname, updatelastname, updatemobile, updateemail;
    EditText updatepassword;
    int selected = 0;
    LruCache picassoCache;
    LinearLayout profilesquare;
    String android_id;
    File folder;
    ImageView img_save,img_edit;
    RelativeLayout deletebtn;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 55;
    TextInputLayout profile_fname,profile_lname,profile_mobile1,profile_email,profile_pass;
    android.support.v7.app.AlertDialog progress_dialog;
    GraduallyTextView txt_load;
    ImageView img_gif;
    int Maxlength;


    @Override
    public void onBackPressed() {
        if (file != null) {
            file.delete();
        }


        Intent i = new Intent(getApplicationContext(), Dashboard.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();
/*
            try {
                path = getPath(uri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }*/

            try {
                path = getPath(uri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        /*    if (path == null) {

                DialogBox.setNoFormat(context, "Format Not Supported Please Select Image From Gallery", runnable);

            }
*/

            if (uri.toString().startsWith("content://com.google.android.apps.docs.storage")) {
                try {
                    InputStream is = context.getContentResolver().openInputStream(uri);
                    if (is != null) {
                        Bitmap pictureBitmap = BitmapFactory.decodeStream(is);
                        //You can use this bitmap according to your purpose or Set bitmap to imageview

                        uri = getImageUri(context, pictureBitmap);

                        try {
                            path = getPath(uri);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }

                        img_user.setImageURI(Uri.parse(path));

                        File source = new File(path);

                        File folder = new File(Environment.getExternalStorageDirectory().toString() + "/Joy/GalleryImage");
                        folder.mkdirs();
                        //Save the path as a string value
                        String extStorageDirectory = folder.toString();
                        //Create New file and name it Image2.PNG
                        Long tsLong = System.currentTimeMillis() / 1000;
                        String ts = tsLong.toString();

                        File file = new File(extStorageDirectory, ts + ".jpg");
                        String pathh = file.getAbsolutePath();


                        File f = new File(pathh);
                        if (!f.exists()) {
                            try {
                                f.createNewFile();
                                copyFile(source, f);
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                        //Drawable d = new BitmapDrawable(getResources(), myBitmap);

                        copyimagepath = f.getAbsolutePath();

                        Log.e("GalleryDrive", copyimagepath);

                        imagevalue = 1;

                    }
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                File source = new File(path);

                File folder = new File(Environment.getExternalStorageDirectory().toString() + "/Joy/GalleryImage");
                folder.mkdirs();
                //Save the path as a string value
                String extStorageDirectory = folder.toString();
                //Create New file and name it Image2.PNG
                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();

                file = new File(extStorageDirectory, ts + ".jpg");
                String pathh = file.getAbsolutePath();


                File f = new File(pathh);
                if (!f.exists()) {
                    try {
                        f.createNewFile();
                        copyFile(source, f);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                copyimagepath = f.getAbsolutePath();

                img_user.setImageURI(Uri.parse(copyimagepath));
                Log.e("Gallery", copyimagepath);
                imagevalue = 1;
            }


            //Drawable d = new BitmapDrawable(getResources(), myBitmap);


        } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            uri = getImageUri(context, photo);


            try {
                path = getPath(uri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            img_user.setImageURI(Uri.parse(path));

            File source = new File(path);

            File folder = new File(Environment.getExternalStorageDirectory().toString() + "/Joy/Cameraimage");
            folder.mkdirs();
            //Save the path as a string value
            String extStorageDirectory = folder.toString();
            //Create New file and name it Image2.PNG
            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();

            File file = new File(extStorageDirectory, ts + ".jpg");
            String pathh = file.getAbsolutePath();


            File f = new File(pathh);
            if (!f.exists()) {
                try {
                    f.createNewFile();
                    copyFile(source, f);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            //Drawable d = new BitmapDrawable(getResources(), myBitmap);

            copyimagepath = f.getAbsolutePath();

            Log.e("Camera", copyimagepath);

            imagevalue = 1;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.fragment_profile);

        verify = "yes";

        getAddreess();


        pref_master = new Pref_Master(context);
        queue = Volley.newRequestQueue(this);
        transferUtility = Util.getTransferUtility(context);
        amazonS3 = Util.getS3Client(this);
        updatefirstname = (EditText) findViewById(R.id.updatefirstname);
        /*imguploadbutton = (ImageView) findViewById(R.id.imguploadbutton);*/
        updatelastname = (EditText) findViewById(R.id.updatelastname);
        scrollView = (ScrollView) findViewById(R.id.scollview);
        whole = (RelativeLayout) findViewById(R.id.whole);
        updatemobile = (EditText) findViewById(R.id.updatemobile);
        notify_bar = (ImageView) findViewById(R.id.notify_bar);
        updateemail = (EditText) findViewById(R.id.updateemailid);
        updatepassword = (EditText) findViewById(R.id.updatepassword);
        img_user = (CircleImageView) findViewById(R.id.img_user);
       /* submitupdate = (RelativeLayout) findViewById(R.id.submitupdate);*/
        profilelayout = (LinearLayout) findViewById(R.id.profilelayout);
        deletebtn = findViewById(R.id.deletebtn);
       // progress_dialog=new Progress_dialog();
        updatefirstname.setText(pref_master.getFirstname());
        updatelastname.setText(pref_master.getLastname());
        updatemobile.setText(pref_master.getUser_number());
        updateemail.setText(pref_master.getEmail());
        updatepassword.setText(pref_master.getPassword());
        profilesquare = (LinearLayout)findViewById(R.id.profilesquare);
        profile_fname=(TextInputLayout)findViewById(R.id.profile_fname);
        profile_lname=(TextInputLayout)findViewById(R.id.profile_lname);
        profile_mobile1=(TextInputLayout)findViewById(R.id.profile_mobile1);
        profile_email=(TextInputLayout)findViewById(R.id.profile_email);
        profile_pass=(TextInputLayout)findViewById(R.id.profile_pass);
        img_edit=(ImageView)findViewById(R.id.img_edit);
        img_save=(ImageView)findViewById(R.id.img_save);


        Picasso.Builder builder = new Picasso.Builder(getApplicationContext());
        picassoCache = new LruCache(getApplicationContext());
        builder.memoryCache(picassoCache);

        updatefirstname.setEnabled(false);
        updatelastname.setEnabled(false);
        updateemail.setEnabled(false);
        updatemobile.setEnabled(false);
        updatepassword.setEnabled(false);
        img_user.setEnabled(false);

        Maxlength= Integer.parseInt(Constants.MOBILE_LENGTH);
        updatemobile.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Maxlength)});

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        updatemobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                profile_mobile1.setErrorEnabled(false);

            }
        });
        updatefirstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               profile_fname.setErrorEnabled(false);
            }
        });

        updatelastname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                profile_lname.setErrorEnabled(false);
            }
        });

        updateemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                profile_email.setErrorEnabled(false);
            }
        });




        updatepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                scrollView.setVisibility(View.GONE);
                Intent i = new Intent(getApplicationContext(), ChangePasswordUpdateprofileold.class);
                startActivity(i);
            }
        });
        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_save.setVisibility(View.VISIBLE);
                img_edit.setVisibility(View.GONE);
                img_user.setEnabled(true);
                updatefirstname.setEnabled(true);
                updatelastname.setEnabled(true);
                updateemail.setEnabled(true);
                updatemobile.setEnabled(true);
                updatepassword.setEnabled(true);
                updatepassword.setEnabled(true);
                
                   if(updatepassword.isInEditMode())
                   {
                       updatepassword.setEnabled(false);
                       updatepassword.setClickable(true);

                   }

            }
        });

        img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_save.setVisibility(View.VISIBLE);
                img_edit.setVisibility(View.GONE);
                img_user.setEnabled(true);
                updatefirstname.setEnabled(true);
                updatelastname.setEnabled(true);
                updateemail.setEnabled(true);
                updatemobile.setEnabled(true);
                updatepassword.setEnabled(true);


                if (imagevalue == 1) {
                    if (updatefirstname.getText().toString().trim().length() == 0) {
                        updatefirstname.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                        profile_fname.setError(getString(R.string.enter_firstname));
                    } else if (updatelastname.getText().toString().trim().length() == 0) {
                        profile_fname.setErrorEnabled(false);
                        updatelastname.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                        profile_lname.setError(getString(R.string.enter_lastname));
                    } else if (updatemobile.getText().toString().trim().length() == 0) {
                        updatemobile.requestFocus();
                        profile_lname.setErrorEnabled(false);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                        profile_mobile1.setError(getString(R.string.enter_mobile_number));
                    } else if (updateemail.getText().toString().trim().length() != 0) {
                        profile_mobile1.setErrorEnabled(false);
                        if (updateemail.getText().toString().contains(" ")) {
                            updateemail.requestFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                            profile_email.setError(getString(R.string.enter_email));
                        } else if (!isValidEmailAddress(updateemail.getText().toString())) {
                            updateemail.requestFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                            profile_email.setError(getString(R.string.enter_valid_email));
                        } else if (updatemobile.getText().toString().trim().length() < Maxlength) {
                            updatemobile.requestFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                            profile_mobile1.setError(getString(R.string.enter_mobile_8_charcters));
                        } else if (updatepassword.getText().toString().trim().length() == 0) {
                            updatepassword.requestFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                            profile_pass.setError(getString(R.string.error_password));
                        } else {
                            LayoutInflater li = LayoutInflater.from(context);
                            View v1 = li.inflate(R.layout.progress_dialog_layout, null);
                            progress_dialog = new android.support.v7.app.AlertDialog.Builder(context, R.style.cart_dialog).setView(v).show();
                            progress_dialog.setCancelable(false);
                            progress_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            txt_load = (GraduallyTextView) v1.findViewById(R.id.txt_load);
                            img_gif = (ImageView) v1.findViewById(R.id.img_gif);
                            txt_load.startLoading();

                            Glide.with(context)
                                    .load(R.drawable.gifimage111)
                                    .asGif()
                                    .placeholder(R.drawable.gifimage111)
                                    .crossFade()
                                    .into(img_gif);
                            progress_dialog.show();


                            beginUpload(copyimagepath);
                            imagevalue = 0;
                        }
                    } else if (updateemail.getText().toString().contains(" ")) {
                        updateemail.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                        profile_email.setError(getString(R.string.enter_email));
                    } else if (updatemobile.getText().toString().trim().length() < 8) {
                        updatemobile.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                        profile_mobile1.setError(getString(R.string.enter_mobile_8_charcters));
                    } else if (updatepassword.getText().toString().trim().length() == 0) {
                        updatepassword.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                        profile_pass.setError(getString(R.string.error_password));
                    } else {
                     /*   LayoutInflater li = LayoutInflater.from(context);
                        View v1 = li.inflate(R.layout.progress_dialog_layout, null);
                        progress_dialog = new android.support.v7.app.AlertDialog.Builder(context, R.style.cart_dialog).setView(v).show();
                        progress_dialog.setCancelable(false);
                        progress_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        txt_load = (GraduallyTextView) v1.findViewById(R.id.txt_load);
                        img_gif = (ImageView) v1.findViewById(R.id.img_gif);
                        txt_load.startLoading();

                        Glide.with(context)
                                .load(R.drawable.gifimage111)
                                .asGif()
                                .placeholder(R.drawable.gifimage111)
                                .crossFade()
                                .into(img_gif);
                        progress_dialog.show();*/
                        beginUpload(copyimagepath);
                        imagevalue = 0;

                    }

                } else {

                    image = pref_master.getUserimage();

                    if (updatefirstname.getText().toString().trim().length() == 0) {
                        updatefirstname.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                        profile_fname.setError(getString(R.string.enter_firstname));
                    } else if (updatelastname.getText().toString().trim().length() == 0) {
                        profile_fname.setErrorEnabled(false);
                        updatelastname.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                        profile_lname.setError(getString(R.string.enter_lastname));
                    } else if (updatemobile.getText().toString().trim().length() == 0) {
                        updatemobile.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                        profile_mobile1.setError(getString(R.string.enter_mobile_number));
                        profile_lname.setErrorEnabled(false);
                    } else if (updateemail.getText().toString().trim().length() != 0) {
                        if (updateemail.getText().toString().contains(" ")) {
                            updateemail.requestFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                            profile_email.setError(getString(R.string.enter_email));
                        } else if (!isValidEmailAddress(updateemail.getText().toString())) {
                            updateemail.requestFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                            profile_email.setError(getString(R.string.enter_valid_email));
                        } else if (updatemobile.getText().toString().trim().length() < Maxlength) {
                            updatemobile.requestFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                            profile_mobile1.setError(getString(R.string.enter_mobile_8_charcters));
                        } else if (updatepassword.getText().toString().trim().length() == 0) {
                            updatepassword.requestFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                            profile_pass.setError(getString(R.string.error_password));
                        } else {
                            Log.e("Up", "Up");

                            makeJsonObjReq();

                        }
                    } else if (updateemail.getText().toString().contains(" ")) {
                        updateemail.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                        profile_email.setError(getString(R.string.enter_email));
                    } else if (updatemobile.getText().toString().trim().length() < Maxlength) {
                        updatemobile.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                        profile_mobile1.setError(getString(R.string.enter_mobile_8_charcters));
                    } else if (updatepassword.getText().toString().trim().length() == 0) {
                        /*updatepassword.requestFocus();*/
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                        profile_pass.setError(getString(R.string.error_password));
                    } else {
                        Log.e("Down", "Down");

                        makeJsonObjReq();
                        profile_mobile1.setErrorEnabled(false);
                        profile_pass.setErrorEnabled(false);
                        profile_email.setErrorEnabled(false);
                        profile_lname.setErrorEnabled(false);
                        profile_fname.setErrorEnabled(false);

                    }


                }


            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                LayoutInflater li = LayoutInflater.from(context);
                View v = li.inflate(R.layout.new_noti_popup, null);
                alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();
                alert.setCancelable(true);
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Tahoma ok = v.findViewById(R.id.txt_ok);
                Tahoma cancel = v.findViewById(R.id.txt_cancle);
                Tahoma tahoma = v.findViewById(R.id.msglaert);

                tahoma.setText("Do you want to Delete Your Account ? ");


                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                        DeleteAccount();

                    }
                });


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });
            }
        });


     /*   submitupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });*/

        notify_bar.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (file != null) {
                    file.delete();
                }
                Intent ii = new Intent(getApplicationContext(), Dashboard.class);
                ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(ii);
            }
        });

        if (pref_master.getUserimage().equals(""))

        {
            Picasso.with(context)
                    .load(R.drawable.personal) //extract as User instance method
                    .placeholder(R.drawable.personal)
                    .into(img_user);
            Log.e("Blank", "Blank");
        } else

        {
            Log.e("the user image create","::"+pref_master.getUserimage());
            Picasso.with(context)
                    .load(pref_master.getAwsurl() + pref_master.getUID() + "/" + pref_master.getUserimage()) //extract as User instance method
                    .placeholder(R.drawable.personal)
                    .into(img_user);


        }


        img_user.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                checkAndRequestPermissions();
                if (file != null) {

                    file.delete();
                    LayoutInflater li = LayoutInflater.from(context);
                    View view = li.inflate(R.layout.dialog_popup_camera, null);

                    final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(view).show();
                    alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    Window window = alert.getWindow();
                    WindowManager.LayoutParams wlp = window.getAttributes();
                    wlp.y = 100; // Here is the param to set your dialog position. Same with params.x
                    alert.getWindow().setAttributes(wlp);

                    wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                    window.setAttributes(wlp);
                    alert.setCancelable(false);

                    alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    Tahoma gallery = (Tahoma) view.findViewById(R.id.txt_gallery);

                    Tahoma camera = (Tahoma) view.findViewById(R.id.txt_camera);


                    gallery.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            alert.dismiss();
                            Intent intent = new Intent();
                            intent.setType("image*//*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

                        }
                    });


                    camera.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert.dismiss();
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        }
                    });
                } else {
                    LayoutInflater li = LayoutInflater.from(context);
                    View view = li.inflate(R.layout.dialog_popup_camera, null);

                    final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(view).show();
                    alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    Window window = alert.getWindow();
                    WindowManager.LayoutParams wlp = window.getAttributes();
                    wlp.y = 100; // Here is the param to set your dialog position. Same with params.x
                    alert.getWindow().setAttributes(wlp);

                    wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                    window.setAttributes(wlp);

                    alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alert.setCancelable(true);

                    Tahoma gallery = (Tahoma) view.findViewById(R.id.txt_gallery);

                    Tahoma camera = (Tahoma) view.findViewById(R.id.txt_camera);


                    gallery.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            alert.dismiss();
                            Intent intent = new Intent();
                            intent.setType("image*//*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

                        }
                    });


                    camera.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert.dismiss();
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        }
                    });
                }
            }
        });


        MyApplication.getInstance().trackScreenView("Profile");


    }

    private void beginUpload(String filePath) {

        CannedAccessControlList cannedAccessControlList;
        cannedAccessControlList = CannedAccessControlList.PublicRead;


        amazonS3.deleteObject(Constants.BUCKET_NAME + "/" + pref_master.getUserfolder() + "/" + pref_master.getUID(), pref_master.getUserimage());


        if (filePath == null) {
            Toast.makeText(context, "Could not find the filepath of the selected file", Toast.LENGTH_LONG).show();
            return;
        }
        File file = new File(filePath);


        image = file.getName();

//        Toast.makeText(getApplicationContext(),file.getName(), Toast.LENGTH_LONG).show();

        Log.e("Upload", " " + Constants.BUCKET_NAME + "/" + pref_master.getUserfolder() + "/" + pref_master.getUID() + "" + file.getName());
        final TransferObserver observer = transferUtility.upload(Constants.BUCKET_NAME + "/" + pref_master.getUserfolder() + "/" + pref_master.getUID(), file.getName(), file, new ObjectMetadata());



        /*
         * Note that usually we set the transfer listener after initializing the
         * transfer. However it isn't required in this sample app. The flow is
         * click upload button -> start an activity for image selection
         * startActivityForResult -> onActivityResult -> beginUpload -> onResume
         * -> set listeners to in progress transfers.
         */
        // observer.setTransferListener(new UploadListener());

        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (state.COMPLETED.equals(observer.getState())) {


                    verify = "yes";
                    makeJsonObjReq();

                }
                if (state.IN_PROGRESS.equals(observer.getState())) {

                }

            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                long _bytesCurrent = bytesCurrent;
                long _bytesTotal = bytesTotal;
                double percentage = ((double) _bytesCurrent / (double) _bytesTotal * 100);
                Log.e("percentage", "" + percentage);

            }

            @Override
            public void onError(int id, Exception ex) {
            }
        });


    }


    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    private String getPath(Uri uri) throws URISyntaxException {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }
        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }


    private void makeJsonObjReq() {
        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.progress_dialog_layout, null);
        progress_dialog = new android.support.v7.app.AlertDialog.Builder(context, R.style.cart_dialog).setView(v).show();
        progress_dialog.setCancelable(false);
        progress_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        txt_load = (GraduallyTextView) v.findViewById(R.id.txt_load);
        img_gif = (ImageView) v.findViewById(R.id.img_gif);

        Glide.with(context)
                .load(R.drawable.gifimage111)
                .asGif()
                .placeholder(R.drawable.gifimage111)
                .crossFade()
                .into(img_gif);
        progress_dialog.show();
        if (copyimagepath == null) {
            copyimagepath = "";
        }
        if (!pref_master.getUser_number().equals(updatemobile.getText().toString())) {
            verify = "no";
        }
        if (!pref_master.getEmail().equals(updateemail.getText().toString())) {
            verify = "no";
        }
        if (!pref_master.getEmail().equals(updateemail.getText().toString()) && !pref_master.getUser_number().equals(updatemobile.getText().toString())) {
            verify = "no";
        }

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("uid", pref_master.getUID());
        postParam.put("utype", "Rider");
        postParam.put("oldmobile", pref_master.getUser_number());
        postParam.put("newmobile", updatemobile.getText().toString());
        postParam.put("oldemail", pref_master.getEmail());
        postParam.put("newemail", updateemail.getText().toString());
        postParam.put("fname", updatefirstname.getText().toString());
        postParam.put("lname", updatelastname.getText().toString());
        /*postParam.put("password", updatepassword.getText().toString());*/
        postParam.put("upic", image);
        postParam.put("verifiedotp", verify);

        Log.e("parameter", " " + new JSONObject(postParam));


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Config.updateprofile, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Reposnse", response.toString());

                        try {
                            Log.e("Status", response.getString("status"));

                            if (response.getString("status").equals("200")) {

                                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                img_edit.setVisibility(View.VISIBLE);
                                img_save.setVisibility(View.GONE);
                                updatefirstname.setEnabled(false);
                                updatelastname.setEnabled(false);
                                updateemail.setEnabled(false);
                                updatemobile.setEnabled(false);
                                updatepassword.setEnabled(false);
                                img_user.setEnabled(false);

                                progress_dialog.dismiss();


                                if (response.getString("activity").equals("otp-sent")) {

                                    Intent i = new Intent(getApplicationContext(), UpdateProfileChangeMobileOTPscreen.class);
                                    i.putExtra("otp", response.getString("OTP"));
                                    i.putExtra("fname", updatefirstname.getText().toString());
                                    i.putExtra("lname", updatelastname.getText().toString());
                                    i.putExtra("mobile", updatemobile.getText().toString());
                                    i.putExtra("email", updateemail.getText().toString());
                                    i.putExtra("pass", updatepassword.getText().toString());
                                    i.putExtra("image", image);
                                    startActivity(i);

                                }


                                JSONArray jsonArray = new JSONArray(response.getString("data"));

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jobj1 = jsonArray.getJSONObject(i);

                                    pref_master.setFirstname(jobj1.getString("fname"));
                                    pref_master.setLastname(jobj1.getString("lname"));
                                    pref_master.setUser_number(jobj1.getString("newmobile"));
                                    pref_master.setEmail(jobj1.getString("newemail"));
                                    pref_master.setUserimage(jobj1.getString("upic"));

                                    Log.e("the user image","::"+pref_master.getUserimage());
                                }


                                picassoCache.clear();


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

        // Cancelling request
    /* if (queue!= null) {
    queue.cancelAll(TAG);
    } */

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public void setfragment(String title, Fragment fragment) {

        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.whole, fragment);
        fragmentTransaction.commit();
    }


    public void DeleteAccount() {

        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.progress_dialog_layout, null);
        progress_dialog = new android.support.v7.app.AlertDialog.Builder(context, R.style.cart_dialog).setView(v).show();
        progress_dialog.setCancelable(false);
        progress_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        txt_load = (GraduallyTextView) v.findViewById(R.id.txt_load);
        img_gif = (ImageView) v.findViewById(R.id.img_gif);

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
        postParam.put("ip", ipaddress);
        postParam.put("deviceid", android_id);
        postParam.put("dtoken", FirebaseInstanceId.getInstance().getToken());


        Log.e("parameter", " " + new JSONObject(postParam));

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Config.deleteaccount, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Reposnse", response.toString());
                        progress_dialog.dismiss();
                        try {
                            Log.e("Status", response.getString("status"));

                            if (response.getString("status").equals("200")) {

                                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();

                                Intent i = new Intent(getApplicationContext(), First_Screen.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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

    public String getAddreess() {

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    //if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress() ) {
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        ipaddress = inetAddress.getHostAddress();

                    }
                }
            }
        } catch (SocketException ex) {
            /*Log.d(TAG, ex.toString());*/
        }


        return null;
    }

    public void deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory != null) {
            if (fileOrDirectory.isDirectory()) {
                for (File child : fileOrDirectory.listFiles()) {
                    deleteRecursive(child);
                }
            }
            fileOrDirectory.delete();
        }


    }

    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }


        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }

        return true;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }
    };


}
