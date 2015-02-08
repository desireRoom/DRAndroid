package com.room.desire.ccmanual;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by desire on 14/12/15.
 */
public class ContentFragment extends Fragment implements View.OnClickListener {
    private String[] mGroups;
    private String[][] mChildren = new String[7][];

    private TextView mTitleTv;
    private View mDrawerBtn, mSettingBtn;
    private ScrollView mTenContainer;
    private WebView mOtherContainer;
    private TextView mTenActionTv, mTenAnalysisTv, mTenTargetTv, mTenCourseTv;
    private ImageView mTenImg1, mTenImg2;

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
            mBinder = null;
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Resources res = activity.getResources();
        mGroups = res.getStringArray(R.array.ccm_six_tech_strs);
        mChildren[0] = res.getStringArray(R.array.ccm_plan_strs);
        mChildren[1] = res.getStringArray(R.array.ccm_push_up_strs);
        mChildren[2] = res.getStringArray(R.array.ccm_deep_squat_strs);
        mChildren[3] = res.getStringArray(R.array.ccm_pull_up_strs);
        mChildren[4] = res.getStringArray(R.array.ccm_leg_raise_strs);
        mChildren[5] = res.getStringArray(R.array.ccm_bridge_strs);
        mChildren[6] = res.getStringArray(R.array.ccm_on_hold_strs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_fragment, container, false);
        mTitleTv = (TextView) view.findViewById(R.id.content_title);
        mTitleTv.setText("Title");
        mStartStopBtn = (TextView) view.findViewById(R.id.content_tick_btn);
        mStartStopBtn.setOnClickListener(this);
        mTenContainer = (ScrollView) view.findViewById(R.id.ccm_ten_content_container);
        mOtherContainer = (WebView) view.findViewById(R.id.ccm_other_container);
        mTenActionTv = (TextView) view.findViewById(R.id.ccm_ten_action_content);
        mTenAnalysisTv = (TextView) view.findViewById(R.id.ccm_ten_analysis_content);
        mTenTargetTv = (TextView) view.findViewById(R.id.ccm_ten_target_content);
        mTenCourseTv = (TextView) view.findViewById(R.id.ccm_ten_course_content);
        mTenImg1 = (ImageView) view.findViewById(R.id.ccm_ten_img_1);
        mTenImg2 = (ImageView) view.findViewById(R.id.ccm_ten_img_2);
        // TODO need add drawer btn & setting btn on click listener.

        showOtherContent("file:///android_asset/index.html");
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

    public void setContent(int groupId, int childId) {
        mTitleTv.setText(mGroups[groupId] + " - " + mChildren[groupId][childId]);
        switch (groupId) {
            case 0:
                showPlanView(childId);
                break;
            case 1:
                showPushUpView(childId);
                break;
            case 2:
                showDeepSquatView(childId);
                break;
            case 3:
                showPullUpView(childId);
                break;
            case 4:
                showLegRaiseView(childId);
                break;
            case 5:
                showBridgeView(childId);
                break;
            case 6:
                showOnHoldView(childId);
                break;
        }
    }

    private void showPlanView(int child) {
        switch (child) {
            case 0:
                showOtherContent("file:///android_asset/plan/1.html");
                break;
            case 1:
                showOtherContent("file:///android_asset/plan/2.html");
                break;
            case 2:
                showOtherContent("file:///android_asset/plan/3.html");
                break;
            case 3:
                showOtherContent("file:///android_asset/plan/4.html");
                break;
            case 4:
                showOtherContent("file:///android_asset/plan/5.html");
                break;
        }

    }

