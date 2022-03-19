package com.sosmmh.demo.ha.config.kafka;

import com.sosmmh.demo.ha.api.reliable.HaConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Lixh
 */
@Data
@ConfigurationProperties("hamq")
public class HaProperties {

    private Ha ha = new Ha();

    @Data
    public static class Ha extends HaConfig {

    }
}
