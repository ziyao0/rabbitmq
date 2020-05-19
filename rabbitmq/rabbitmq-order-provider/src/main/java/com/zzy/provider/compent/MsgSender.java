package com.zzy.provider.compent;

import com.zzy.provider.bo.MsgBo;
import com.zzy.provider.constants.MqConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhangziyao
 * @version 1.0
 * @date 2020/5/19 9:46
 * @desc 消息发送组件
 */
@Component
@Slf4j
public class MsgSender implements InitializingBean {

    @Autowired
    private MsgConfirm msgConfirm;

    @Autowired
    private MsgRetrunListener msgRetrunListener;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 消息发送
     *
     * @param msgBo
     */
    public void sendMsg(MsgBo msgBo) {
        log.info("发送的消息ID:{},消息：{}", msgBo.getMsgId(),msgBo);
        CorrelationData correlationData = new CorrelationData(msgBo.getMsgId());
        //发送消息
        rabbitTemplate.convertAndSend(MqConst.ORDER_TO_PRODUCT_EXCHANGE_NAME, MqConst.ORDER_TO_PRODUCT_ROUTING_KEY, msgBo, correlationData);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        rabbitTemplate.setConfirmCallback(msgConfirm);
        rabbitTemplate.setReturnCallback(msgRetrunListener);
        //设置消息转换器
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);
    }
}
