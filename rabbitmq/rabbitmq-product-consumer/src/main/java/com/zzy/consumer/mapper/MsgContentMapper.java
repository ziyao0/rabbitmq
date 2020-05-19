package com.zzy.consumer.mapper;

import com.zzy.consumer.entity.MessageContent;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhangziyao
 * @version 1.0
 * @date 2020/5/19 9:35
 */
@Repository
public interface MsgContentMapper {
    /**
     * 方法实现说明:保存消息
     */
    int saveMsgContent(MessageContent messageContent);

    int updateMsgStatus(MessageContent messageContent);

    List<MessageContent> qryNeedRetryMsg(@Param("msgStatus") Integer status, @Param("timeDiff") Integer timeDiff);

    void updateMsgRetryCount(String msgId);
}
