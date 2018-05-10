package com.taxibookingrider.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.taxibookingrider.Controller.Tahoma;
import com.taxibookingrider.Model.Reason;
import com.taxibookingrider.R;

import java.util.ArrayList;

/**
 * Created by Admin on 1/9/2018.
 */

public class Reason_adapter extends RecyclerView.Adapter<Reason_adapter.Viewholder> {
    Context context;
    ArrayList<Reason> array_list = new ArrayList<>();
    public static String reason = "";

    public Reason_adapter(Context context, ArrayList<Reason> array_list) {
        this.context = context;
        this.array_list = array_list;

    }

    @Override
    public Reason_adapter.Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_reason_list, parent, false);
        return new Reason_adapter.Viewholder(itemView);
    }

    @Override
    public void onBindViewHolder(Reason_adapter.Viewholder holder, final int position) {

        holder.reason.setText(array_list.get(position).getReason());


        holder.whole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reason = array_list.get(position).getReason();

                for (int i = 0; i < array_list.size(); i++) {
                    Reason m = array_list.get(i);
                    m.setSelected((i == position) ? "1" : "0");
                }
                notifyDataSetChanged();

            }
        });

        holder.whole.setBackgroundResource(array_list.get(position).getSelected().equals("0") ? R.color.White : R.color.bg_color);

    }

    @Override
    public int getItemCount() {
        return array_list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {


        Tahoma reason;
        LinearLayout whole;

        public Viewholder(View view) {
            super(view);

            reason = view.findViewById(R.id.reason);
            whole = view.findViewById(R.id.wholelinear);

        }
    }
}
