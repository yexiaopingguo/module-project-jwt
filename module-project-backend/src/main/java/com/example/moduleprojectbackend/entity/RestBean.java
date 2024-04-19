package com.example.moduleprojectbackend.entity;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;

public record RestBean<T> (int code, T data, String message) {
    public static <T> RestBean<T> success(T data){
        return new RestBean<>(200, data, "request successful");
    }

    public static <T> RestBean<T> success(){
        return success(null);
    }

    public static <T> RestBean<T> failure(int code, String message){
        return new RestBean<>(code, null, message);
    }

    /**
     * 快速将当前实体转换为JSON字符串格式
     * @return JSON字符串
     */
    public String asJsonString() {
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }

}
