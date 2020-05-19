package com.zzy.provider.mapper;

import com.zzy.provider.entity.OrderInfo;
import org.springframework.stereotype.Repository;

/**
 * @author zhangziyao
 * @version 1.0
 * @date 2020/5/18 16:43
 */
@Repository
public interface OrderInfoMapper {
    /**
     * 创建订单
     *
     * @param orderInfo
     */
    void saveOrderInfo(OrderInfo orderInfo);
}
