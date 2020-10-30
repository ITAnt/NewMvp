package com.miekir.mvp.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.miekir.mvp.view.OldBaseMVPActivity;
import com.miekir.mvp.widget.LoadingDialog;

/**
 * 适配器模式，这个类会适配子类{@link OldBaseMVPActivity}的功能，帮子类实现具体的弹出加载框、弹出提示等基本操作
 */
public abstract class BaseActivity extends AppCompatActivity {
    private LoadingDialog mLoadingDialog;
    private View rootView;
    //private Unbinder mBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 状态栏深色模式，改变状态栏文字颜色
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }

        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(getLayoutID(), null);
        setContentView(rootView);

        View.OnTouchListener rootTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 点击空白处隐藏输入法（要配合Activity根布局增加以下标志使用
                // android:focusable="true"
                // android:focusableInTouchMode="true"）
                hideInputMethod();
                return false;
            }
        };
        rootView.setOnTouchListener(rootTouchListener);

        //进入页面隐藏输入框
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //mBinder = ButterKnife.bind(this);
        mLoadingDialog = new LoadingDialog(this);
    }

    protected void hideInputMethod() {
        if (rootView != null) {
            rootView.requestFocus();
        }

        final InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        final View currentFocusView = getCurrentFocus();
        if (currentFocusView != null) {
            imm.hideSoftInputFromWindow(currentFocusView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onPause() {
        // 必须要在onPause隐藏键盘，在onDestroy就太晚了
        hideInputMethod();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mBinder.unbind();
        dismissLoading();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public abstract int getLayoutID();

    public abstract void initViews(Bundle savedInstanceState);

    private boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public void showLoading() {
        if (isMainThread()) {
            mLoadingDialog.show();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoadingDialog.show();
                }
            });
        }
    }

    public void showLoading(final String msg) {
        if (isMainThread()) {
            mLoadingDialog.show(msg);
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoadingDialog.show(msg);
                }
            });
        }
    }

    public void dismissLoading() {
        if (mLoadingDialog != null) {
            if (isMainThread()) {
                mLoadingDialog.close();
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLoadingDialog.close();
                    }
                });
            }
        }
    }
}
