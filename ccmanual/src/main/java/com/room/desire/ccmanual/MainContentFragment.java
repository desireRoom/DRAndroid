package com.room.desire.ccmanual;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by desire on 14/12/15.
 */
public class MainContentFragment extends Fragment implements View.OnClickListener {

    private TextView mTitleTv;

    private TextView mStartStopBtn;

    private TickingService.TickBinder mBinder;
    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (TickingService.TickBinder) service;
            setPlayStatus(mBinder.isPlaying());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_fragment, container, false);
        mTitleTv = (TextView) view.findViewById(R.id.content_title);
        mTitleTv.setText("Title");
        mStartStopBtn = (TextView) view.findViewById(R.id.content_tick_btn);
        mStartStopBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isBinding()) {
            setPlayStatus(mBinder.isPlaying());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mBinder != null) {
            getActivity().unbindService(mConn);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.content_tick_btn:
                if (isBinding()) {
                    boolean isPlaying = mBinder.isPlaying();
                    if (isPlaying) {
                        mBinder.stop();
                    } else {
                        mBinder.start();
                    }
                    setPlayStatus(!isPlaying);
                }
                break;
        }
    }

    private void setPlayStatus(boolean isPlaying) {
        if (isPlaying) {
            mStartStopBtn.setText("stop");
        } else {
            mStartStopBtn.setText("start");
        }
    }

    private boolean isBinding() {
        if (mBinder == null) {
            Intent intent = new Intent(getActivity(), TickingService.class);
            getActivity().bindService(intent, mConn, Context.BIND_AUTO_CREATE);
            return false;
        }
        return true;
    }
}
