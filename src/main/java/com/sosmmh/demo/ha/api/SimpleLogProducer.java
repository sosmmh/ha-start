package com.sosmmh.demo.ha.api;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Lixh
 */
@Slf4j
public class SimpleLogProducer extends AbstractReliableProducer {

    @Override
    public boolean actualSend(String messageId, String topic, String message) {
        log.info("msgId={}|事务成功提交|simpleLog|topic={}|message={}", messageId, topic, message);
        return true;
    }
}
