package com.miekir.newmvp;

import com.miekir.mvp.presenter.BasePresenter;

/**
 * Copyright (C), 2019-2020, Miekir
 *
 * @author Miekir
 * @date 2020/10/6 20:53
 * Description:
 */
public class TestPresenter1 extends BasePresenter {
    public void go() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    //getView().onLoginResult(3, "ddd");
                    post(0, "test", new TestBean1(3, "Jason"), 3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
