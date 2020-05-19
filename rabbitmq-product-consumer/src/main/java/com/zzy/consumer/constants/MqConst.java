package com.zzy.consumer.constants;

/**
 * @author zhangziyao
 * @version 1.0
 * @date 2020/5/19 9:46
 */
public class MqConst {
    /**
     * 交换机名称
     */
    public static final String ORDER_TO_PRODUCT_EXCHANGE_NAME = "order-to-product.exchange";

    /**
     * 队列名称
     */
    public static final String ORDER_TO_PRODUCT_QUEUE_NAME = "order-to-product.queue";

    /**
     * 路由key
     */
    public static final String ORDER_TO_PRODUCT_ROUTING_KEY = "order-to-product.key";

    /**
     * redisKey
     */
    public static final String STORE_LOCK_KEY = "store-lock-key";

    /**
     * 消息重发的最大次数
     */
    public static final Integer MSG_RETRY_COUNT = 5;

    public static final Integer TIME_DIFF = 30;
}
