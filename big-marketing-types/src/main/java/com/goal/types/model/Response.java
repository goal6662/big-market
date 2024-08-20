package com.goal.types.model;

import com.goal.types.enums.ResponseCode;
import lombok.Data;

@Data
public class Response<T> {

    private String code;
    private String info;
    private T data;

    private Response() {
    }

    public static <T> Response<T> success() {
        ResponseCode code = ResponseCode.SUCCESS;

        Response<T> response = new Response<>();
        response.setCode(code.getCode());
        response.setInfo(code.getInfo());

        return response;
    }


    public static <T> Response<T> success(T data) {
        ResponseCode code = ResponseCode.SUCCESS;

        Response<T> response = new Response<>();
        response.setCode(code.getCode());
        response.setInfo(code.getInfo());
        response.setData(data);

        return response;
    }

    public static <T> Response<T> fail(ResponseCode code) {
        Response<T> response = new Response<>();
        response.setCode(code.getCode());
        response.setInfo(code.getInfo());

        return response;
    }

    public static <T> Response<T> fail() {
        ResponseCode code = ResponseCode.UN_ERROR;
        return fail(code);
    }

}

