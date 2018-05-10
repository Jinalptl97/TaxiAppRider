package com.taxibookingrider.Activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.taxibookingrider.Controller.DialogBox;
import com.taxibookingrider.Fragment.Fare;
import com.taxibookingrider.Fragment.MessageData;
import com.taxibookingrider.Fragment.ShortByETA;
import com.taxibookingrider.Fragment.ShortByPrice;
import com.taxibookingrider.Fragment.ShortByRating;
import com.taxibookingrider.MyApplication;
import com.taxibookingrider.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankita on 4/2/2018.
 */

public class Current_tripDemo extends AppCompatActivity {

    static Context context;
    ImageView notify_bar;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                DialogBox.setNoData(context,getResources().getString(R.string.location_enable), runnable);
            }
            /*Toast.makeText(context, "GPS is disabled!", Toast.LENGTH_LONG).show();*/
            else {

            }
        } else {

        }
    }

    @Override
    public void onBackPressed() {

        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current__trip);


        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        notify_bar=(ImageView)findViewById(R.id.notify_bar);

        context=this;

        adapter.addFragment(new Fare(), "Fare");
        adapter.addFragment(new MessageData(), "Message");

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        notify_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        MyApplication.getInstance().trackScreenView("Current_tripDemo");

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();




        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);

        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public static void addNotification() {
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_icon_rider);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.app_icon_rider)
                        .setLargeIcon(Bitmap.createBitmap(icon))
                        .setContentTitle("Joy Rider")
                        .setSound(defaultSoundUri)
                        .setContentText("Driver Arrived");

        Intent notificationIntent = new Intent(context,Current_tripDemo.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
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
