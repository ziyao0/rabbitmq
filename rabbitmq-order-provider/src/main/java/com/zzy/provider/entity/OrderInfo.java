package com.zzy.provider.entity;

import lombok.Data;

import java.util.Date;

/**
 * @desc   订单描述
 * @author zhangziyao
 * @version 1.0
 * @date 2020/5/18 16:41
 */
@Data
public class OrderInfo {

    private long orderNo;

    private Date createTime;

    private Date updateTime;

    private String userName;

    private double money;

    private Integer productNo;
}
