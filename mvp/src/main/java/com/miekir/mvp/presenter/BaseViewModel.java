package com.miekir.mvp.presenter;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.miekir.mvp.base.BaseMvpResponse;
import com.miekir.mvp.base.BaseMvpSource;
import com.miekir.mvp.base.DataResult;

/**
 * Copyright (C), 2019-2020, Miekir
 *
 * @author Miekir
 * @date 2020/10/7 22:46
 * Description:
 */
public class BaseViewModel extends ViewModel {
    /**
     * ViewModel的生命周期和Activity保持一致；在onStart()、onResume()、onPause()时调用LiveData的setValue才会通知观察者
     * liveData在ViewModel(Presenter)创建，然后提供方法给Activity获取，方便取消观察
     */
    private MutableLiveData<DataResult> liveData = new MutableLiveData<DataResult>();

    public MutableLiveData<DataResult> getLiveData() {
        return liveData;
    }

    public void post(int responseCode, String msg, Object dataBean, int sourceCode) {
        if (liveData == null) {
            return;
        }

        liveData.postValue(new DataResult(responseCode, msg, dataBean, sourceCode));
    }

    public void post(int responseCode, String msg, Object dataBean) {
        if (liveData == null) {
            return;
        }

        liveData.postValue(new DataResult(responseCode, msg, dataBean, BaseMvpSource.COMMON));
    }

    public void post(int responseCode, Object dataBean) {
        if (liveData == null) {
            return;
        }

        liveData.postValue(new DataResult(responseCode, "", dataBean, BaseMvpSource.COMMON));
    }

    public void post(int responseCode, Object dataBean, int sourceCode) {
        if (liveData == null) {
            return;
        }

        liveData.postValue(new DataResult(responseCode, "", dataBean, sourceCode));
    }

    public void postSuccess(Object dataBean) {
        if (liveData == null) {
            return;
        }

        liveData.postValue(new DataResult(BaseMvpResponse.COMMON_SUCCESS, "", dataBean, BaseMvpSource.COMMON));
    }

    public void postSuccess(Object dataBean, int sourceCode) {
        if (liveData == null) {
            return;
        }

        liveData.postValue(new DataResult(BaseMvpResponse.COMMON_SUCCESS, "", dataBean, sourceCode));
    }

    public void postSuccess(String msg, Object dataBean) {
        if (liveData == null) {
            return;
        }

        liveData.postValue(new DataResult(BaseMvpResponse.COMMON_SUCCESS, msg, dataBean, BaseMvpSource.COMMON));
    }

    public void postSuccess(String msg, Object dataBean, int sourceCode) {
        if (liveData == null) {
            return;
        }

        liveData.postValue(new DataResult(BaseMvpResponse.COMMON_SUCCESS, msg, dataBean, sourceCode));
    }

    public void postFail(Object dataBean) {
        if (liveData == null) {
            return;
        }

        liveData.postValue(new DataResult(BaseMvpResponse.COMMON_FAIL, "", dataBean, BaseMvpSource.COMMON));
    }

    public void postFail(Object dataBean, int sourceCode) {
        if (liveData == null) {
            return;
        }

        liveData.postValue(new DataResult(BaseMvpResponse.COMMON_FAIL, "", dataBean, sourceCode));
    }

    public void postFail(String msg, Object dataBean) {
        if (liveData == null) {
            return;
        }

        liveData.postValue(new DataResult(BaseMvpResponse.COMMON_FAIL, msg, dataBean, BaseMvpSource.COMMON));
    }

    public void postFail(String msg, Object dataBean, int sourceCode) {
        if (liveData == null) {
            return;
        }

        liveData.postValue(new DataResult(BaseMvpResponse.COMMON_FAIL, msg, dataBean, sourceCode));
    }

    public void detachView() {
        liveData = null;
    }
}
