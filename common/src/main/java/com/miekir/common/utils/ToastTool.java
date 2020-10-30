package com.miekir.common.utils;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @author Miekir
 * @date 2020/7/5 0:08
 * Description: Toast工具
 */
public class ToastTool {
    private static final long PERIOD_SHORT = 1500L;
    private static final long PERIOD_LONG = 2500L;

    private ToastTool() { }

    private static long mLastShortToastMillis;
    private static long mLastLongToastMillis;

    private static int mVerticalMargin = 0;

    /**
     * @param text 要弹出的语句
     */
    public static void showShort(String text) {
        Context context = ContextManager.getInstance().getContext();
        if (context == null) {
            return;
        }

        initMargin(context);

        if (System.currentTimeMillis() - mLastShortToastMillis > PERIOD_SHORT) {
            mLastShortToastMillis = System.currentTimeMillis();
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, mVerticalMargin);
            toast.show();
        }
    }

    /**
     * @param text 要弹出的语句
     */
    public static void showLong(String text) {
        Context context = ContextManager.getInstance().getContext();
        if (context == null) {
            return;
        }

        initMargin(context);

        if (System.currentTimeMillis() - mLastLongToastMillis > PERIOD_LONG) {
            mLastLongToastMillis = System.currentTimeMillis();
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, mVerticalMargin);
            toast.show();
        }
    }

    private static void initMargin(Context context) {
        if (context == null) {
            return;
        }
        if (mVerticalMargin == 0) {
            mVerticalMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    72,
                    context.getResources().getDisplayMetrics());
        }
    }
}
