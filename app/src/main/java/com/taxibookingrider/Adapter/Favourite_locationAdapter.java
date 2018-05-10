package com.taxibookingrider.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.taxibookingrider.Controller.Tahoma;
import com.taxibookingrider.Fragment.Favourite_location;
import com.taxibookingrider.Model.FavLocation;
import com.taxibookingrider.R;

import java.util.ArrayList;

/**
 * Created by Admin on 1/8/2018.
 */

public class Favourite_locationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;
    Context context;
    ArrayList<FavLocation> array_list = new ArrayList<>();
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;


    public Favourite_locationAdapter(Context context, ArrayList<FavLocation> array_list) {
        this.context = context;
        this.array_list = array_list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TYPE_MOVIE) {
            return new Viewholder(inflater.inflate(R.layout.row_favourite_location, parent, false));
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


        Tahoma address;
        RelativeLayout delete;

        public Viewholder(View view) {
            super(view);

            address = (Tahoma) view.findViewById(R.id.address);
            delete = (RelativeLayout) view.findViewById(R.id.delete);

        }

        void bindData(final FavLocation postHolder, final int position) {

            address.setText(postHolder.getAddress());

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new Favourite_location().RemoveTrip(position, context);

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
