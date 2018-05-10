package com.taxibookingrider.Activity;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Ankita on 4/2/2018.
 */

public class FixedBottomViewBehavior extends CoordinatorLayout.Behavior<View> {

    public FixedBottomViewBehavior() {
    }

    public FixedBottomViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        //making our bottom view depends on ViewPager (or whatever that have appbar_scrolling_view_behavior behavior)
        return dependency instanceof ViewPager;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        if (ViewCompat.isLaidOut(parent)) {
            //attach our bottom view to the bottom of CoordinatorLayout
            child.setY(parent.getBottom() - child.getHeight());

            //set bottom padding to the dependency view to prevent bottom view from covering it
            dependency.setPadding(dependency.getPaddingLeft(), dependency.getPaddingTop(),
                    dependency.getPaddingRight(), child.getHeight());
        }
        return false;
    }
}
