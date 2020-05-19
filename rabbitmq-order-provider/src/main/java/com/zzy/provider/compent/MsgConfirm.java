package com.zzy.provider.compent;

import com.zzy.provider.entity.MessageContent;
import com.zzy.provider.eum.MsgStatusEnum;
import com.zzy.provider.mapper.MsgContentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author zhangziyao
 * @version 1.0
 * @date 2020/5/19 9:48
 * @desc 消息确认组件
 */
@Component
@Slf4j
public class MsgConfirm implements RabbitTemplate.ConfirmCallback {

    @Autowired
    private MsgContentMapper msgContentMapper;

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String msgId = correlationData.getId();
        //消息确认
        if (ack) {
            log.info("消息Id:{}对应的消息被broker签收成功", msgId);
            updateMsgStatusWithAck(msgId);
        } else {
            log.warn("消息Id:{}对应的消息被broker签收失败:{}", msgId, cause);
            updateMsgStatusWithNack(msgId, cause);
        }

    }

    /**
     * 发送失败
     *
     * @param msgId
     * @param cause
     */
    private void updateMsgStatusWithNack(String msgId, String cause) {
        MessageContent messageContent = builderUpdateContent(msgId);
        messageContent.setMsgStatus(MsgStatusEnum.SENDING_FAIL.getCode());
        messageContent.setErrCause(cause);
        msgContentMapper.updateMsgStatus(messageContent);
    }

    /**
     * 发送成功
     *
     * @param msgId
     */
    private void updateMsgStatusWithAck(String msgId) {
        MessageContent messageContent = builderUpdateContent(msgId);
        messageContent.setMsgStatus(MsgStatusEnum.SENDING_SUCCESS.getCode());
        msgContentMapper.updateMsgStatus(messageContent);
    }

    /**
     * 组装参数
     *
     * @param msgId
     * @return
     */
    private MessageContent builderUpdateContent(String msgId) {
        MessageContent messageContent = new MessageContent();
        messageContent.setMsgId(msgId);
        messageContent.setUpdateTime(new Date());
        return messageContent;
    }
}
