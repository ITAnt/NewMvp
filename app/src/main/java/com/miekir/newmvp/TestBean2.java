package com.miekir.newmvp;

/**
 * Copyright (C), 2019-2020, Miekir
 *
 * @author Miekir
 * @date 2020/10/7 11:35
 * Description:
 */
public class TestBean2 {
    private String name;
    private String address;

    public TestBean2(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
