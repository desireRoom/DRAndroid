package com.room.desire.common;


import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by desire on 14/11/27.
 */
public class DRWeakHandler extends Handler {

    private WeakReference<IHandler> mWeakRef;

    public DRWeakHandler(IHandler hander) {
        mWeakRef = new WeakReference<IHandler>(hander);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (mWeakRef != null) {
            IHandler handler = mWeakRef.get();
            if (handler != null) {
                handler.handleMessage(msg);
            }
        }
    }

    public static interface IHandler {
        public void handleMessage(Message msg);
    }
}
