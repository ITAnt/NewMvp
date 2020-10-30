package com.miekir.newmvp;

import com.miekir.mvp.presenter.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C), 2019-2020, Miekir
 *
 * @author Miekir
 * @date 2020/10/6 20:53
 * Description:注意ViewModel的类必须是public的，否则无法创建实例
 */
public class TestViewModel1 extends BaseViewModel {
    public void go() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    List<TestBean1> testBean1List = new ArrayList<>();
                    testBean1List.add(new TestBean1(3, "ViewModel Jason"));
                    postSuccess(testBean1List);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void detachView() {
        super.detachView();
        // todo 这里可以做一些回收的动作
    }
}
