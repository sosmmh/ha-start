package com.sosmmh.demo.ha.config.kafka;

import com.sosmmh.demo.ha.api.reliable.AbstractReliableProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * @author Lixh
 */
@Slf4j
public class ReliableKafkaProducer extends AbstractReliableProducer {

    private KafkaTemplate kafkaTemplate;

    public ReliableKafkaProducer(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public boolean actualSend(String messageId, String topic, String message) {
        final boolean[] ok = {false};
        try {
            ListenableFuture<SendResult> future = kafkaTemplate.send(topic, message);
            future.addCallback(new ListenableFutureCallback<SendResult>() {
                @Override
                public void onFailure(Throwable ex) {
                    log.warn("messageId={}|发送消息失败", message, ex);
                }

                @Override
                public void onSuccess(SendResult result) {
                    log.info("messageId={}|发送消息成功", messageId);
                    ok[0] = true;
                }
            });
            future.get();
        } catch (Exception e) {
            log.error("messageId={}|发送消息异常", messageId, e);
        }
        return ok[0];
    }
}
