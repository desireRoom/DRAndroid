package com.room.desire.common;


import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by desire on 14/11/27.
 */
public class DRWeakHandler extends Handler {

    private WeakReference<DRIHander> mWeakRef;

    public DRWeakHandler(DRIHander hander) {
        mWeakRef = new WeakReference<DRIHander>(hander);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (mWeakRef != null) {
            DRIHander handler = mWeakRef.get();
            if (handler != null) {
                handler.handleMessage(msg);
            }
        }
    }

    public static interface DRIHander {
        public void handleMessage(Message msg);
    }
}
