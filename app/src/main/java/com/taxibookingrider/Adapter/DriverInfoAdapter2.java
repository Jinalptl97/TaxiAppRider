package com.taxibookingrider.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import com.taxibookingrider.Controller.Config;
import com.taxibookingrider.Controller.Tahoma;
import com.taxibookingrider.Activity.DriverList;
import com.taxibookingrider.Model.DriverInfo;
import com.taxibookingrider.R;
import com.taxibookingrider.Utils.Pref_Master;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by admin on 27-Jan-18.
 */

public class DriverInfoAdapter2 extends RecyclerView.Adapter<DriverInfoAdapter2.MyView> {
    Context context;
    ArrayList<DriverInfo> driverinfo_list = new ArrayList<>();
    Pref_Master pref_master;


    public DriverInfoAdapter2(Context context, ArrayList<DriverInfo> driverinfo_list) {
        this.context = context;
        this.driverinfo_list = driverinfo_list;
    }

    public class MyView extends RecyclerView.ViewHolder {

        Tahoma drivername, carmodel;
        RelativeLayout driverlayout;
        ImageView driverimg;
        RatingBar ratingbar;
        Tahoma totaleta, fare;


        public MyView(View itemView) {
            super(itemView);

            drivername = (Tahoma) itemView.findViewById(R.id.drivername);
            carmodel = (Tahoma) itemView.findViewById(R.id.carmodel);
            driverlayout = itemView.findViewById(R.id.driverlayout);
            driverimg = itemView.findViewById(R.id.driverimg);
            ratingbar = itemView.findViewById(R.id.ratingbar);
            totaleta = itemView.findViewById(R.id.totaleta);
            fare = itemView.findViewById(R.id.fare);
        }
    }


    @Override
    public DriverInfoAdapter2.MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.driverinfo, parent, false);
        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final DriverInfoAdapter2.MyView holder, final int position) {
        pref_master = new Pref_Master(context);

        Collections.sort(driverinfo_list, new Comparator<DriverInfo>() {
            @Override
            public int compare(DriverInfo o1, DriverInfo o2) {

                return (o1.getRating() > o2.getRating()) ? -1: (o1.getRating() > o2.getRating()) ? 1:0 ;
            }
        });

        int rate = Math.round(driverinfo_list.get(position).getRating());

        Log.e("Ratingss", "" + rate);

        holder.ratingbar.setRating(rate);

       /* float f = driverinfo_list.get(position).getDistance();*/
        //DecimalFormat decimalFormat = new DecimalFormat("#.##");
        //float totalfare = Float.valueOf(decimalFormat.format(f));
        float f = driverinfo_list.get(position).getDistance();
        Double value = Double.valueOf(driverinfo_list.get(position).getDistance());
        // DecimalFormat decimalFormat = new DecimalFormat("#.##");
        //  float totalfare = Float.valueOf(decimalFormat.format(f));

        Double total = (int) Math.round(value * 100) / (double) 100;
        holder.totaleta.setText(total + " Kms");

       /* holder.totaleta.setText( totalfare + " Kms");*/
        holder.fare.setText(Config.currency+ driverinfo_list.get(position).dfare + " Per Kms");



        holder.drivername.setText(driverinfo_list.get(position).getFname() + driverinfo_list.get(position).getLname());
        holder.carmodel.setText(driverinfo_list.get(position).getCarModel());

        holder.driverlayout.setBackgroundResource(driverinfo_list.get(position).getSelected().equals("0") ? R.color.White : R.color.bg_color);

        holder.driverlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("click", "click");
                DriverList.driverid = driverinfo_list.get(position).getDriverid();
                DriverList.position = position;

                pref_master.setDriverfare(String.valueOf(driverinfo_list.get(position).dfare));
                pref_master.setDriverid(driverinfo_list.get(position).getDriverid());
                pref_master.setDriverregistrationnumber(driverinfo_list.get(position).getCar_number());
                pref_master.setCtype(driverinfo_list.get(position).getCar());
                pref_master.setDrivertoken(driverinfo_list.get(position).getDevice_token());
                pref_master.setDriverprofilepic(driverinfo_list.get(position).getDriverProfilepic());
                pref_master.setDriverfname(driverinfo_list.get(position).getFname());
                pref_master.setDriverlname(driverinfo_list.get(position).getLname());


                for (int i = 0; i < driverinfo_list.size(); i++) {
                    DriverInfo m = driverinfo_list.get(i);
                    m.setSelected((i == position) ? "1" : "0");
                }

                notifyDataSetChanged();

            }
        });


        Picasso.with(context)
                .load(driverinfo_list.get(position).getDriverProfilepic())
                .placeholder(R.drawable.profilepi_drawer)
                .into(holder.driverimg);

    }

    @Override
    public int getItemCount() {
        return driverinfo_list.size();
    }

}
