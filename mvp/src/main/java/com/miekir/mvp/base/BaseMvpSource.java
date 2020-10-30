package com.miekir.mvp.base;

/**
 * Copyright (C), 2019-2020, Miekir
 *
 * @author Miekir
 * @date 2020/10/30 9:00
 * Description: 用于区分数据来源。如一个界面上有两个列表，左边是展示本地数据库的数据，右边是展示远程数据库的数据，数据类型一样，
 * 此时有必要区分来源。
 */
public interface BaseMvpSource {
    int COMMON = 0;
}
