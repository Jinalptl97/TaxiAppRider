package com.taxibookingrider.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.taxibookingrider.Activity.Chat_Activity;
import com.taxibookingrider.Adapter.ChatAdapter;
import com.taxibookingrider.Model.Chat;
import com.taxibookingrider.MyApplication;
import com.taxibookingrider.R;
import com.taxibookingrider.Utils.Pref_Master;

import java.util.ArrayList;

/**
 * Created by Ankita on 4/2/2018.
 */

public class MessageData extends Fragment {

    ArrayList<Chat> chatArrayList = new ArrayList<>();
    EditText sendmessage;
    RecyclerView chatlist;
    ImageView send_button;
    Context context;
    ChatAdapter chatRecyclerAdapter;
    Pref_Master pref_master;
    String tripId,driverId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fregment_message, container, false);
        context=getActivity();
        chatlist = view.findViewById(R.id.chatlist);
        send_button = view.findViewById(R.id.send_button);
        sendmessage = view.findViewById(R.id.chatmsg);
        pref_master = new Pref_Master(context);


      /*  Bundle mArgs = getArguments();
        tripId = mArgs.getString("tripId");
        driverId = mArgs.getString("driverid");
        Log.e("the tripvalue",":::"+tripId);
        Log.e("the drivervalue",":::"+driverId);*/

       /* msgsendbutton = view.findViewById(R.id.msgsendbutton);
        sendermessage = view.findViewById(R.id.sendermessage);*/

        LinearLayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        chatlist.setLayoutManager(layoutManager);

        chatRecyclerAdapter = new ChatAdapter(context.getApplicationContext(), chatArrayList);
        chatlist.setAdapter(chatRecyclerAdapter);

        getMessage();

        sendmessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().startsWith(" ")) {
                    String result = s.toString().replaceAll(" ", "");
                    if (!s.toString().equals(result)) {
                        sendmessage.setText(result);
                        sendmessage.setSelection(result.length());
                        // alert the user
                    }
                }
            }
        });

        Log.e("RiderToken", FirebaseInstanceId.getInstance().getToken());

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sendmessage.getText().toString().trim().length() == 0) {

                } else {
                    sendMessage();
                   /* Intent i = new Intent(context.getApplicationContext(), Chat_Activity.class);
                    i.putExtra("tripid", tripId);
                    i.putExtra("driverid", driverId);
                    startActivity(i);*/
                    sendmessage.setText("");
                }


            }
        });





        return view;
    }

    private void sendMessage() {
        String message = sendmessage.getText().toString();
        String receiver = pref_master.getDriverid();
        String sender = pref_master.getUID();
        Chat chat = new Chat(sender,
                receiver,
                message,
                System.currentTimeMillis());
        String key = FirebaseDatabase.getInstance().getReference("cars").child("trip").child(pref_master.getTripid()).child("msg").push().getKey();
        FirebaseDatabase.getInstance().getReference("cars").child("trip").child(pref_master.getTripid()).child("msg").child(String.valueOf(key)).setValue(chat);


        /*chatRecyclerAdapter.notifyDataSetChanged();
         */

    }

    private void getMessage() {
        Log.e("getMessage", pref_master.getStr_tripId());
        FirebaseDatabase.getInstance().getReference("cars").child("trip").child(pref_master.getTripid()).child("msg").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                chatArrayList.clear();

                for (DataSnapshot chilSnapshot : dataSnapshot.getChildren()) {

                    chatlist.scrollToPosition(chatArrayList.size());
                    Chat chat = new Chat();
                    chat.setMessage(String.valueOf(chilSnapshot.child("message").getValue()));
                    chat.setReceiver(String.valueOf(chilSnapshot.child("receiver").getValue()));
                    chat.setSender(String.valueOf(chilSnapshot.child("sender").getValue()));
                    chatArrayList.add(chat);
                    if (chatArrayList.size() >= 1) {
                        chatlist.setVisibility(View.VISIBLE);
                    }
                    chatRecyclerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        MyApplication.getInstance().trackScreenView("MessageData");
    }
}