    private void showPushUpView(int child) {
        switch (child) {
            case 0:
                showOtherContent("file:///android_asset/all/push_up.html");
                break;
            case 1:
                showTenContent(R.array.ccm_push_up_content_1, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 2:
                showTenContent(R.array.ccm_push_up_content_2, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 3:
                showTenContent(R.array.ccm_push_up_content_3, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 4:
                showTenContent(R.array.ccm_push_up_content_4, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 5:
                showTenContent(R.array.ccm_push_up_content_5, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 6:
                showTenContent(R.array.ccm_push_up_content_6, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 7:
                showTenContent(R.array.ccm_push_up_content_7, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 8:
                showTenContent(R.array.ccm_push_up_content_8, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 9:
                showTenContent(R.array.ccm_push_up_content_9, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 10:
                showTenContent(R.array.ccm_push_up_content_10, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
        }
    }

    private void showDeepSquatView(int child) {
        switch (child) {
            case 0:
                showOtherContent("file:///android_asset/all/deep_squat.html");
                break;
            case 1:
                showTenContent(R.array.ccm_deep_squat_content_1, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 2:
                showTenContent(R.array.ccm_deep_squat_content_2, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 3:
                showTenContent(R.array.ccm_deep_squat_content_3, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 4:
                showTenContent(R.array.ccm_deep_squat_content_4, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 5:
                showTenContent(R.array.ccm_deep_squat_content_5, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 6:
                showTenContent(R.array.ccm_deep_squat_content_6, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 7:
                showTenContent(R.array.ccm_deep_squat_content_7, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 8:
                showTenContent(R.array.ccm_deep_squat_content_8, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 9:
                showTenContent(R.array.ccm_deep_squat_content_9, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 10:
                showTenContent(R.array.ccm_deep_squat_content_10, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
        }

    }

    private void showPullUpView(int child) {
        switch (child) {
            case 0:
                showOtherContent("file:///android_asset/all/pull_up.html");
                break;
            case 1:
                showTenContent(R.array.ccm_pull_up_content_1, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 2:
                showTenContent(R.array.ccm_pull_up_content_2, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 3:
                showTenContent(R.array.ccm_pull_up_content_3, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 4:
                showTenContent(R.array.ccm_pull_up_content_4, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 5:
                showTenContent(R.array.ccm_pull_up_content_5, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 6:
                showTenContent(R.array.ccm_pull_up_content_6, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 7:
                showTenContent(R.array.ccm_pull_up_content_7, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 8:
                showTenContent(R.array.ccm_pull_up_content_8, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 9:
                showTenContent(R.array.ccm_pull_up_content_9, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 10:
                showTenContent(R.array.ccm_pull_up_content_10, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
        }

    }

    private void showLegRaiseView(int child) {
        switch (child) {
            case 0:
                showOtherContent("file:///android_asset/all/leg_raise.html");
                break;
            case 1:
                showTenContent(R.array.ccm_leg_raise_content_1, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 2:
                showTenContent(R.array.ccm_leg_raise_content_2, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 3:
                showTenContent(R.array.ccm_leg_raise_content_3, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 4:
                showTenContent(R.array.ccm_leg_raise_content_4, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 5:
                showTenContent(R.array.ccm_leg_raise_content_5, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 6:
                showTenContent(R.array.ccm_leg_raise_content_6, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 7:
                showTenContent(R.array.ccm_leg_raise_content_7, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 8:
                showTenContent(R.array.ccm_leg_raise_content_8, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 9:
                showTenContent(R.array.ccm_leg_raise_content_9, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 10:
                showTenContent(R.array.ccm_leg_raise_content_10, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
        }

    }

    private void showBridgeView(int child) {
        switch (child) {
            case 0:
                showOtherContent("file:///android_asset/all/bridge.html");
                break;
            case 1:
                showTenContent(R.array.ccm_bridge_content_1, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 2:
                showTenContent(R.array.ccm_bridge_content_2, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 3:
                showTenContent(R.array.ccm_bridge_content_3, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 4:
                showTenContent(R.array.ccm_bridge_content_4, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 5:
                showTenContent(R.array.ccm_bridge_content_5, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 6:
                showTenContent(R.array.ccm_bridge_content_6, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 7:
                showTenContent(R.array.ccm_bridge_content_7, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 8:
                showTenContent(R.array.ccm_bridge_content_8, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 9:
                showTenContent(R.array.ccm_bridge_content_9, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 10:
                showTenContent(R.array.ccm_bridge_content_10, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
        }

    }

    private void showOnHoldView(int child) {
        switch (child) {
            case 0:
                showOtherContent("file:///android_asset/all/on_hold.html");
                break;
            case 1:
                showTenContent(R.array.ccm_on_hold_content_1, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 2:
                showTenContent(R.array.ccm_on_hold_content_2, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 3:
                showTenContent(R.array.ccm_on_hold_content_3, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 4:
                showTenContent(R.array.ccm_on_hold_content_4, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 5:
                showTenContent(R.array.ccm_on_hold_content_5, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 6:
                showTenContent(R.array.ccm_on_hold_content_6, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 7:
                showTenContent(R.array.ccm_on_hold_content_7, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 8:
                showTenContent(R.array.ccm_on_hold_content_8, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 9:
                showTenContent(R.array.ccm_on_hold_content_9, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
            case 10:
                showTenContent(R.array.ccm_on_hold_content_10, R.drawable.ic_launcher, R.drawable.ic_launcher);
                break;
        }

    }

    private void showOtherContent(String url) {
        // mWebView.loadUrl("file:///android_asset/faq/index.html");
        mOtherContainer.setVisibility(View.VISIBLE);
        mTenContainer.setVisibility(View.GONE);
        mOtherContainer.loadUrl(url);
    }

    private void showTenContent(int strsRes, int imgRes1, int imgRes2) {
        mOtherContainer.setVisibility(View.GONE);
        mTenContainer.setVisibility(View.VISIBLE);
        // TODO show string array
        String[] strs = getResources().getStringArray(strsRes);
        if (strs != null && strs.length >= 4) {
            mTenActionTv.setText(strs[0]);
            mTenAnalysisTv.setText(strs[1]);
            mTenTargetTv.setText(strs[2]);
            mTenCourseTv.setText(strs[3]);

            mTenImg1.setImageResource(imgRes1);
            mTenImg2.setImageResource(imgRes2);

            mTenContainer.scrollTo(0, 0);
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
