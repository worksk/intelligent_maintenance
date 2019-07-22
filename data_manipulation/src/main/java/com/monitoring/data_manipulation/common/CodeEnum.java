package com.monitoring.data_manipulation.common;

/**
 * @Auther: wyx
 * @Date: 2019-07-22 15:07
 * @Description: 返回码枚举类
 */
public enum CodeEnum {

    REQUEST_SUCCESS(1001, "请求成功"),
    UN_KNOW_ERROR(2001, "未知异常");

    private Integer code;

    private String msg;

    CodeEnum(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode(){
        return code;
    }

    public String getMsg(){
        return msg;
    }

}
