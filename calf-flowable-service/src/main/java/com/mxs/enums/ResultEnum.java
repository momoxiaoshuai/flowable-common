package com.mxs.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

@Getter
public enum ResultEnum implements IEnum<Integer> {
    SUCCESS(200, "成功"),
    FAILED(500, "失败");

    private final Integer code;
    private final String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getValue() {
        return this.code;
    }

    /** 根据枚举值获取对应的枚举实例 */
    public static ResultEnum getEnum(Integer code) {
        if (code == null) {
            return null;
        }
        for (ResultEnum result : ResultEnum.values()) {
            if (result.getCode().equals(code)) {
                return result;
            }
        }
        return null;
    }
}
