package com.zzy.provider.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author zhangziyao
 * @version 1.0
 * @date 2020/5/19 8:41
 * @desc 消息日志表
 */
@Data
public class MessageContent {
    private String msgId;

    private long orderNo;

    private Date createTime;

    private Date updateTime;

    private Integer msgStatus;

    private String exchange;

    private String routingKey;

    private String errCause;

    private Integer maxRetry;

    private Integer currentRetry = 0;

    private Integer productNo;
}
