package com.miekir.common.utils;

import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * Copyright (C), 2019-2020, Miekir
 *
 * @author Miekir
 * @date 2020/9/12 17:43
 * Description:
 */
public class ContextManager {
    private ContextManager(){}

    private static WeakReference<Context> contextWeakReference = new WeakReference<>(null);
    private static ContextManager instance;
    public void initContext(Context context) {
        contextWeakReference = new WeakReference<>(context);
    }

    public static ContextManager getInstance() {
        if (instance == null) {
            init();
        }
        return instance;
    }

    private static synchronized void init() {
        if (instance == null) {
            instance = new ContextManager();
        }
    }

    public Context getContext() {
        return contextWeakReference.get();
    }
}
