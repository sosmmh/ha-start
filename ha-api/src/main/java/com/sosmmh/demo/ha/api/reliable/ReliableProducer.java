package com.sosmmh.demo.ha.api.reliable;

import com.sosmmh.demo.ha.api.model.HaMessage;

/**
 * @author lixiahan
 */
public interface ReliableProducer {


    /**
     * 发送消息
     *
     * @param haMessage haMessage
     */
    void send(HaMessage haMessage);

    /**
     * 重新发送
     */
    void resend();

}
