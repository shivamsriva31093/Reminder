package com.reminder.customAdapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.reminder.R;
import com.reminder.data.ReminderContract;
import com.reminder.data.RemindersDbAdapter;
import com.reminder.touchListeners.ItemTouchHelper;
import com.reminder.touchListeners.OnStartDragListener;
import com.reminder.touchListeners.TouchListener;

/**
 * Created by sHIVAM on 6/20/2016.
 */
public class ReminderListAdapter extends CursorAdapter<ReminderListAdapter.ViewHolder> {

    int selectedItem = 0;
    private Context mContext;
    private OnStartDragListener dragListener;

    public ReminderListAdapter(Context mContext, Cursor mCursor) {
        super(mContext, mCursor);
        this.mContext = mContext;
    }

    public ReminderListAdapter(Context mContext, Cursor mCursor, OnStartDragListener dragListener
                        , RemindersDbAdapter dbHelper) {

        super(mContext, mCursor);
        Log.d("DB", mCursor.getCount()+"");
        this.mContext = mContext;
        this.dragListener = dragListener;

    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
                if(event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
                        return tryMoveSelection(lm, 1);
                    else if (keyCode == KeyEvent.KEYCODE_DPAD_UP)
                        return tryMoveSelection(lm, -1);
                }
                return false;
            }
        });
    }

    private boolean tryMoveSelection(RecyclerView.LayoutManager lm, int direction) {
        int nextSelectItem = selectedItem + direction;
        if (nextSelectItem >= 0 && nextSelectItem < getItemCount()) {
            notifyItemChanged(selectedItem);
            selectedItem = nextSelectItem;
            notifyItemChanged(selectedItem);
            lm.scrollToPosition(selectedItem);
            return true;
        }
        return false;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, Cursor cursor, int position) {
        holder.itemView.setSelected(selectedItem == position );
        holder.desc.setText(cursor.getString(cursor.getColumnIndex(ReminderContract.COLUMN_DESC)));
        holder.date.setText(cursor.getString(cursor.getColumnIndex(ReminderContract.COLUMN_DATE)));
        holder.time1.setText(cursor.getString(cursor.getColumnIndex(ReminderContract.COLUMN_TIME)));

//        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN)
//                    dragListener.onStartDrag(holder);
//                return false;
//            }
//        });
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.listview_reminder_layout,
                parent,
                false
        );
        return new ViewHolder(itemView);
    }

    @Override
    public Cursor getCursor() {
        return super.getCursor();
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView desc;
        public TextView time1;
        public TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            if(itemView instanceof LinearLayout) {
                if(itemView.findViewById(R.id.reminder) instanceof TextView) {
                    this.desc = (TextView) itemView.findViewById(R.id.reminder);
                }
                if(itemView.findViewById(R.id.LinLayout) instanceof LinearLayout) {
                    LinearLayout linearLayout = (LinearLayout) itemView.findViewById(R.id.LinLayout);
                    this.date = (TextView) linearLayout.findViewById(R.id.Date);
                    this.time1 = (TextView) linearLayout.findViewById(R.id.Time);
                }
                //setOnClickListener(itemView);
            }

        }
        /* If the onTouch method doesnt return true explicitly, the swipe gestures wont work unless
            clicklistener is et
         */
        private void setOnClickListener(View itemView) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemChanged(selectedItem);
                    selectedItem = getLayoutPosition();
                    Toast.makeText(mContext, getItemCount()+"", Toast.LENGTH_SHORT).show();
                    notifyItemChanged(selectedItem);
                }
            });
        }

        private void setTouchGestures(View itemView) {
            itemView.setOnTouchListener(new TouchListener(mContext) {


                public void onSwipeRight() {
                    Log.d("gestures", "swiped right!");
                }


                public void onSwipeLeft() {
                    //super.onSwipeLeft();
                    Log.d("gestures: ", "swiped left!");
                }
            });

        }




    }
}
