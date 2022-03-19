package com.sosmmh.demo.ha.config.kafka;

import com.sosmmh.demo.ha.api.reliable.ReliableStore;
import com.sosmmh.demo.ha.mysql.ReliableMysql;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;

import javax.sql.DataSource;

/**
 * @author Lixh
 */
@Configuration
@ConditionalOnSingleCandidate(DataSource.class)
@EnableConfigurationProperties({HaProperties.class})
@ConditionalOnProperty(
        name = {"hamq.ha.type"},
        havingValue = "kafka"
)
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
                                                       ReliableStore reliableStore,
                                                       HaProperties haProperties) {

        ReliableKafkaProducer reliableKafkaProducer = new ReliableKafkaProducer(kafkaTemplate);
        reliableKafkaProducer.init(reliableStore, haProperties.getHa());
        return reliableKafkaProducer;
    }
}
