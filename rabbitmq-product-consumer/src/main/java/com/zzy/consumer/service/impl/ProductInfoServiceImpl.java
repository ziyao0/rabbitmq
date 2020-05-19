package com.zzy.consumer.service.impl;

import com.zzy.consumer.bo.MsgBo;
import com.zzy.consumer.compent.MqConsumer;
import com.zzy.consumer.entity.MessageContent;
import com.zzy.consumer.eum.MsgStatusEnum;
import com.zzy.consumer.exception.MqException;
import com.zzy.consumer.mapper.MsgContentMapper;
import com.zzy.consumer.mapper.ProductInfoMapper;
import com.zzy.consumer.service.ProductInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.PropertyMatches;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author zhangziyao
 * @version 1.0
 * @date 2020/5/19 14:27
 * @desc
 */
@Slf4j
@Service
@Transactional
public class ProductInfoServiceImpl implements ProductInfoService {

    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Autowired
    private MsgContentMapper msgContentMapper;

    @Transactional
    @Override
    public void updateProductStore(MsgBo msgBo) {
//        boolean updateFlag = true;

        try {
            //更新库存
            productInfoMapper.updateProductStoreById(msgBo.getProductNo());

            //更新消息表
            MessageContent messageContent = new MessageContent();
            messageContent.setMsgId(msgBo.getMsgId());
            messageContent.setUpdateTime(new Date());
            messageContent.setMsgStatus(MsgStatusEnum.CONSUMER_SUCCESS.getCode());
            msgContentMapper.updateMsgStatus(messageContent);
            log.info("消息：{}消费成功!", msgBo.getMsgId());
        } catch (Exception e) {
            log.error("更新数据库失败:{}", e);
            throw new MqException(0, "更新数据库异常");
        }
//        return updateFlag;
    }
}
