package com.miekir.mvp.base;

import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.miekir.mvp.view.IView;
import com.miekir.mvp.widget.LoadingDialog;


public abstract class BaseFragment extends Fragment implements IView {
    public FragmentActivity activity;
    //private Unbinder mBinder;
    private LoadingDialog mLoadingDialog;
    protected View rootView;

    /**
     * 设置布局layout
     *
     * @return 布局文件id
     */
    public abstract @LayoutRes
    int getLayoutResId();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutResId(), container, false);
        activity = getActivity();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //mBinder = ButterKnife.bind(this, view);
        mLoadingDialog = new LoadingDialog(getActivity());
    }


    private boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    @Override
    public void showLoading() {
        if (isMainThread()) {
            mLoadingDialog.show();
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoadingDialog.show();
                }
            });
        }
    }

    @Override
    public void showLoading(final String msg) {
        if (isMainThread()) {
            mLoadingDialog.show(msg);
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoadingDialog.show(msg);
                }
            });
        }
    }

    @Override
    public void dismissLoading() {
        if (mLoadingDialog != null) {
            if (isMainThread()) {
                mLoadingDialog.close();
            } else {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLoadingDialog.close();
                    }
                });
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //mBinder.unbind();
        dismissLoading();
        activity = null;
    }
}
