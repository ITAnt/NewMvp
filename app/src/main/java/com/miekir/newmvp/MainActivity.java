package com.miekir.newmvp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.miekir.mvp.presenter.DataMethod;
import com.miekir.mvp.presenter.InjectViewModel;
import com.miekir.mvp.view.model.BaseMvpActivity;

import java.util.List;

public class MainActivity extends BaseMvpActivity {
    @InjectViewModel
    TestViewModel1 viewModel1;

    @Override
    public int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        TextView tv_name = findViewById(R.id.tv_name);
        tv_name.setText("MainActivity");

        findViewById(R.id.view_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击之后，后台执行耗时操作
                viewModel1.go();
            }
        });
    }

    @DataMethod
    public void onDataCome(int code, String msg, List<TestBean1> data, int source) {
        Toast.makeText(this, data.get(0).getName(), Toast.LENGTH_SHORT).show();
    }
}
