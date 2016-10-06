package com.reminder.touchListeners;

import android.view.View;

/**
 * Created by sHIVAM on 6/22/2016.
 */
public interface ClickListener {
    void onClick(View view, int position);
    void onLongClick(View view, int position);
}
