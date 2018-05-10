package com.taxibookingrider.Firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.taxibookingrider.Activity.Chat_Activity;
import com.taxibookingrider.Activity.Current_tripDemo;
import com.taxibookingrider.Activity.Dashboard;
import com.taxibookingrider.Activity.DriverList;
import com.taxibookingrider.Activity.DriverListDemo;
import com.taxibookingrider.Activity.First_Screen;
import com.taxibookingrider.Activity.LiveTripActivity;
import com.taxibookingrider.Activity.Payment;
import com.taxibookingrider.Controller.DialogService;
import com.taxibookingrider.Controller.Helper;
import com.taxibookingrider.Controller.NotificationHelper;
import com.taxibookingrider.Controller.Progress_dialog;
import com.taxibookingrider.R;
import com.taxibookingrider.Utils.Pref_Master;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Random;

//Created by raj on 6/25/2016.

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    public static String driverid = "";
    public static String ctype = "";
    public static String tripid = "";

    boolean mIsBound = false;
    Context context = this;
    double radius = 0.050;
    private DialogService mBoundService;
    public static final String SECONDARY_CHANNEL = "second";
    public static String message, ntype;
    Pref_Master pref_master;
    GoogleApiClient mGoogleApiClient;
    NotificationCompat.Builder builder;
    NotificationHelper noti;
    Notification.Builder builder2;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        remoteMessage.getCollapseKey();


        pref_master = new Pref_Master(getApplicationContext());




        sendNotification(remoteMessage.getData());
    }

    private void sendNotification(Map<String, String> messageBody) {
        Log.e("Message_Body", "" + messageBody);

        ntype = messageBody.get("ntype");
        message = messageBody.get("body");

        try {
            JSONObject jsonObject = new JSONObject(messageBody.get("details"));

            if (jsonObject.has("driverid")) {
                driverid = jsonObject.getString("driverid");

            } else {
                driverid = "";
            }
            if (jsonObject.has("tripid")) {
                tripid = jsonObject.getString("tripid");

            } else {
                tripid = "";
            }
            if (jsonObject.has("ctype")) {
                ctype = jsonObject.getString("ctype");
            } else {
                ctype = "";
            }


            Log.e("Details", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (Helper.isAppIsInBackground(MyFirebaseMessagingService.this)) {
            Log.e("Off", "Off");
            GenerateNotification(messageBody.get("body"));
        } else {

            if (ntype.equals("d-trip-accept")) {
                GenerateNotification(messageBody.get("body"));
                doBindService();
            } else if (ntype.equals("d-trip-reject")) {
                doBindService();
            } else if (ntype.equals("d-start-trip")) {
                GenerateNotification(messageBody.get("body"));
                doBindService();
            } else if (ntype.equals("d-end-trip")) {
                GenerateNotification(messageBody.get("body"));
                doBindService();
            } else if (ntype.equals("a-global-notification")) {
                GenerateNotification(messageBody.get("body"));
            } else if (ntype.equals("c-device-change")) {
                doBindService();
            }
        }


        Log.e("hellloo", "msg aayoo");


    }

    public void noti_one(String message, PendingIntent intent) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(intent)
                .setWhen(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.mipmap.app_icon_rider);
        } else {
            builder.setSmallIcon(R.mipmap.app_icon_rider);
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }


    public void notification(String message, PendingIntent intent, String noti_url) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder nb = new NotificationCompat.Builder(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            nb.setSmallIcon(R.mipmap.app_icon_rider);
        } else {
            nb.setSmallIcon(R.mipmap.app_icon_rider);
        }
        nb.setContentTitle(getResources().getString(R.string.app_name));
        nb.setContentText(message);
        nb.setContentIntent(intent);
        nb.setSound(defaultSoundUri);
        nb.setAutoCancel(true);
        Bitmap bitmap_image = getBitmapFromURL(noti_url);

        bitmap_image = Bitmap.createScaledBitmap(bitmap_image, 500, 500, false);
        NotificationCompat.BigPictureStyle s = new NotificationCompat.BigPictureStyle().bigPicture(bitmap_image);
        s.setSummaryText(message);
        nb.setStyle(s);
        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(11221, nb.build());
    }

    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private void GenerateNotification(String message) {
        final Random rand = new Random();


        int diceRoll = rand.nextInt(6) + 1;
        Intent noti_Intent = getNotificationInent();
        PendingIntent intent = PendingIntent.getActivity(this, diceRoll, noti_Intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            noti = new NotificationHelper(this);
            builder2 = null;
            builder2 = noti.getNotification2(message, intent);

            if (builder2 != null) {
                noti.notify(1200, builder2);
            }

        } else {
            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.app_icon_rider);

            builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.app_icon_rider)
                    .setLargeIcon(Bitmap.createBitmap(icon))
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(intent)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setWhen(System.currentTimeMillis());

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, builder.build());


        }

    }


    private void getMessage() {

        if (!Chat_Activity.tripid.equals(null)) {

            FirebaseDatabase.getInstance().getReference("cars").child("trip").child(Chat_Activity.tripid).child("msg").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    for (DataSnapshot chilSnapshot : dataSnapshot.getChildren()) {

                        Log.e("Valueee", "" + chilSnapshot.child("message").getValue());


                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    Intent sendintents(Intent intent) {

        return intent;
    }


    void doBindService() {
        if (!mIsBound) {
            bindService(sendintents(new Intent(context, DialogService.class)), mConnection, Context.BIND_AUTO_CREATE);
            mIsBound = true;
        }
    }


    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mBoundService = ((DialogService.LocalBinder) service).getService();
            mBoundService.createDialogIn(1000);
            doUnbindService();
        }

        public void onServiceDisconnected(ComponentName className) {
            mBoundService = null;
        }
    };

    void doUnbindService() {
        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    public Intent getNotificationInent() {
        Intent noti_Intent = null;

        if (ntype.equals("d-start-trip")) {
            noti_Intent = new Intent(this, LiveTripActivity.class);
            noti_Intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else if (ntype.equals("d-end-trip")) {
            noti_Intent = new Intent(this, Payment.class);
            noti_Intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else if (ntype.equals("d-trip-accept")) {
            noti_Intent = new Intent(this, Current_tripDemo.class);
            noti_Intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        } else if (ntype.equals("d-trip-reject")) {
            noti_Intent = new Intent(this, DriverList.class);
            noti_Intent.putExtra("driverid", driverid);
            noti_Intent.putExtra("cartype", ctype);
            noti_Intent.putExtra("tripid", tripid);
            noti_Intent.putExtra("rej", "1");
            noti_Intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else if (ntype.equals("a-global-notification")) {
            noti_Intent = new Intent(this, Nullable.class);
            noti_Intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else if (ntype.equals("c-device-change")) {
            noti_Intent = new Intent(this, First_Screen.class);
            noti_Intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            noti_Intent = new Intent(this, Dashboard.class);
            noti_Intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return noti_Intent;
    }


}
