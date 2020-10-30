package com.miekir.mvp.base;

/**
 * Copyright (C), 2019-2020, Miekir
 *
 * @author Miekir
 * @date 2020/10/6 21:30
 * Description:
 */
public class DataResult {
    private int responseCode;
    private String message;
    private Object bean;
    private int sourceCode;

    public DataResult(int responseCode, String message, Object bean, int sourceCode) {
        this.responseCode = responseCode;
        this.message = message;
        this.bean = bean;
        this.sourceCode = sourceCode;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public int getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(int sourceCode) {
        this.sourceCode = sourceCode;
    }
}
