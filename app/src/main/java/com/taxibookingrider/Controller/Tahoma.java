package com.taxibookingrider.Controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by admin on 5/16/2017.
 */

@SuppressLint("AppCompatCustomView")
public class Tahoma extends TextView {

    private final static String CONDENSED_FONT = "fonts/Tahoma.ttf";

    public Tahoma(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Tahoma(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Tahoma(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), CONDENSED_FONT);
        setTypeface(tf);
    }


}
