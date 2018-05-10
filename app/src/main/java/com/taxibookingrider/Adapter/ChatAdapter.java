package com.taxibookingrider.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.taxibookingrider.Activity.Chat_Activity;
import com.taxibookingrider.Controller.Tahoma;
import com.taxibookingrider.Model.Chat;
import com.taxibookingrider.R;
import com.taxibookingrider.Utils.Pref_Master;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 12/5/2017.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    Context context;
    ArrayList<Chat> array_list = new ArrayList<>();
    Pref_Master pref_master;

    public ChatAdapter(Context context, ArrayList<Chat> array_list) {
        this.context = context;
        this.array_list = array_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_chat_list, parent, false);


        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        pref_master = new Pref_Master(context);

        Chat_Activity.drivername = array_list.get(position).getReceiver();

        if (array_list.get(position).getSender().equals(pref_master.getUID())) {
            holder.sender.setText(array_list.get(position).getMessage());

            Picasso.with(context)
                    .load(pref_master.getAwsurl() + pref_master.getUID() + "/" + pref_master.getUserimage()) //extract as User instance method
                    .placeholder(R.drawable.personal)
                    .into(holder.user_sender_pic);


            holder.ll_sender.setVisibility(View.VISIBLE);
            holder.ll_receiver.setVisibility(View.GONE);
        } else {
            holder.receiver.setText(array_list.get(position).getMessage());


            Picasso.with(context)
                    .load(pref_master.getDriverprofilepic()) //extract as User instance method
                    .placeholder(R.drawable.personal)
                    .into(holder.user_receiver_profile);


            holder.ll_sender.setVisibility(View.GONE);
            holder.ll_receiver.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {


        return array_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView user_sender_pic, user_receiver_profile;
        Tahoma receiver, sender;
        LinearLayout ll_receiver, ll_sender;


        public ViewHolder(View itemView) {
            super(itemView);

            user_receiver_profile = itemView.findViewById(R.id.user_receiver_profile);
            user_sender_pic = itemView.findViewById(R.id.user_sender_pic);
            receiver = itemView.findViewById(R.id.receiver);
            sender = itemView.findViewById(R.id.sender);
            ll_receiver = itemView.findViewById(R.id.ll_receiver);
            ll_sender = itemView.findViewById(R.id.ll_sender);

        }
    }
}
