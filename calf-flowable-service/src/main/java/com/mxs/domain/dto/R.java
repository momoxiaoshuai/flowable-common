package com.mxs.domain.dto;

import com.mxs.enums.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class R<T> implements Serializable {
    private static final long serialVersionUID = 3623221448160277110L;

    private int code;
    private String message;
    private T data;

    public static <T> R<T> success(T data) {
        return success(ResultEnum.SUCCESS.getMessage(), data);
    }

    public static <T> R<T> success(String message, T data) {
        return new R<>(ResultEnum.SUCCESS.getCode(), message, data);
    }

    public static <T> R<T> success() {
        return success(ResultEnum.SUCCESS.getMessage(), null);
    }

    public static <T> R<T> failed() {
        return failed(ResultEnum.FAILED.getMessage());
    }

    public static <T> R<T> failed(String message) {
        return failed(ResultEnum.FAILED.getCode(), message);
    }

    public static <T> R<T> failed(Integer code, String message) {
        return new R<>(code, message, null);
    }

    public static <T> R<T> failed(ResultEnum resultEnum) {
        return new R<>(resultEnum.getCode(), resultEnum.getMessage(), null);
    }
}
