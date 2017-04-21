package com.amayadream.panspider.common;

import java.io.Serializable;

/**
 * 返回值实体
 * @author :  Amayadream
 * @date :  2017.04.12 21:13
 */
public class ResultEntity {

    /** 状态码 */
    private Integer code;
    /** 信息 */
    private String message;
    /** 时间戳 */
    private Long timestamp;
    /** 数据 */
    private Serializable data;

    private ResultEntity(Integer code, String message, Long timestamp, Serializable data) {
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
        this.data = data;
    }

    public static ResultEntity buildOkResult(ResultConstant c) {
        return new ResultEntity(c.getCode(), c.getMessage(), System.currentTimeMillis(), null);
    }

    public static ResultEntity buildOkResult(ResultConstant c, Serializable data) {
        return new ResultEntity(c.getCode(), c.getMessage(), System.currentTimeMillis(), data);
    }

    public static ResultEntity buildNokResult(ResultConstant c) {
        return new ResultEntity(c.getCode(), c.getMessage(), System.currentTimeMillis(), null);
    }

    public static ResultEntity buildNokResult(ResultConstant c, Serializable data) {
        return new ResultEntity(c.getCode(), c.getMessage(), System.currentTimeMillis(), data);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Serializable getData() {
        return data;
    }

    public void setData(Serializable data) {
        this.data = data;
    }
}
