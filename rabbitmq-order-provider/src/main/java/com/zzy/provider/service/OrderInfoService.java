package com.zzy.provider.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zzy.provider.entity.OrderInfo;

/**
 * @author zhangziyao
 * @version 1.0
 * @date 2020/5/18 16:41
 */
public interface OrderInfoService {
    /**
     * 创建订单
     *
     * @param orderInfo
     * @return
     */
    void saveOrderAndMessage(OrderInfo orderInfo)throws JsonProcessingException;
}
