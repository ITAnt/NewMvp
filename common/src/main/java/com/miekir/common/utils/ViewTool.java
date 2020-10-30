package com.miekir.common.utils;

import android.app.Activity;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

/**
 *
 *
 * @author Miekir
 * @date 2020/7/5 8:21
 * Description: View相关的工具
 */
public class ViewTool {
    private ViewTool() {}

    /**
     * 批量设置监听
     * @param activity 控件所在Activity
     * @param viewIdArray 需要监听的控件ID列表
     * @param listener 监听回调
     */
    public static void setOnClickListener(Activity activity, View.OnClickListener listener, int[] viewIdArray) {
        if (activity == null || viewIdArray == null || listener == null || viewIdArray.length == 0) {
            return;
        }

        for (int viewId : viewIdArray) {
            activity.findViewById(viewId).setOnClickListener(listener);
        }
    }

    /**
     * 获取焦点
     * @param activity
     * @param view
     */
    public static void requestInputFocus(Activity activity, final View view) {
        // 进入页面弹出软键盘
        view.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        // 或使用以下方法（view.post不行）
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN , 0, 0, 0));
                view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP , 0, 0, 0));
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }
}
