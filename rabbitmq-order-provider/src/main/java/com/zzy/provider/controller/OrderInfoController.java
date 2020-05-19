package com.zzy.provider.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zzy.provider.entity.OrderInfo;
import com.zzy.provider.service.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author zhangziyao
 * @version 1.0
 * @date 2020/5/18 16:42
 */
@RestController
@Slf4j
@RequestMapping("order")
public class OrderInfoController {

    @Autowired
    private OrderInfoService orderService;

    /**
     * 创建订单
     *
     * @return
     */
    @PostMapping("saveOrder")
    public String saveOrder() {
        OrderInfo orderInfo = new OrderInfo();
//        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        try {
            orderInfo.setCreateTime(new Date());
            orderInfo.setOrderNo(System.currentTimeMillis());
            orderInfo.setMoney(10000.00);
            orderInfo.setUserName("zzy");
            orderInfo.setProductNo(1);
            orderInfo.setUpdateTime(new Date());
            orderService.saveOrderAndMessage(orderInfo);
        } catch (Exception e) {
            if (e instanceof JsonProcessingException) {
                log.error("====================保存订单失败====================");
                return "no";
            }
            log.error("系统异常============：", e);
            return "no";
        }
        return "ok";
    }
}
