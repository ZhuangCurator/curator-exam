package com.curator.common.support;

import lombok.ToString;

import java.io.Serializable;

/**
 * 返回信息封装类
 *
 * @author Jun
 * @date 2020/9/15
 */
@SuppressWarnings("ALL")
@ToString
public class ResultResponse<T> implements Serializable {

    private static final String SUCCESS_CODE = "2000";
    private static final String FAILURE_CODE = "5000";

    /**
     * 是否成功
     */
    private Boolean succeeded;

    /**
     * 返回的状态码
     */
    private String status;

    /**
     * 返回的请求信息的描述
     */
    private String message;

    /**
     * 返回的具体数据
     */
    private T data;

    ResultResponse() {}

    ResultResponse(String status, Boolean succeeded, String message, T data) {
        this.status = status;
        this.succeeded = succeeded;
        this.message = message;
        this.data = data;
    }

    public static <T> ResultResponse.ResultResponseBuilder<T> builder() {
        return new ResultResponse.ResultResponseBuilder();
    }

    public String getStatus() {
        return this.status;
    }

    public Boolean getSucceeded() {
        return succeeded;
    }

    public String getMessage() {
        return this.message;
    }

    public T getData() {
        return this.data;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSucceeded(Boolean succeeded) {
        this.succeeded = succeeded;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static class ResultResponseBuilder<T> {

        private Boolean succeeded;
        private String status;
        private String message;
        private T data;

        ResultResponseBuilder() {
        }

        public ResultResponse.ResultResponseBuilder<T> success() {
            this.status = SUCCESS_CODE;
            this.succeeded = Boolean.TRUE;
            return this;
        }

        public ResultResponse.ResultResponseBuilder<T> success(String message) {
            this.status = SUCCESS_CODE;
            this.succeeded = Boolean.TRUE;
            this.message = message;
            return this;
        }

        public ResultResponse.ResultResponseBuilder<T> failure() {
            this.status = FAILURE_CODE;
            this.succeeded = Boolean.FALSE;
            return this;
        }

        public ResultResponse.ResultResponseBuilder<T> failure(String message) {
            this.status = FAILURE_CODE;
            this.succeeded = Boolean.FALSE;
            this.message = message;
            return this;
        }

        public ResultResponse.ResultResponseBuilder<T> status(String status) {
            this.status = status;
            return this;
        }

        public ResultResponse.ResultResponseBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        public ResultResponse.ResultResponseBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ResultResponse<T> build() {
            return new ResultResponse(this.status, this.succeeded, this.message, this.data);
        }
    }

}
