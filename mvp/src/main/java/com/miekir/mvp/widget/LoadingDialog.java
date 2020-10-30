package com.miekir.mvp.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miekir.mvp.R;


public class LoadingDialog {
    LVCircularRing mLoadingView;
    private TextView loadingText;
    Dialog mLoadingDialog;
    Context mContext;
    private String msg = "加载中...";

    public LoadingDialog(Context context) {
        mContext = context;
        // 首先得到整个View
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_loading_view, null);
        // 获取整个布局
        LinearLayout dialogLayout = (LinearLayout) view.findViewById(R.id.dialog_view);
        // 页面中的LoadingView
        mLoadingView = (LVCircularRing) view.findViewById(R.id.lv_circularring);
        // 页面中显示文本
        loadingText = (TextView) view.findViewById(R.id.loading_text);
        // 显示文本
        loadingText.setText(msg);
        // 创建自定义样式的Dialog
        mLoadingDialog = new Dialog(context, R.style.loading_dialog);
        // 设置返回键无效
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.setContentView(dialogLayout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
    }


    public void show() {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
            mLoadingView.startAnim();
        }
    }

    public void show(String message) {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
            loadingText.setText(message);
            mLoadingDialog.show();
            mLoadingView.startAnim();
        }
    }

    public void close() {
        if (mLoadingDialog != null) {
            mLoadingView.stopAnim();
            mLoadingDialog.dismiss();
        }
    }
}
