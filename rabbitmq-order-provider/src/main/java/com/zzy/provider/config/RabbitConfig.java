package com.zzy.provider.config;

import com.zzy.provider.constants.MqConst;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangziyao
 * @version 1.0
 * @date 2020/5/18 9:31
 * @desc mq配置类
 */
@Configuration
public class RabbitConfig {

    /**
     * 设置交换机
     *
     * @return
     */
    @Bean
    public DirectExchange getDirectExchange() {
        return new DirectExchange(MqConst.ORDER_TO_PRODUCT_EXCHANGE_NAME, true, false);
    }

    /**
     * 设置队列
     *
     * @return
     */
    @Bean
    public Queue getQueue() {
        return new Queue(MqConst.ORDER_TO_PRODUCT_QUEUE_NAME, true, false, false);
    }

    /**
     * 绑定交换机和对列
     *
     * @return
     */
    @Bean
    public Binding bindingQueue() {
        return BindingBuilder.bind(getQueue()).to(getDirectExchange()).with(MqConst.ORDER_TO_PRODUCT_ROUTING_KEY);
    }
}
