package com.miekir.mvp.view.model;


import android.os.Bundle;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.miekir.mvp.base.BaseActivity;
import com.miekir.mvp.base.BaseMvpResponse;
import com.miekir.mvp.base.DataResult;
import com.miekir.mvp.presenter.BaseViewModel;
import com.miekir.mvp.presenter.DataMethod;
import com.miekir.mvp.presenter.InjectViewModel;
import com.miekir.mvp.view.IView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于MVP思想的Activity
 * @author zhan
 */
public abstract class BaseMvpActivity extends BaseActivity implements IView {
    private List<Method> mDataMethodList = new ArrayList<>();
    private List<BaseViewModel> mInjectPresenters = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVariables();
        initCallbacks();
        initViews(savedInstanceState);
    }

    /**
     * 初始化添加注解的变量
     */
    private void initVariables() {
        clearPresenters();
        // 这里可以获取到子类的成员变量
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 获取变量上面的注解类型
            InjectViewModel injectViewModel = field.getAnnotation(InjectViewModel.class);
            if (injectViewModel == null) {
                continue;
            }

            try {
                field.setAccessible(true);

                // 父类引用指向子类对象
                Class<? extends BaseViewModel> type = (Class<? extends BaseViewModel>) field.getType();
                BaseViewModel presenter = type.newInstance();
                // 下面这种方法是单例形式，会造成重复回调的问题
                //BaseViewModel presenter = new ViewModelProvider(this).get(type);

                // 创建一个观察者去更新UI
                final Observer<DataResult> observer = new Observer<DataResult>() {
                    @Override
                    public void onChanged(final DataResult result) {
                        // 回调是在主线程
                        // Log.i("Thread", String.valueOf(Thread.currentThread() == Looper.getMainLooper().getThread()));
                        onDataResult(result);
                    }
                };
                if (injectViewModel.isPersist()) {
                    presenter.getLiveData().observeForever(observer);
                } else {
                    presenter.getLiveData().observe(this, observer);
                }

                field.set(this, presenter);
                mInjectPresenters.add(presenter);
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            } catch (ClassCastException e) {
                e.printStackTrace();
                throw new RuntimeException(InjectViewModel.class.getName() + "注解修饰的类必须继承自：" + BaseViewModel.class.getName());
            }
        }
    }

    /**
     * 初始化获取需要回调的注解方法
     */
    private void initCallbacks() {
        // 查找回调方法
        mDataMethodList.clear();

        Method[] methods = null;
        try {
            // This is faster than getMethods, especially when subscribers are fat classes like Activities
            methods = getClass().getDeclaredMethods();
        } catch (Throwable th) {
            // Workaround for java.lang.NoClassDefFoundError, see https://github.com/greenrobot/EventBus/issues/149
            try {
                methods = getClass().getMethods();
            } catch (LinkageError error) {
                error.printStackTrace();
            }
        }

        // 参数校验
        if (methods == null || methods.length == 0) {
            return;
        }

        String methodAnnotationName = DataMethod.class.getName();
        for (Method method : methods) {
            DataMethod dataMethod = method.getAnnotation(DataMethod.class);
            if (dataMethod == null) {
                continue;
            }

            int length = method.getParameterTypes().length;
            if (length != 4) {
                throw new AssertionError(methodAnnotationName + "修饰的方法参数数量必须为4个");
            }

            // 响应结果，必须为int或者boolean
            String firstType = method.getParameterTypes()[0].getSimpleName();
            String resultCodeType = int.class.getSimpleName();
            String resultStatusType = boolean.class.getSimpleName();
            if (!TextUtils.equals(resultCodeType, firstType) && !TextUtils.equals(resultStatusType, firstType)) {
                throw new AssertionError(methodAnnotationName + "修饰的方法第1个参数必须为" + resultCodeType + "或" + resultStatusType);
            }

            // 响应消息
            String secondType = method.getParameterTypes()[1].getSimpleName();
            String messageType = String.class.getSimpleName();
            if (!TextUtils.equals(messageType, secondType)) {
                throw new AssertionError(methodAnnotationName + "修饰的方法第2个参数必须为" + messageType);
            }

            // 数据来源
            String fourthType = method.getParameterTypes()[3].getSimpleName();
            String sourceCodeType = int.class.getSimpleName();
            if (!TextUtils.equals(fourthType, sourceCodeType)) {
                throw new AssertionError(methodAnnotationName + "修饰的方法第4个参数必须为" + sourceCodeType);
            }

            method.setAccessible(true);
            mDataMethodList.add(method);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        clearPresenters();
        mDataMethodList.clear();
    }

    private void clearPresenters() {
        if (mInjectPresenters.size() > 0) {
            for (BaseViewModel presenter : mInjectPresenters) {
                if (presenter == null) {
                    continue;
                }

                MutableLiveData<DataResult> liveData = presenter.getLiveData();
                if (liveData == null) {
                    continue;
                }
                liveData.removeObservers(this);
                presenter.detachView();
            }
            mInjectPresenters.clear();
        }
    }

    /**
     * 回调
     * @param result
     */
    private void onDataResult(DataResult result) {
        dismissLoading();
        Class objParamClass = result.getBean().getClass();
        for (Method method : mDataMethodList) {
            Class methodParamClass = method.getParameterTypes()[2];
            // 判断方法的第二个参数类型是否是回调对象的父类
            if (!methodParamClass.isAssignableFrom(objParamClass)) {
                continue;
            }

            try {
                method.invoke(this, result.getResponseCode(), result.getMessage(), result.getBean(), result.getSourceCode());
            } catch (Exception e) {
                //e.printStackTrace();
            }

            try {
                method.invoke(this, result.getResponseCode() == BaseMvpResponse.COMMON_SUCCESS, result.getMessage(), result.getBean(), result.getSourceCode());
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
    }
}
