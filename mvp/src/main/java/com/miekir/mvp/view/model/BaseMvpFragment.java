package com.miekir.mvp.view.model;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.miekir.mvp.base.BaseFragment;
import com.miekir.mvp.base.DataResult;
import com.miekir.mvp.presenter.BaseViewModel;
import com.miekir.mvp.presenter.DataMethod;
import com.miekir.mvp.presenter.InjectViewModel;
import com.miekir.mvp.view.IView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

// 第三个参数的类型
//String objTypeName = method.getGenericParameterTypes()[2].toString();
//if (objectParamsList.contains(objTypeName)) {
//    throw new AssertionError("第3个参数为" + objTypeName + "的方法有多个，应该合并为一个方法");
//}
//objectParamsList.add(objTypeName);
public abstract class BaseMvpFragment extends BaseFragment implements IView {
    /**
     * 是否被创建了
     */
    protected boolean isViewCreated;
    /**
     * 当前是否可见
     */
    protected boolean isUIVisible;

    private List<Method> mDataMethodList = new ArrayList<>();
    private List<BaseViewModel> mInjectPresenters = new ArrayList<>();

    @Override
    public  void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initVariables();
        initCallbacks();

        isViewCreated = true;
        onViewInit();
        loadData();
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
            } catch (IllegalAccessException | java.lang.InstantiationException e) {
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

            // 响应结果code
            String firstType = method.getParameterTypes()[0].getSimpleName();
            String resultCodeType = int.class.getSimpleName();
            if (!TextUtils.equals(resultCodeType, firstType)) {
                throw new AssertionError(methodAnnotationName + "修饰的方法第1个参数必须为" + resultCodeType);
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

    /**
     * View初始化
     */
    protected abstract void onViewInit();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isUIVisible = isVisibleToUser;
        if (isVisibleToUser) {
            loadData();
        }
    }

    /**
     * 懒加载，当Fragment可见的时候，再去加载数据
     * 应用初始化会先调用完所有的setUserVisibleHint再调用onViewCreated，然后切换的时候，就只调用setUserVisibleHint了
     */
    private void loadData() {
        if (isViewCreated && isUIVisible) {
            isViewCreated = false;
            isUIVisible = false;
            onLazyLoad();
        }
    }

    /**
     * 懒加载，初始化加载数据
     */
    protected abstract void onLazyLoad();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mInjectPresenters != null && mInjectPresenters.size() > 0) {
            for (BaseViewModel presenter : mInjectPresenters) {
                presenter.getLiveData().removeObservers(this);
                presenter.detachView();
            }
            mInjectPresenters.clear();
            mInjectPresenters = null;
        }

        clearPresenters();
        mDataMethodList.clear();
        //objectParamsList.clear();
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

    private void onDataResult(DataResult result) {
        dismissLoading();
        Class objParamClass = result.getBean().getClass();
        for (Method method : mDataMethodList) {
            Class methodParamClass = method.getParameterTypes()[2];
            if (methodParamClass.isAssignableFrom(objParamClass)) {
                try {
                    method.invoke(this, result.getResponseCode(), result.getMessage(), result.getBean(), result.getSourceCode());
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }
        }
    }
}
