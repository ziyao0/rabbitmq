package com.zzy.consumer.compent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.zzy.consumer.bo.MsgBo;
import com.zzy.consumer.constants.MqConst;
import com.zzy.consumer.entity.MessageContent;
import com.zzy.consumer.eum.MsgStatusEnum;
import com.zzy.consumer.exception.MqException;
import com.zzy.consumer.mapper.MsgContentMapper;
import com.zzy.consumer.service.ProductInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

/**
 * @author zhangziyao
 * @version 1.0
 * @date 2020/5/19 14:14
 * @desc
 */
@Slf4j
@Component
public class MqConsumer {

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private MsgContentMapper msgContentMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 没有加分布式锁的版本,可能存在重复消费问题
     *
     * @param message
     * @param channel
     * @throws IOException
     */
//    @RabbitListener(queues = {MqConst.ORDER_TO_PRODUCT_QUEUE_NAME})
//    @RabbitHandler
//    public void consumerMsg(Message message, Channel channel) throws IOException {
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        MsgBo msgBo = objectMapper.readValue(message.getBody(), MsgBo.class);
//
//        log.info("消费消息:{}", msgBo);
//        Long deliveryTag = (Long) message.getMessageProperties().getDeliveryTag();
//
//        try {
//            //更新消息表也业务表
//            productInfoService.updateProductStore(msgBo);
//
//            System.out.println(1 / 0);
//            //消息签收
//            channel.basicAck(deliveryTag, false);
//        } catch (Exception e) {
//            log.error("模拟网络抖动，消息信息：{}", msgBo);
//            //更新msg表为消费失败
//            //更新消息表状态
//            MessageContent messageContent = new MessageContent();
//            messageContent.setMsgId(msgBo.getMsgId());
//            messageContent.setUpdateTime(new Date());
//            messageContent.setMsgStatus(MsgStatusEnum.CONSUMER_FAIL.getCode());
//            msgContentMapper.updateMsgStatus(messageContent);
//
//            channel.basicReject(deliveryTag, false);
//        }
//    }

    /**
     * 加分布式锁
     *
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitHandler
    @RabbitListener(queues = {MqConst.ORDER_TO_PRODUCT_QUEUE_NAME})
    public void consumerMsgWithLock(Message message, Channel channel) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        MsgBo msgBo = objectMapper.readValue(message.getBody(), MsgBo.class);
        Long deliveryTag = message.getMessageProperties().getDeliveryTag();
        if (stringRedisTemplate.opsForValue().setIfAbsent(MqConst.STORE_LOCK_KEY + msgBo.getMsgId(), msgBo.getMsgId())) {
            log.info("消费消息:{}", msgBo);
            try {
                //更新业务表的数据
                productInfoService.updateProductStore(msgBo);
                System.out.println(1 / 0);
                //消息签收
                channel.basicAck(deliveryTag, false);
            } catch (Exception e) {
                /**
                 * 更新数据库异常说明业务没有操作成功需要删除分布式锁
                 */
                log.error("模拟网络抖动，消息信息：{}", msgBo);
                if (e instanceof MqException) {
                    MqException mqException = (MqException) e;
                    log.error("数据业务异常:{},即将删除分布式锁", mqException.getErrMsg());
                    stringRedisTemplate.delete(MqConst.STORE_LOCK_KEY + msgBo.getMsgId());
                }
                //更新消息表状态
                MessageContent messageContent = new MessageContent();
                messageContent.setMsgStatus(MsgStatusEnum.CONSUMER_FAIL.getCode());
                messageContent.setUpdateTime(new Date());
                messageContent.setErrCause(e.getMessage());
                messageContent.setMsgId(msgBo.getMsgId());
                msgContentMapper.updateMsgStatus(messageContent);
                channel.basicReject(deliveryTag, false);
            }
        } else {
            log.warn("请不要重复消费消息{}", msgBo);
            channel.basicReject(deliveryTag, false);
        }
    }
}




























