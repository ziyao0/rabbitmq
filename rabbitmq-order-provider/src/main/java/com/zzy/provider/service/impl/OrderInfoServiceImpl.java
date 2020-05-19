package com.zzy.provider.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zzy.provider.bo.MsgBo;
import com.zzy.provider.compent.MsgSender;
import com.zzy.provider.constants.MqConst;
import com.zzy.provider.entity.MessageContent;
import com.zzy.provider.entity.OrderInfo;
import com.zzy.provider.eum.MsgStatusEnum;
import com.zzy.provider.mapper.MsgContentMapper;
import com.zzy.provider.mapper.OrderInfoMapper;
import com.zzy.provider.service.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

/**
 * @author zhangziyao
 * @version 1.0
 * @desc 订单业务处理
 * @date 2020/5/18 16:42
 */
@Service
@Slf4j
@Transactional
public class OrderInfoServiceImpl implements OrderInfoService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private MsgSender msgSender;

    @Autowired
    private MsgContentMapper msgContentMapper;

    /**
     * 创建订单
     *
     * @param orderInfo
     * @return
     */
    @Override
    public void saveOrderAndMessage(OrderInfo orderInfo) throws JsonProcessingException {


        //构建消息对象
        MessageContent messageContent = builderMessageContent(orderInfo.getOrderNo(), orderInfo.getProductNo());

        //保存数据库
        saveOrderInfo(orderInfo, messageContent);

        //模拟异常
       // int a = 1/0;
        //构建消息发送对象
        MsgBo msgTxtBo = new MsgBo();
        msgTxtBo.setMsgId(messageContent.getMsgId());
        msgTxtBo.setOrderNo(orderInfo.getOrderNo());
        msgTxtBo.setProductNo(orderInfo.getProductNo());

        //发送消息
        msgSender.sendMsg(msgTxtBo);
    }

    /**
     * 保存订单表
     *
     * @param orderInfo
     * @param messageContent
     */
    private void saveOrderInfo(OrderInfo orderInfo, MessageContent messageContent) {
        try {
            orderInfoMapper.saveOrderInfo(orderInfo);

            //插入消息表
            msgContentMapper.saveMsgContent(messageContent);

        } catch (Exception e) {
            log.error("操作数据库失败:{}", e);
            throw new RuntimeException("操作数据库失败");
        }
    }

    /**
     * 组装参数
     *
     * @param orderNo
     * @param productNo
     * @return
     */
    private MessageContent builderMessageContent(long orderNo, Integer productNo) {
        MessageContent messageContent = new MessageContent();
        String msgId = UUID.randomUUID().toString();
        messageContent.setMsgId(msgId);
        messageContent.setCreateTime(new Date());
        messageContent.setUpdateTime(new Date());
        messageContent.setExchange(MqConst.ORDER_TO_PRODUCT_EXCHANGE_NAME);
        messageContent.setRoutingKey(MqConst.ORDER_TO_PRODUCT_QUEUE_NAME);
        messageContent.setMsgStatus(MsgStatusEnum.SENDING.getCode());
        messageContent.setOrderNo(orderNo);
        messageContent.setProductNo(productNo);
        messageContent.setMaxRetry(MqConst.MSG_RETRY_COUNT);
        return messageContent;
    }
}
