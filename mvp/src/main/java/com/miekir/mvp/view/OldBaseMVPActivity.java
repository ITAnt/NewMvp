package com.miekir.mvp.view;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.miekir.mvp.base.BaseActivity;
import com.miekir.mvp.base.DataResult;
import com.miekir.mvp.presenter.BasePresenter;
import com.miekir.mvp.presenter.DataMethod;
import com.miekir.mvp.presenter.InjectPresenter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 利用反射，BaseMVPActivity将会查找子类中有{@link InjectPresenter}注解的成员变量，
 * 筛选出父类是{@link BasePresenter}的成员变量，对它们进行实例化
 * 然后BaseMVPActivity调用{@link BasePresenter}的一个方法把自身的{@link androidx.lifecycle.LiveData}类型引用
 * 传递给{@link BasePresenter}，这样{@link BasePresenter}的子类就可以调用相应Activity的LiveData；
 */
public abstract class OldBaseMVPActivity extends BaseActivity implements IView {
    private List<Method> mDataMethodList = new ArrayList<>();
    private List<BasePresenter> mInjectPresenters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 查找回调方法
        if (mDataMethodList.size() == 0) {
            Method[] methods = null;
            try {
                // This is faster than getMethods, especially when subscribers are fat classes like Activities
                methods = getClass().getDeclaredMethods();
            } catch (Throwable th) {
                // Workaround for java.lang.NoClassDefFoundError, see https://github.com/greenrobot/EventBus/issues/149
                try {
                    methods = getClass().getDeclaredMethods();
                } catch (LinkageError error) { // super class of NoClassDefFoundError to be a bit more broad...
                    error.printStackTrace();
                }
            }

            // 参数校验
            if (methods != null && methods.length > 0) {
                for (Method method : methods) {
                    DataMethod dataMethod = method.getAnnotation(DataMethod.class);
                    if (dataMethod != null) {
                        int length = method.getParameterTypes().length;
                        if (length != 4) {
                            throw new AssertionError("DataMethod参数数量必须为4个");
                        }

                        // 响应结果code
                        String firstType = method.getParameterTypes()[0].getSimpleName();
                        String resultCodeType = int.class.getSimpleName();
                        if (!TextUtils.equals(resultCodeType, firstType)) {
                            throw new AssertionError("DataMethod第1个参数必须为" + resultCodeType);
                        }

                        // 响应消息
                        String secondType = method.getParameterTypes()[1].getSimpleName();
                        String messageType = String.class.getSimpleName();
                        if (!TextUtils.equals(messageType, secondType)) {
                            throw new AssertionError("DataMethod第2个参数必须为" + messageType);
                        }

                        // 数据来源
                        String fourthType = method.getParameterTypes()[3].getSimpleName();
                        String sourceCodeType = int.class.getSimpleName();
                        if (!TextUtils.equals(fourthType, sourceCodeType)) {
                            throw new AssertionError("DataMethod第4个参数必须为" + sourceCodeType);
                        }

                        method.setAccessible(true);
                        mDataMethodList.add(method);
                    }
                }
            }
        }

        // 这里可以获取到子类的成员变量
        mInjectPresenters = new ArrayList<>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            //获取变量上面的注解类型
            InjectPresenter injectPresenter = field.getAnnotation(InjectPresenter.class);
            if (injectPresenter != null) {
                try {
                    //创建一个观察者去更新UI
                    final Observer<DataResult> observer = new Observer<DataResult>() {
                        @Override
                        public void onChanged(final DataResult result) {
                            onDataResult(result);
                        }
                    };
                    // liveData也可以在Presenter创建，然后提供方法给Activity获取
                    MutableLiveData<DataResult> liveData = new MutableLiveData<DataResult>();
                    liveData.observe(this, observer);

                    Class<? extends BasePresenter> type = (Class<? extends BasePresenter>) field.getType();
                    BasePresenter mInjectPresenter = type.newInstance();
                    //mInjectPresenter.attachView(this);
                    mInjectPresenter.setLiveData(liveData);
                    field.setAccessible(true);
                    field.set(this, mInjectPresenter);
                    mInjectPresenters.add(mInjectPresenter);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    throw new RuntimeException("SubClass must extends Class:BasePresenter");
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mInjectPresenters != null && mInjectPresenters.size() > 0) {
            for (BasePresenter presenter : mInjectPresenters) {
                presenter.detachView();
            }
            mInjectPresenters.clear();
            mInjectPresenters = null;
        }

        mDataMethodList.clear();
    }


    private void onDataResult(DataResult result) {
        dismissLoading();
        for (Method method : mDataMethodList) {
            try {
                method.invoke(this, result.getResponseCode(), result.getMessage(), result.getBean(), result.getSourceCode());
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
    }
}
