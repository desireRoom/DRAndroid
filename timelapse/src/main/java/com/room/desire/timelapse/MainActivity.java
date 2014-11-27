package com.room.desire.timelapse;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.UnderlineSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.room.desire.common.DRWeakHandler;


public class MainActivity extends Activity implements DRWeakHandler.IHandler, View.OnTouchListener {

    private TextView mTimeNumberTV, mMaximTopTV, mMaximBottomTV;
    private TextView mStartBtn, mStopBtn, mResetBtn;

    private long mStartTime;
    private int mDiffSec;
    private DRWeakHandler mHandler = new DRWeakHandler(this);

    private MainService.TimeLapseBinder mService;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            SU.pt("main activity is on service Disconnected!");
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SU.pt("main activity is before on service connected!");
            mService = (MainService.TimeLapseBinder) service;
            refreshViewWhenReBind();
            SU.pt("main activity is after on service connected! && is playing = " + mService.isStarted());
        }
    };

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent start = new Intent(this, MainService.class);
        startService(start);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mTimeNumberTV = (TextView) findViewById(R.id.t_time_number);
        mMaximTopTV = (TextView) findViewById(R.id.t_maxim_top);
        mMaximBottomTV = (TextView) findViewById(R.id.t_maxim_bottom);

        mStartBtn = (TextView) findViewById(R.id.start_time_btn);
        mStartBtn.setOnTouchListener(this);
        mResetBtn = (TextView) findViewById(R.id.reset_time_btn);
        mResetBtn.setOnTouchListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initMaximView();
        mStartTime = TLSPUtils.getStartTime(this, 0);
        if (mStartTime > 0) {
            TLSPUtils.removeKey(this, TLSPUtils.STARING_TIME);
            refreshViewWhenReBind();
        } else {
            mDiffSec = TLSPUtils.getDiffSec(this, 0);
            if (mDiffSec > 0) {
                TLSPUtils.removeKey(this, TLSPUtils.Diff_SEC);
                setTimeNumber(mDiffSec);
                setButtonState(true, true);
            } else {
                setTimeNumber(0, 0, 0);
                setButtonState(true, false);
            }
        }
        connectMediaService(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mStartTime > 0) {// is playing
            TLSPUtils.setStartTime(this, mStartTime);
        } else if (mDiffSec > 0) {
            TLSPUtils.setDiffSec(this, mDiffSec);
        }
        unbindService(mConnection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if ((motionEvent.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
            int id = view.getId();
            switch (id) {
                case R.id.start_time_btn:
                    if (mService != null && mService.isStarted()) {
                        stopTick();
                    } else {
                        startTick();
                    }
                    break;
                case R.id.reset_time_btn:
                    resetTick();
                    break;

            }
            return true;
        }
        return false;
    }

    @Override
    public void handleMessage(Message msg) {

    }

    private void initMaximView() {
        String top = getString(R.string.preset_maxim_top);
        String bottom = getString(R.string.preset_maxim_bottom);
        SpannableStringBuilder ssb = new SpannableStringBuilder(top);
        ssb.setSpan(new SuperscriptSpan(), 4, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new AbsoluteSizeSpan(15, true), 4, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mMaximTopTV.setText(ssb);
        ssb = new SpannableStringBuilder(bottom);
        ssb.setSpan(new SuperscriptSpan(), 4, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new AbsoluteSizeSpan(15, true), 4, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mMaximBottomTV.setText(ssb);
    }

    private void connectMediaService(boolean startPlay) {
        Intent intent = new Intent(this, MainService.class);
        intent.putExtra("play", startPlay);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void refreshViewWhenReBind() {
        if (mService != null && mService.isStarted()) {
            startTick(mStartTime);
        }
    }

    private void setButtonState(boolean isIdle, boolean reseted) {
        mResetBtn.setEnabled(reseted);
        if (isIdle) {
            mStartBtn.setText(R.string.btn_start);
            mStartBtn.setTextColor(getResources().getColor(R.color.green));
        } else {
            mStartBtn.setText(R.string.btn_stop);
            mStartBtn.setTextColor(getResources().getColor(R.color.red));
        }
    }

    private void setTimeNumber(int diffSec) {
        int hour = diffSec / 3600;
        int min = diffSec % 3600 / 60;
        int sec = diffSec % 60;
        setTimeNumber(hour, min, sec);
    }

    private void setTimeNumber(int hour, int min, int sec) {
        StringBuilder sb = new StringBuilder();
        if (hour < 0 || min < 0 || sec < 0) {
            sb.append("00:00");
        } else {
            if (hour > 0) {
                if (hour < 10) {
                    sb.append("0");
                }
                sb.append(String.valueOf(hour)).append(":");
            } else {
                // TODO set time number text size
            }
            if (min < 10) {
                sb.append("0");
            }
            sb.append(String.valueOf(min)).append(":");
            if (sec < 10) {
                sb.append("0");
            }
            sb.append(String.valueOf(sec));
        }
        mTimeNumberTV.setText(sb.toString());
    }

    private Runnable mCountRunnable = new Runnable() {
        @Override
        public void run() {
            if (mStartTime <= 0)
                return;

            long now = System.currentTimeMillis();
            long diff = now - mStartTime;
            int diffSec = (int) (diff / 1000);
            setTimeNumber(diffSec);
            mDiffSec = diffSec;

            mHandler.postDelayed(this, 1000 - diff % 1000);
        }
    };

    private void startTick() {
        long start = System.currentTimeMillis();
        if (mDiffSec > 0) {
            start -= mDiffSec * 1000;
        }

        startTick(start);
    }

    private void startTick(long start) {
        if (start <= 0) {
            startTick();
        }

        mStartTime = start;
        mHandler.post(mCountRunnable);
        setButtonState(false, true);
        if (mService != null) {
            mService.start();
        }
    }

    private void stopTick() {
        mHandler.removeCallbacks(mCountRunnable);
        mStartTime = 0;
        setButtonState(true, mDiffSec > 0);
        if (mService != null) {
            mService.stop();
        }
    }

    private void resetTick() {
        mHandler.removeCallbacks(mCountRunnable);
        mDiffSec = 0;
        setTimeNumber(0, 0, 0);
        if (mStartTime > 0) {
            startTick();
        } else {
            setButtonState(true, false);
        }
    }
}
