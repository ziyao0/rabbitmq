package com.zzy.provider.task;

import com.zzy.provider.bo.MsgBo;
import com.zzy.provider.compent.MsgSender;
import com.zzy.provider.constants.MqConst;
import com.zzy.provider.entity.MessageContent;
import com.zzy.provider.eum.MsgStatusEnum;
import com.zzy.provider.mapper.MsgContentMapper;
import com.zzy.provider.thread.ThreadPololConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author zhangziyao
 * @version 1.0
 * @desc 定时任务
 * @date 2020/5/19 9:43
 */
@Component
@Slf4j
public class RetryMsgTask {

    @Autowired
    private MsgContentMapper msgContentMapper;

    @Autowired
    private MsgSender msgSender;

    @Autowired
    private ThreadPololConfig threadPololConfig;

    private final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);

    /**
     * 延迟发送
     */
    @Scheduled(initialDelay = 10000, fixedDelay = 5000)
    public void retrySend() {
        executor.execute(() -> {
            log.info("-------------------定时任务-重发消息-----------------------");
            //查询五分钟消息状态还没有完结的消息
            List<MessageContent> messageContentList = msgContentMapper.qryNeedRetryMsg(MsgStatusEnum.CONSUMER_SUCCESS.getCode(), MqConst.TIME_DIFF);

            for (MessageContent messageContent : messageContentList) {

                if (messageContent.getMaxRetry() > messageContent.getCurrentRetry()) {
                    log.info("消息:{}当前重试次数:{}", messageContent.getMsgId(), messageContent.getCurrentRetry() + 1);
                    MsgBo msgTxtBo = new MsgBo();
                    msgTxtBo.setMsgId(messageContent.getMsgId());
                    msgTxtBo.setProductNo(messageContent.getProductNo());
                    msgTxtBo.setOrderNo(messageContent.getOrderNo());
                    //更新消息重试次数
                    msgContentMapper.updateMsgRetryCount(msgTxtBo.getMsgId());
                    msgSender.sendMsg(msgTxtBo);
                } else {
                    log.info("消息:{}已经达到最大重试次数。", messageContent);
                }

            }
        });
    }

}
