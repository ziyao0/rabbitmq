package com.zzy.consumer.eum;

import lombok.Getter;

/**
 * @author zhangziyao
 * @version 1.0
 * @date 2020/5/19 9:54
 * @desc 消息状态
 */
@Getter
public enum MsgStatusEnum {
    SENDING(0, "发送中"),

    SENDING_SUCCESS(1, "消息发送成功"),

    SENDING_FAIL(2, "消息发送失败"),

    CONSUMER_SUCCESS(3, "消费成功"),

    CONSUMER_FAIL(4, "消费失败");

    private Integer code;

    private String msgStatus;


    MsgStatusEnum(Integer code, String msgStatus) {
        this.code = code;
        this.msgStatus = msgStatus;
    }
}
