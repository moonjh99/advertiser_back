package com.example.advertiser.common;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private T data;
    String message;

    public static <T> ApiResponse<T> ok(T data){
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(false, null, message);
    }

}
