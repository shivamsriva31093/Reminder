package com.reminder.touchListeners;

/**
 * Created by sHIVAM on 6/23/2016.
 */
public interface ItemTouchHelper {
    void onItemMove(int fromPos, int toPos);
    void onItemDismiss(int position);
}
