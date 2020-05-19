package com.zzy.provider.compent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzy.provider.bo.MsgBo;
import com.zzy.provider.entity.MessageContent;
import com.zzy.provider.eum.MsgStatusEnum;
import com.zzy.provider.mapper.MsgContentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author zhangziyao
 * @version 1.0
 * @date 2020/5/19 10:00
 * @desc 消息不可达监听器
 */
@Component
@Slf4j
public class MsgRetrunListener implements RabbitTemplate.ReturnCallback {

    @Autowired
    private MsgContentMapper msgContentMapper;

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            MsgBo msgBo = objectMapper.readValue(message.getBody(), MsgBo.class);
            log.info("无法路由消息内容:{},cause:{}", msgBo, replyText);

            //构建消息对象
            MessageContent messageContent = new MessageContent();
            messageContent.setErrCause(replyText);
            messageContent.setUpdateTime(new Date());
            messageContent.setMsgStatus(MsgStatusEnum.SENDING_FAIL.getCode());
            messageContent.setMsgId(msgBo.getMsgId());
            //更新消息表
            msgContentMapper.updateMsgStatus(messageContent);
        } catch (Exception e) {
            log.error("更新消息表异常:{}", e.getMessage());
        }
    }
}
