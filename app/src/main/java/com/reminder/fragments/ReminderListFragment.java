package com.reminder.fragments;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.reminder.R;
import com.reminder.alarm.ReminderManager;
import com.reminder.customAdapters.ReminderListAdapter;
import com.reminder.data.RemindersDbAdapter;
import com.reminder.touchListeners.ItemTouchHelper;
import com.reminder.touchListeners.OnStartDragListener;
import com.reminder.touchListeners.SimpleItemTouchHelperCallabck;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReminderListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReminderListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReminderListFragment extends Fragment implements OnStartDragListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = ReminderListFragment.class.getSimpleName();
    private RecyclerView recyclerView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RemindersDbAdapter dbHelper;
    private ListView listView;
    private SimpleCursorAdapter cursorAdapter;
    private ReminderListAdapter mAdapter;
    private android.support.v7.widget.helper.ItemTouchHelper touchHelper;

    public ReminderListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReminderListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReminderListFragment newInstance(String param1, String param2) {
        ReminderListFragment fragment = new ReminderListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static ReminderListFragment newInstance() {
        ReminderListFragment fragment = new ReminderListFragment();
        return fragment;
    }

    @Override
    public void onPause() {
        super.onPause();
        dbHelper.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!dbHelper.isOpen()) dbHelper.open();
    }

    @Override
    public void onActivityCreated(Bundle savedInstance) {
        super.onActivityCreated(savedInstance);
        fillList();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = RemindersDbAdapter.getInstance(getActivity());
        dbHelper.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_reminder_list, container, false);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.remindersView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            Drawable mDivider = ContextCompat.getDrawable(getActivity(),
                    android.R.drawable.divider_horizontal_textfield);
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);

                int left = parent.getPaddingLeft();
                int right = parent.getWidth() - parent.getPaddingRight();
                int childCount = parent.getChildCount();
                for(int i=0; i<childCount; i++) {
                    View child = parent.getChildAt(i);
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                    int top = child.getBottom() + params.bottomMargin;
                    int bottom = top + mDivider.getIntrinsicHeight();
                    mDivider.setBounds(left,top,right,bottom);
                    mDivider.draw(c);
                }
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if(parent.getChildAdapterPosition(view) == 0)
                    return;
                outRect.top = mDivider.getIntrinsicHeight();
            }
        });
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        //touchHelper.startDrag(viewHolder);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void fillList() {
        try{

            Cursor reminders = dbHelper.fetchAllReminders();
            Log.d("DB", reminders.getCount()+"");
            mAdapter = new ReminderListAdapter(getActivity(), reminders, this, dbHelper);
            android.support.v7.widget.helper.ItemTouchHelper.Callback callback =
                    new SimpleItemTouchHelperCallabck(new ItemTouchHelper() {
                        @Override
                        public void onItemMove(int fromPos, int toPos) {

                        }

                        @Override
                        public void onItemDismiss(int position) {
                            if(dbHelper.deleteRow(mAdapter.getItemId(position))) {
                                ReminderManager.cancelAlarm(mAdapter.getItemId(position));
                                mAdapter.notifyItemRemoved(position);
                                mAdapter.changeCursor(dbHelper.fetchAllReminders());

                            }else {
                                Toast.makeText(getActivity(), "Error in deletion!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            touchHelper = new android.support.v7.widget.helper.ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(recyclerView);
            recyclerView.setAdapter(mAdapter);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showReminders() {
        fillList();
    }
}









