package com.skmns.codingtest.util;

public class SkmnsResult<T> {
    private String message;
    private int statusCode;
    private T data;

    public SkmnsResult(String message, int statusCode, T data) {
        this.message = message;
        this.statusCode = statusCode;
        this.data = data;
    }

    public SkmnsResult(String message, int statusCode) {
        this(message, statusCode, null);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
