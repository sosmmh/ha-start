package com.sosmmh.demo.ha.config.kafka;

import com.sosmmh.demo.ha.api.reliable.ReliableStore;
import com.sosmmh.demo.ha.mysql.ReliableMysql;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @author Lixh
 */
@Configuration
@EnableAutoConfiguration
public class HaKafkaAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ReliableStore reliableStore(JdbcTemplate jdbcTemplate) {
        ReliableStore reliableStore = new ReliableMysql(jdbcTemplate);
        return reliableStore;
    }

    @Bean
    @ConditionalOnMissingBean
    public ReliableKafkaProducer reliableKafkaProducer(KafkaTemplate kafkaTemplate,
                                                       ReliableStore reliableStore) {
        ReliableKafkaProducer reliableKafkaProducer = new ReliableKafkaProducer(kafkaTemplate);
        reliableKafkaProducer.init(reliableStore);
        return reliableKafkaProducer;
    }
}
