package com.reminder.touchListeners;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by sHIVAM on 6/23/2016.
 */
public class SimpleItemTouchHelperCallabck extends android.support.v7.widget.helper.ItemTouchHelper.Callback {

    private com.reminder.touchListeners.ItemTouchHelper touchHelper;

    public SimpleItemTouchHelperCallabck(com.reminder.touchListeners.ItemTouchHelper touchHelper) {
        this.touchHelper = touchHelper;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = android.support.v7.widget.helper.ItemTouchHelper.UP |
                android.support.v7.widget.helper.ItemTouchHelper.DOWN;
        int swipeFlags = android.support.v7.widget.helper.ItemTouchHelper.START |
                android.support.v7.widget.helper.ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        touchHelper.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        Log.d("DB",position+"");
        touchHelper.onItemDismiss(position);
    }


}
