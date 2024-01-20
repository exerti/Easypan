package com.CloudPan.controller.utils;


import lombok.Data;

@Data
public class ResultData<T> {
    private int status;
    private String message;
    private T data;
    private long timestamp ;


    public ResultData (){
        this.timestamp = System.currentTimeMillis();
    }


    public static <T> ResultData<T> success(T data) {
        ResultData<T> resultData = new ResultData<>();
        resultData.setStatus(ReturnCodeEnum.RC100.getCode());
        resultData.setMessage(ReturnCodeEnum.RC100.getMessage());
        resultData.setData(data);
        return resultData;
    }

    public static <T> ResultData<T> fail(int code, String message) {
        ResultData<T> resultData = new ResultData<T>();
        resultData.setStatus(code);
        resultData.setMessage(message);
        return resultData;
    }
    public  static  <T> ResultData<T> fail(ReturnCodeEnum  returnCodeEnum){
        ResultData<T> resultData = new ResultData<T>();
        resultData.setStatus(resultData.getStatus());
        resultData.setMessage(resultData.getMessage());
        return  resultData;
    }


}