package org.example.c.backend.common;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> Result<T> success(T data){
        return success("操作成功", data);
    }

    public static <T> Result<T> success(String message, T data){
        return success(200, message, data);
    }
    public static <T> Result<T> success(Integer code, String message, T data){
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> fail(){
        return fail("操作失败");
    }
    public static <T> Result<T> fail(String message){
        return fail(500, message);
    }
    public static <T> Result<T> fail(Integer code, String message){
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);
        return result;
    }

}
