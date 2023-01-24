package com.study.net.rpc.remote;

/**
 * @author Steven
 * @date 2023年01月23日 23:17
 */
public class Response<T> {

    private Long requestId;

    private int code;

    private String msg;

    private T data;

    public Response(String msg, T data) {
        this.msg = msg;
        this.data = data;
    }

    public Response(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Response ok() {
        return new Response(200, "successfully", "");
    }

    public static Response ok(String msg, Object data) {
        return new Response(200, msg, data);
    }


    public static Response error(int code, String msg) {
        return new Response(code, msg, "");
    }

    public static Response error(String msg, Object data) {
        return new Response(-1, msg, data);
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
