package com.taxibookingrider.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taxibookingrider.Activity.Dashboard;
import com.taxibookingrider.MyApplication;
import com.taxibookingrider.R;

/**
 * Created by Admin on 1/8/2018.
 */

public class Legal extends Fragment {

    Context context;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_legal, container, false);
        context = getActivity();
        TextView headertext = getActivity().findViewById(R.id.headertext);
        headertext.setText("Legal");


        return view;

    }

    @Override
    public void onResume() {

        super.onResume();

        if (getView() == null) {
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_BACK) {
                    RelativeLayout maplayout, locationlayout, choosedialog;
                    maplayout = (RelativeLayout) getActivity().findViewById(R.id.maplayout);
                    maplayout.setVisibility(View.VISIBLE);

                    locationlayout = (RelativeLayout) getActivity().findViewById(R.id.locationlayout);
                    locationlayout.setVisibility(View.VISIBLE);

                    choosedialog = (RelativeLayout) getActivity().findViewById(R.id.chooselocationdialog);
                    choosedialog.setVisibility(View.VISIBLE);

                    TextView header = getActivity().findViewById(R.id.headertext);
                    header.setText("Home");


                 /*   Intent intent = new Intent(getActivity(), Dashboard.class);
                        startActivity(intent);*/


                   /* ImageView toplogo = getActivity().findViewById(R.id.toplogo);
                    toplogo.setVisibility(View.VISIBLE);*/


                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.remove(Legal.this);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    ft.commit();
// handle back button's click listener
                }

                return false;
            }
        });

        MyApplication.getInstance().trackScreenView("Legal");


    }


}
