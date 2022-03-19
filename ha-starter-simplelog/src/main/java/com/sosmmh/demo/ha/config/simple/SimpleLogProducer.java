package com.sosmmh.demo.ha.config.simple;

import com.sosmmh.demo.ha.api.reliable.AbstractReliableProducer;
import com.sosmmh.demo.ha.api.reliable.ReliableMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Lixh
 */
@Slf4j
public class SimpleLogProducer extends AbstractReliableProducer {

    @Override
    public boolean actualSend(ReliableMessage reliableMessage) {
        log.info("messageId={}|事务成功提交|simpleLog|reliableMessage={}", reliableMessage.getMsgId(), reliableMessage);
        return true;
    }
}
