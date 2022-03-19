package com.sosmmh.demo.ha.api.reliable;

import com.alibaba.fastjson.JSONObject;
import com.sosmmh.demo.ha.api.enums.MessageStatus;
import com.sosmmh.demo.ha.api.model.HaMessage;
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
    protected HaConfig haConfig;

    public void init(ReliableStore reliableStore, HaConfig haConfig) {
        this.reliableStore = reliableStore;
        this.haConfig = haConfig;
    }

    @Override
    public void send(HaMessage haMessage) {
        ReliableMessage reliableMessage = buildReliableMessage(haMessage);
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            preSend(reliableMessage);
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    if (actualSend(reliableMessage)) {
                        postSend(reliableMessage);
                    }
                }
            });
        } else {
            actualSend(reliableMessage);
        }
    }

    @Override
    public void resend() {
        List<ReliableMessage> reliableMessages = reliableStore.fetchTimeOutMessage4Retry(haConfig.getMaxRetries());
        for (ReliableMessage reliableMessage : reliableMessages) {
            try {
                if (actualSend(reliableMessage)) {
                    postSend(reliableMessage);
                }
            } catch (Exception e) {
                log.error("messageId={}|超时重发异常", reliableMessage.getMsgId(), e);
            }
        }
    }

    public void preSend(ReliableMessage reliableMessage) {
        reliableStore.preSend(reliableMessage);
        log.info("msgId={}|事务消息|存储发送记录|args={}",
                reliableMessage.getMsgId(), reliableMessage.getSendArgs());
    }

    public void postSend(ReliableMessage reliableMessage) {
        reliableStore.postSend(reliableMessage);
        log.info("msgId={}|事务消息|发送消息成功删除存储", reliableMessage.getMsgId());
    }

    /**
     * 发送消息到队列
     *
     * @param reliableMessage reliableMessage
     * @return 是否发送成功
     */
    protected abstract boolean actualSend(ReliableMessage reliableMessage);

    /**
     * 构建可靠消息
     *
     * @param haMessage haMessage
     * @return ReliableMessage
     */
    private ReliableMessage buildReliableMessage(HaMessage haMessage) {
        ReliableMessage reliableMessage = new ReliableMessage();
        reliableMessage.setRefId(haMessage.getRefId());
        reliableMessage.setMsgId(HaUtil.uuid());
        reliableMessage.setSendArgs(JSONObject.toJSONString(haMessage.getSendArgs()));
        reliableMessage.setMessage(haMessage.getMessage());
        reliableMessage.setStatus(MessageStatus.SENDING);
        reliableMessage.setNextExcTime(HaUtil.nextRetryTime(haConfig.getRetryPeriod()));
        reliableMessage.setType(haConfig.getType());
        return reliableMessage;
    }

}
