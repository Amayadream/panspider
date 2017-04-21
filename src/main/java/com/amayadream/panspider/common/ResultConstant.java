package com.amayadream.panspider.common;

/**
 * 返回状态码与返回信息对应实体
 * @author :  Amayadream
 * @date :  2017.04.12 21:13
 */
public enum ResultConstant {

    SUCCESS(0, "OK")
    ,NOT_LOGIN(-1, "未登录")

    ;

    private Integer code;
    private String message;

    ResultConstant(Integer code, String message) {
        this.code = code;
        this.message = message;
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
}
