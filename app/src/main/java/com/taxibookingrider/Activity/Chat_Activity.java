package com.taxibookingrider.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.taxibookingrider.Adapter.ChatAdapter;
import com.taxibookingrider.Model.Chat;
import com.taxibookingrider.MyApplication;
import com.taxibookingrider.R;
import com.taxibookingrider.Utils.Pref_Master;

import java.util.ArrayList;
import java.util.Random;


public class Chat_Activity extends AppCompatActivity {

    String driverid, driverpic;
    public static String tripid;
    EditText chatmsged;
    ImageView sendbutton;
    RecyclerView chatlist;
    Pref_Master pref_master;
    ArrayList<Chat> chatArrayList = new ArrayList<>();
    ChatAdapter chatRecyclerAdapter;
    TextView usertextname;
    ImageView notify_bar;
    public static String drivername;

    String refreshedToken = "";//add your user refresh tokens who are logged in with firebase.

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_);

        chatmsged = findViewById(R.id.chatmsg);
        sendbutton = findViewById(R.id.send_button);
        notify_bar = findViewById(R.id.notify_bar);
        usertextname = findViewById(R.id.usertextname);
        chatlist = findViewById(R.id.chatlist);
        pref_master = new Pref_Master(this);

        Intent intent = getIntent();
        tripid = intent.getStringExtra("tripid");
        driverid = intent.getStringExtra("driverid");
        driverpic = intent.getStringExtra("driverpic");


        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        chatlist.setLayoutManager(layoutManager);


        usertextname.setText(pref_master.getDriverfname() + " " + pref_master.getDriverlname());

        notify_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        chatmsged.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().startsWith(" ")){
                    String result = s.toString().replaceAll(" ", "");
                    if (!s.toString().equals(result)) {
                        chatmsged.setText(result);
                        chatmsged.setSelection(result.length());
                        // alert the user
                    }
                }
            }
        });

        chatRecyclerAdapter = new ChatAdapter(getApplicationContext(), chatArrayList);
        chatlist.setAdapter(chatRecyclerAdapter);


        getMessage();

        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chatmsged.getText().toString().trim().length() == 0) {

                } else {
                    sendMessage();
                    chatmsged.setText("");
                }
            }
        });

        MyApplication.getInstance().trackScreenView("Chat_Activity");

    }


    private void sendMessage() {
        chatArrayList.clear();
        String message = chatmsged.getText().toString();
        String receiver = driverid;
        String sender = pref_master.getUID();
        Chat chat = new Chat(sender,
                receiver,
                message,
                System.currentTimeMillis());
        String key = FirebaseDatabase.getInstance().getReference("cars").child("trip").child(tripid).child("msg").push().getKey();
        FirebaseDatabase.getInstance().getReference("cars").child("trip").child(tripid).child("msg").child(String.valueOf(key)).setValue(chat);

        /*chatRecyclerAdapter.notifyDataSetChanged();*/

    }


    private void getMessage() {
        Log.e("getMessage", tripid);
        FirebaseDatabase.getInstance().getReference("cars").child("trip").child(tripid).child("msg").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                chatArrayList.clear();
                for (DataSnapshot chilSnapshot : dataSnapshot.getChildren()) {
                    chatlist.scrollToPosition(chatArrayList.size());
                    Log.e("Valueee", "" + chilSnapshot.child("message").getValue());
                    Chat chat = new Chat();
                    chat.setMessage(String.valueOf(chilSnapshot.child("message").getValue()));
                    chat.setReceiver(String.valueOf(chilSnapshot.child("receiver").getValue()));
                    chat.setSender(String.valueOf(chilSnapshot.child("sender").getValue()));
                    chatArrayList.add(chat);
                    chatRecyclerAdapter.notifyDataSetChanged();


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private int getRandomNumber(int min, int max) {
        return (new Random()).nextInt((max - min) + 1) + min;
    }


    protected String getSaltString() {
        String SALTCHARS = "abcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 12) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }

        return salt.toString();
    }
}

