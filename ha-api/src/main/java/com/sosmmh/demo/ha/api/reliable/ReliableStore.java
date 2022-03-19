package com.sosmmh.demo.ha.api.reliable;

import java.util.List;

/**
 * @author xiahan.li
 */
public interface ReliableStore {
    /**
     * 发送前
     *
     * @param reliableMessage reliableMessage
     */
    void preSend(ReliableMessage reliableMessage);

    /**
     * 发送后
     *
     * @param reliableMessage reliableMessage
     */
    void postSend(ReliableMessage reliableMessage);

    /**
     * 查询超时未发送的消息
     *
     * @param maxRetryCount 最大重试次数
     * @return ReliableMessage
     */
    List<ReliableMessage> fetchTimeOutMessage4Retry(Integer maxRetryCount);
}
