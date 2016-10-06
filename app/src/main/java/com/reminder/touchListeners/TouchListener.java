package com.reminder.touchListeners;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by sHIVAM on 6/21/2016.
 */
public class TouchListener implements View.OnTouchListener {

    private final GestureDetectorCompat mDetector;
    private Context mContext;

    public TouchListener(Context context) {
        this.mContext = context;
        mDetector = new GestureDetectorCompat(mContext, new GestureListener());
    }

    public boolean onTouch(View v, MotionEvent event) {
        mDetector.onTouchEvent(event);
        return true;
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }



    class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String TAG = "gestures";
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            Log.d("gestures: ", e1.toString()+e2.toString()+velocityX+velocityY+"");
            boolean result = false;
            try {
                float diffX = Math.abs(e1.getX() - e2.getX());
                float diffY = Math.abs(e1.getY() - e2.getY());
                Log.d("gestures", diffX+" "+diffY);
                if(diffX > diffY) {
                    if(diffX > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if(diffX > 0)   onSwipeRight();
                        else    onSwipeLeft();
                        result = true;
                    }
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                }
                result = true;

            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }


    }
}


