package com.sosmmh.demo.ha.config.simple;

import com.sosmmh.demo.ha.api.reliable.ReliableStore;
import com.sosmmh.demo.ha.mysql.ReliableMysql;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author Lixh
 */
@Configuration
@EnableConfigurationProperties({HaProperties.class})
@ConditionalOnSingleCandidate(DataSource.class)
@ConditionalOnProperty(
        name = {"hamq.ha.type"},
        havingValue = "simple"
)
public class HaSimpleLogAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ReliableStore reliableStore(JdbcTemplate jdbcTemplate) {
        ReliableStore reliableStore = new ReliableMysql(jdbcTemplate);
        return reliableStore;
    }


    @Bean
    @ConditionalOnMissingBean
    public SimpleLogProducer simpleLogProducer(ReliableStore reliableStore,
                                               HaProperties haProperties) {
        SimpleLogProducer simpleLogProducer = new SimpleLogProducer();
        simpleLogProducer.init(reliableStore, haProperties.getHa());
        return simpleLogProducer;
    }
}
