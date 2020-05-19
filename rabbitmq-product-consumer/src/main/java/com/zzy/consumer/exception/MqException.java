package com.zzy.consumer.exception;

import lombok.Data;

/**
 * @author zhangziyao
 * @version 1.0
 * @date 2020/5/19 14:18
 * @desc
 */
@Data
public class MqException extends RuntimeException {

    private Integer code;

    private String errMsg;

    public MqException(Integer code, String errMsg) {
        super();
        this.code = code;
        this.errMsg = errMsg;
    }
}
