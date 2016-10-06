package com.reminder.customAdapters;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.reminder.data.ReminderContract;

/**
 * Created by sHIVAM on 6/20/2016.
 */
public abstract class CursorAdapter<VH extends RecyclerView.ViewHolder> extends
                                            RecyclerView.Adapter<VH> {
    private Context mContext;
    private Cursor mCursor;
    private boolean dataValid;
    private int RowIdColumn;
    private DataSetObserver dataSetObserver;

    public CursorAdapter(Context mContext, Cursor mCursor) {
        this.mContext = mContext;
        this.mCursor = mCursor;
        this.dataValid = mCursor!=null;
        this.RowIdColumn = dataValid?mCursor.getColumnIndex(ReminderContract._ID):-1;
        this.dataSetObserver = new NotifyingDataSetObserver();
        assert mCursor != null;
        mCursor.registerDataSetObserver(dataSetObserver);

    }

    public Cursor getCursor() {
        return mCursor;
    }

    @Override
    public int getItemCount() {
        if(dataValid && mCursor!=null)  return mCursor.getCount();
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if(dataValid && mCursor!=null && mCursor.moveToPosition(position)) {
            return mCursor.getLong(RowIdColumn);
        }
        return 0;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    public abstract void onBindViewHolder(VH holder, Cursor cursor, int position);

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if(!dataValid)

            throw new IllegalStateException("Cursor is invalid");
        if(!mCursor.moveToPosition(position))
            throw new IllegalStateException("could not find the position: "+position);
        onBindViewHolder(holder, mCursor, position);
    }

    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if(old!=null)
            old.close();
    }

    private Cursor swapCursor(Cursor cursor) {
        if(cursor == mCursor) return null;
        final Cursor oldCursor = mCursor;
        if(oldCursor!=null && dataSetObserver!=null) {
            oldCursor.unregisterDataSetObserver(dataSetObserver);
        }
        mCursor = cursor;
        if(mCursor!=null) {
            if(dataSetObserver!=null) {
                mCursor.registerDataSetObserver(dataSetObserver);
            }
            RowIdColumn = mCursor.getColumnIndex(ReminderContract._ID);
            dataValid = true;
            notifyDataSetChanged();
        }
        else {
            dataValid = false;
            RowIdColumn = -1;
            notifyDataSetChanged();
        }
        return oldCursor;
    }

    private class NotifyingDataSetObserver extends DataSetObserver{
        @Override
        public void onChanged() {
            super.onChanged();
            dataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            dataValid = false;
            notifyDataSetChanged();
        }
    }


}
