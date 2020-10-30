package com.miekir.mvp.presenter;

import androidx.lifecycle.MutableLiveData;

import com.miekir.mvp.base.DataResult;

import java.lang.ref.WeakReference;

public abstract class BasePresenter {

    private WeakReference<MutableLiveData<DataResult>> mLiveData;

    public void setLiveData(MutableLiveData<DataResult> liveData) {
        mLiveData = new WeakReference<>(liveData);
    }

    public void post(int responseCode, String msg, Object dataBean, int sourceCode) {
        if (mLiveData == null) {
            return;
        }

        MutableLiveData<DataResult> liveData = mLiveData.get();
        if (liveData == null) {
            return;
        }

        liveData.postValue(new DataResult(responseCode, msg, dataBean, sourceCode));
    }

    public void detachView() {
        if (mLiveData != null) {
            mLiveData.clear();
            mLiveData = null;
        }
    }
}
