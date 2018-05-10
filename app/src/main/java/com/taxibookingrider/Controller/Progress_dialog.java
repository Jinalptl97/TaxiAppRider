package com.taxibookingrider.Controller;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.taxibookingrider.R;

/**
 * Created by Admin on 4/6/2018.
 */

public class Progress_dialog extends DialogFragment {

    Dialog mDialog;

    GraduallyTextView txt_load;
    ImageView img_gif;
    Context context;
    RelativeLayout rr_dialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (mDialog == null) {
            mDialog = new Dialog(getActivity(), R.style.cart_dialog);
            mDialog.setContentView(R.layout.progress_dialog_layout);
            context = getActivity();

            View view = mDialog.getWindow().getDecorView();
            // mDialog.requestWindowFeature(STYLE_NO_TITLE);
            rr_dialog = (RelativeLayout) view.findViewById(R.id.rr_dialog);
            rr_dialog.setClickable(false);
            setCancelable(false);
            txt_load = (GraduallyTextView) view.findViewById(R.id.txt_load);
            img_gif = (ImageView) view.findViewById(R.id.img_gif);

            Glide.with(context)
                    .load(R.drawable.gifimage111)
                    .asGif()
                    .placeholder(R.drawable.gifimage111)
                    .crossFade()
                    .into(img_gif);


        }
        return mDialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        txt_load.startLoading();

    }

    @Override
    public void onPause() {
        super.onPause();
        txt_load.stopLoading();


    }


}