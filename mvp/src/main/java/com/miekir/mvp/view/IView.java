package com.miekir.mvp.view;

public interface IView {
    void showLoading();

    void showLoading(String msg);

    void dismissLoading();
}
