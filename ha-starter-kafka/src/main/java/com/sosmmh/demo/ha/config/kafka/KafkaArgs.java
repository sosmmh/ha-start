package com.sosmmh.demo.ha.config.kafka;

import com.sosmmh.demo.ha.api.model.SendArgs;
import lombok.Data;

/**
 * @author Lixh
 */
@Data
public class KafkaArgs extends SendArgs {
    private String topic;
    private Integer partition;
    private String key;
}
