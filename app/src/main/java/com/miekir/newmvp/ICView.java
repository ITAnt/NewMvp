package com.miekir.newmvp;

import java.util.List;

/**
 * Copyright (C), 2019-2020, Miekir
 *
 * @author Miekir
 * @date 2020/10/15 10:47
 * Description:
 */
public interface ICView {
    void onDataCome(int code, String msg, List<TestBean1> data, int source);
}
