package com.miekir.newmvp;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.miekir.mvp.presenter.DataMethod;
import com.miekir.mvp.presenter.InjectViewModel;
import com.miekir.mvp.view.model.BaseMvpActivity;

import java.util.List;

public class CActivity extends BaseMvpActivity implements ICView {
    @InjectViewModel
    TestViewModel1 viewModel1;

    @Override
    public int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv_name = findViewById(R.id.tv_name);
        tv_name.setText("CActivity");

        findViewById(R.id.view_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel1.go();
            }
        });
    }


    @DataMethod
    @Override
    public void onDataCome(int resultCode, String msg, List<TestBean1> data, int source) {
        Log.e("tttttbbbb", data.get(0).getName());
    }
}
