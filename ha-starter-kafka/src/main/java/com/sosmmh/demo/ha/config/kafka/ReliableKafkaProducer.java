package com.sosmmh.demo.ha.config.kafka;

import com.alibaba.fastjson.JSONObject;
import com.sosmmh.demo.ha.api.reliable.AbstractReliableProducer;
import com.sosmmh.demo.ha.api.reliable.ReliableMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.TimeUnit;

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
    public boolean actualSend(ReliableMessage reliableMessage) {

        final boolean[] ok = {false};
        try {
            ListenableFuture<SendResult> future = kafkaTemplate.send(getProducerRecord(reliableMessage));
            future.addCallback(new ListenableFutureCallback<SendResult>() {
                @Override
                public void onFailure(Throwable ex) {
                    log.warn("messageId={}|发送消息失败", reliableMessage.getMsgId(), ex);
                }

                @Override
                public void onSuccess(SendResult result) {
                    log.info("messageId={}|发送消息成功", reliableMessage.getMsgId());
                    ok[0] = true;
                }
            });
            future.get(haConfig.getTimeout(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("messageId={}|发送消息异常", reliableMessage.getMsgId(), e);
        }
        return ok[0];
    }

    private ProducerRecord getProducerRecord(ReliableMessage reliableMessage) {
        JSONObject sendArgs = JSONObject.parseObject(reliableMessage.getSendArgs());
        String topic = sendArgs.getString("topic");
        String key = sendArgs.getString("key");
        Integer partition = sendArgs.getInteger("partition");
        ProducerRecord producerRecord = new ProducerRecord(topic, partition, key, reliableMessage.getMessage());
        return producerRecord;
    }

}
