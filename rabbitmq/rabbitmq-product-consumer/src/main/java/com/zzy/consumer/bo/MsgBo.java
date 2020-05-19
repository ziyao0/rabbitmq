package com.zzy.consumer.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangziyao
 * @version 1.0
 * @date 2020/5/19 10:02
 */
@Data
public class MsgBo implements Serializable {
    private long orderNo;

    private int productNo;

    private String msgId;
}
