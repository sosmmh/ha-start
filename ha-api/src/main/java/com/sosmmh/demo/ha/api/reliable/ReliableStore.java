package com.sosmmh.demo.ha.api.reliable;

import java.util.List;

public interface ReliableStore {
    /**
     * 发送前
     *
     * @param reliableMessage
     */
    void preSend(ReliableMessage reliableMessage);

    /**
     * 发送后
     *
     * @param messageId
     */
    void postSend(String messageId);

    /**
     * 查询超时未发送的消息
     *
     * @return
     */
    List<ReliableMessage> fetchTimeOutMessage4Retry();
}
