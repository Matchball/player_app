package com.example.shree.player_nav_drawer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.DatePicker;

/**
 * Created by shree on 01/09/2017.
 */

    public class CustomDP extends DatePicker {

        public CustomDP(Context context) {
            super(context);
        }

        public CustomDP(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public CustomDP(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            ViewParent parentView = getParent();

            if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
                if (parentView != null) {
                    parentView.requestDisallowInterceptTouchEvent(true);
                }
            }

            return false;
        }

}
