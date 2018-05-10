package com.taxibookingrider.Controller;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;


import com.taxibookingrider.Activity.Current_tripDemo;
import com.taxibookingrider.Activity.Dashboard;
import com.taxibookingrider.Activity.DriverList;
import com.taxibookingrider.Activity.DriverListDemo;
import com.taxibookingrider.Activity.First_Screen;
import com.taxibookingrider.Activity.LiveTripActivity;
import com.taxibookingrider.Activity.Payment;
import com.taxibookingrider.R;
import com.taxibookingrider.Utils.Pref_Master;




public class DialogBox extends AppCompatActivity {

    public static void setAccepted(final Context context, String msg) {
        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.dialog_accepted, null);
        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog).setView(v).show();
        alert.setCancelable(false);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Tahoma textaccepeted = (Tahoma) v.findViewById(R.id.tv_msg);
        Tahoma ok = (Tahoma) v.findViewById(R.id.con_ok);

        final Notification notification = new Notification();

        textaccepeted.setText(msg);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Accepeted", "Accepted");

                NotificationManager nManager = ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
                nManager.cancelAll();
                if (alert != null && alert.isShowing()) {
                    alert.dismiss();
                }

                Intent i = new Intent(context, Current_tripDemo.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(i);
            }
        });
        FragmentManager manager = null;

        final FragmentManager finalManager = manager;
    }


    public static void setRejected(final Context context, String msg, final String driverid, final String ctype, final String tripid) {
        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.dialog_accepted, null);
        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog).setView(v).show();
        alert.setCancelable(false);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Tahoma textaccepeted = (Tahoma) v.findViewById(R.id.tv_msg);
        Tahoma ok = (Tahoma) v.findViewById(R.id.con_ok);

        textaccepeted.setText(msg);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NotificationManager nManager = ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
                nManager.cancelAll();

                if (alert != null && alert.isShowing()) {
                    alert.dismiss();
                }

                Intent i = new Intent(context, DriverList.class);
                i.putExtra("driverid", driverid);
                i.putExtra("cartype", ctype);
                i.putExtra("tripid", tripid);
                i.putExtra("rej", "1");
                context.startActivity(i);


            }
        });
        FragmentManager manager = null;

        final FragmentManager finalManager = manager;
    }

    public static void setStartTrip(final Context context, String msg) {
        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.dialog_accepted, null);
        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog).setView(v).show();
        alert.setCancelable(false);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Tahoma textaccepeted = (Tahoma) v.findViewById(R.id.tv_msg);
        Tahoma ok = (Tahoma) v.findViewById(R.id.con_ok);

        textaccepeted.setText(msg);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NotificationManager nManager = ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
                nManager.cancelAll();

                if (alert != null && alert.isShowing()) {
                    alert.dismiss();
                }

                Intent i = new Intent(context, LiveTripActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
        FragmentManager manager = null;

        final FragmentManager finalManager = manager;
    }

    public static void setPayby(final Context context, String msg) {
        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.dialog_accepted, null);
        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog).setView(v).show();
        alert.setCancelable(false);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Tahoma textaccepeted = (Tahoma) v.findViewById(R.id.tv_msg);
        Tahoma ok = (Tahoma) v.findViewById(R.id.con_ok);

        textaccepeted.setText(msg);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        FragmentManager manager = null;

        final FragmentManager finalManager = manager;

    }

    public static void setEmailExits(final Context context, String msg) {
        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.dialog_accepted, null);
        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog).setView(v).show();
        alert.setCancelable(false);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Tahoma textaccepeted = (Tahoma) v.findViewById(R.id.tv_msg);
        Tahoma ok = (Tahoma) v.findViewById(R.id.con_ok);

        textaccepeted.setText(msg);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                Intent i = new Intent(context, First_Screen.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
        FragmentManager manager = null;

        final FragmentManager finalManager = manager;

    }

    public static void setNoFormat(final Context context, String msg, final Runnable runnable) {
        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.dialog_accepted, null);
        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog).setView(v).show();

        alert.setCancelable(false);
        Tahoma textaccepeted = (Tahoma) v.findViewById(R.id.tv_msg);
        Tahoma ok = (Tahoma) v.findViewById(R.id.con_ok);

        textaccepeted.setText(msg);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                runnable.run();

            }
        });
        FragmentManager manager = null;

        final FragmentManager finalManager = manager;

    }

    public static void setNoData(final Context context, String msg, final Runnable runnable) {
        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.dialog_accepted, null);
        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog).setView(v).show();
        alert.setCancelable(false);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Tahoma textaccepeted = (Tahoma) v.findViewById(R.id.tv_msg);
        Tahoma ok = (Tahoma) v.findViewById(R.id.con_ok);

        textaccepeted.setText(msg);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                runnable.run();
            }
        });
        FragmentManager manager = null;

        final FragmentManager finalManager = manager;

    }


    public static void setPaybyRegistor(final Context context, String msg) {
        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.dialog_accepted, null);
        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog).setView(v).show();
        alert.setCancelable(false);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Tahoma textaccepeted = (Tahoma) v.findViewById(R.id.tv_msg);
        Tahoma ok = (Tahoma) v.findViewById(R.id.con_ok);

        textaccepeted.setText(msg);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                Pref_Master pref_master = new Pref_Master(context);
                pref_master.clear_pref();
                pref_master.setAuto(1);
                Intent i = new Intent(context, First_Screen.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
        FragmentManager manager = null;

        final FragmentManager finalManager = manager;

    }

    public static void setUpdateApp(final Context context, String msg) {
        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.dialog_accepted, null);
        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog).setView(v).show();
        alert.setCancelable(false);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Tahoma textaccepeted = (Tahoma) v.findViewById(R.id.tv_msg);
        Tahoma ok = (Tahoma) v.findViewById(R.id.con_ok);

        textaccepeted.setText(msg);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                String urll = "https://play.google.com/store/apps/details?id=com.joyrider";
                Intent sendIntent = new Intent();
                sendIntent.setType("text/plain");
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, urll);
                context.startActivity(sendIntent);

            }
        });
        FragmentManager manager = null;

        final FragmentManager finalManager = manager;

    }


    public static void setNoDriver(final Context context, String msg) {
        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.dialog_nodriver, null);
        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog).setView(v).show();
        alert.setCancelable(false);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Tahoma textaccepeted = (Tahoma) v.findViewById(R.id.tv_msg);
        Tahoma ok = (Tahoma) v.findViewById(R.id.con_ok);

        textaccepeted.setText(msg);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
               /* runnable.run();*/
            }
        });
        FragmentManager manager = null;

        final FragmentManager finalManager = manager;

    }

    public static void setNoDriverDriverList(final Context context, String msg) {
        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.dialog_nodriver, null);
        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog).setView(v).show();
        alert.setCancelable(false);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Tahoma textaccepeted = (Tahoma) v.findViewById(R.id.tv_msg);
        Tahoma ok = (Tahoma) v.findViewById(R.id.con_ok);

        textaccepeted.setText(msg);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                Intent i = new Intent(context, Dashboard.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            }
        });
        FragmentManager manager = null;

        final FragmentManager finalManager = manager;

    }


    public static void setEndTrip(final Context context, String msg) {
        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.dialog_accepted, null);
        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog).setView(v).show();
        alert.setCancelable(false);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Tahoma textaccepeted = (Tahoma) v.findViewById(R.id.tv_msg);
        Tahoma ok = (Tahoma) v.findViewById(R.id.con_ok);

        textaccepeted.setText(msg);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NotificationManager nManager = ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
                nManager.cancelAll();

                Intent i = new Intent(context, Payment.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
        FragmentManager manager = null;

        final FragmentManager finalManager = manager;
    }

    /*public static void setDriverList(final Context context, String msg) {
        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.dialog_driverlist, null);
        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog).setView(v).show();
        alert.setCancelable(false);
        Tahoma textaccepeted = (Tahoma) v.findViewById(R.id.tv_msg);
        Tahoma ok = (Tahoma) v.findViewById(R.id.con_ok);
        textaccepeted.setText(msg);
        RotateLoading rotateLoading = v.findViewById(R.id.rotateloading);
        rotateLoading.start();

    }*/

    public static void setAutoStart(final Context context, String msg, final Runnable runnable) {
        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.dialog_accepted, null);
        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog).setView(v).show();
        alert.setCancelable(false);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Tahoma textaccepeted = (Tahoma) v.findViewById(R.id.tv_msg);
        Tahoma ok = (Tahoma) v.findViewById(R.id.con_ok);
        final Pref_Master pref;
        pref = new Pref_Master(context);

        textaccepeted.setText(msg);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.setAuto(1);
                alert.dismiss();
                runnable.run();
            }
        });
        FragmentManager manager = null;
        final FragmentManager finalManager = manager;

    }


}
