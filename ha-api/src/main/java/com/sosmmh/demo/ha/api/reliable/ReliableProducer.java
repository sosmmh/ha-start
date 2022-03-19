package com.sosmmh.demo.ha.api.reliable;

public interface ReliableProducer {

    /**
     * 发送消息
     * @param topic
     * @param message
     */
    void send(String topic, String message);

    /**
     * 重新发送
     */
    void resend();

}
