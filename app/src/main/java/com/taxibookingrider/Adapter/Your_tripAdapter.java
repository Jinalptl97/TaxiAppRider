package com.taxibookingrider.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.taxibookingrider.Activity.Trip_Detail;
import com.taxibookingrider.Controller.Constants;
import com.taxibookingrider.Controller.Tahoma;
import com.taxibookingrider.Controller.TahomaBold;
import com.taxibookingrider.Model.YouTripModel;
import com.taxibookingrider.R;
import com.taxibookingrider.Utils.Pref_Master;

import java.util.ArrayList;

/**
 * Created by Admin on 1/8/2018.
 */

public class Your_tripAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;
    Context context;
    ArrayList<YouTripModel> array_list = new ArrayList<>();
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    Pref_Master pref_master;


    public Your_tripAdapter(Context context, ArrayList<YouTripModel> array_list) {
        this.context = context;
        this.array_list = array_list;
        pref_master=new Pref_Master(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TYPE_MOVIE) {
            return new Viewholder(inflater.inflate(R.layout.row_your_trip_list, parent, false));
        } else {
            return new LoadHolder(inflater.inflate(R.layout.loaderview, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

        if (getItemViewType(position) == TYPE_MOVIE) {
            ((Viewholder) holder).bindData(array_list.get(position), position);
        }


    }

    @Override
    public int getItemCount() {
        return array_list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (array_list.get(position).type.equals("load")) {
            return TYPE_LOAD;
        } else {
            return TYPE_MOVIE;
        }
    }

    public class Viewholder extends RecyclerView.ViewHolder {


        Tahoma date, time, from, to, payment, cartype , tripstatus,trip_no;
        TahomaBold fare;
        LinearLayout trip;

        public Viewholder(View view) {
            super(view);

            date = view.findViewById(R.id.date);
            time = view.findViewById(R.id.time);
            from = view.findViewById(R.id.from);
            to = view.findViewById(R.id.to);
            payment = view.findViewById(R.id.payment);
            cartype = view.findViewById(R.id.cartype);
            fare = view.findViewById(R.id.fare);
            trip = view.findViewById(R.id.trip);
            tripstatus = view.findViewById(R.id.tripstatus);
            trip_no=view.findViewById(R.id.trip_no);
        }

        void bindData(final YouTripModel postHolder, int position) {
            date.setText(postHolder.getDate());
            time.setText(postHolder.getTime());
            from.setText(postHolder.getFrom());
            to.setText(postHolder.getTo());
            payment.setText(postHolder.getPayment());
            /*cartype.setText(postHolder.getCartype());*/
            if(postHolder.getCartype().equals(Constants.FOURSETER))
            {
                cartype.setText(R.string.sedan);
            }else if(postHolder.getCartype().equals(Constants.SIXSETER))
            {
                cartype.setText(R.string.suv);
            }else if(postHolder.getCartype().equals(Constants.RENTAL))
            {
                cartype.setText(R.string.mini);
            }
            fare.setText("$"+postHolder.getFare());
            tripstatus.setText(postHolder.getFlag());
            trip_no.setText(postHolder.getTripid());
            trip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, Trip_Detail.class);
                    i.putExtra("date", postHolder.getDate());
                    i.putExtra("time", postHolder.getTime());
                    i.putExtra("from", postHolder.getFrom());
                    i.putExtra("to", postHolder.getTo());
                    i.putExtra("fare", postHolder.getFare());
                    i.putExtra("wcharge", postHolder.getWamt());
                    i.putExtra("finalfare", postHolder.getFare());
                    i.putExtra("flat", postHolder.getFlat());
                    i.putExtra("flng", postHolder.getFlong());
                    i.putExtra("tlat", postHolder.getTlat());
                    i.putExtra("tlng", postHolder.getTlong());
                    i.putExtra("trip_no",postHolder.getTripid());
                    context.startActivity(i);
                }
            });

        }

    }

    static class LoadHolder extends RecyclerView.ViewHolder {
        public LoadHolder(View itemView) {
            super(itemView);
        }
    }


    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    public void notifyDataChanged() {
        notifyDataSetChanged();
        isLoading = false;
    }


    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

}

