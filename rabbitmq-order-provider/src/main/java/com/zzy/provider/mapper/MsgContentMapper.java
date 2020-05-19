package com.zzy.provider.mapper;

import com.zzy.provider.entity.MessageContent;
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
     *
     * @param messageContent:消息对象
     * @author:smlz
     * @return:
     * @date:2019/10/11 16:16
     */
    int saveMsgContent(MessageContent messageContent);

    int updateMsgStatus(MessageContent messageContent);

    List<MessageContent> qryNeedRetryMsg(@Param("msgStatus") Integer status, @Param("timeDiff") Integer timeDiff);

    void updateMsgRetryCount(String msgId);
}
