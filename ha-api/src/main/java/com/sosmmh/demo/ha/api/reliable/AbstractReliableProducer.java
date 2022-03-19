package com.sosmmh.demo.ha.api.reliable;

import com.sosmmh.demo.ha.api.enums.MessageStatus;
import com.sosmmh.demo.ha.api.utils.HaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

/**
 * @author Lixh
 */
@Slf4j
public abstract class AbstractReliableProducer implements ReliableProducer {

    protected ReliableStore reliableStore;

    public void init(ReliableStore reliableStore) {
        this.reliableStore = reliableStore;
    }

    @Override
    public void send(String topic, String message) {
        String messageId = HaUtil.uuid();
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            preSend(messageId, topic, message);
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    if (actualSend(messageId, topic, message)) {
                        postSend(messageId);
                    }
                }
            });
        } else {
            actualSend(messageId, topic, message);
        }
    }

    @Override
    public void resend() {
        List<ReliableMessage> reliableMessages = reliableStore.fetchTimeOutMessage4Retry();
        for (ReliableMessage reliableMessage : reliableMessages) {
            try {
                if (actualSend(reliableMessage.getMsgId(), reliableMessage.getTopic(), reliableMessage.getMessage())) {
                    postSend(reliableMessage.getMsgId());
                }
            } catch (Exception e) {
                log.error("messageId={}|超时重发异常", reliableMessage.getMsgId(), e);
            }
        }

    }

    public void preSend(String messageId, String topic, String message) {
        ReliableMessage reliableMessage = new ReliableMessage();
        reliableMessage.setMsgId(messageId);
        reliableMessage.setTopic(topic);
        reliableMessage.setMessage(message);
        reliableMessage.setStatus(MessageStatus.SENDING);
        reliableMessage.setNextExcTime(60000L);
        reliableStore.preSend(reliableMessage);
        log.info("msgId={}|事务消息|存储发送记录|topic={}|message={}", messageId, topic, message);
    }

    public void postSend(String messageId) {
        reliableStore.postSend(messageId);
        log.info("msgId={}|事务消息|发送消息成功删除存储", messageId);
    }

    /**
     * 发送消息到队列
     *
     * @param messageId messageId
     * @param topic     topic
     * @param message   message
     * @return 是否发送成功
     */
    public abstract boolean actualSend(String messageId, String topic, String message);


}
