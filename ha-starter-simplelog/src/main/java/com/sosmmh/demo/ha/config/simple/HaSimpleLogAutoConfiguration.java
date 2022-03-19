package com.sosmmh.demo.ha.config.simple;

import com.sosmmh.demo.ha.api.reliable.ReliableStore;
import com.sosmmh.demo.ha.mysql.ReliableMysql;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author Lixh
 */
@Configuration
@ConditionalOnSingleCandidate(DataSource.class)
@AutoConfigureAfter({DataSourceAutoConfiguration.class})
public class HaSimpleLogAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ReliableStore reliableStore(JdbcTemplate jdbcTemplate) {
        ReliableStore reliableStore = new ReliableMysql(jdbcTemplate);
        return reliableStore;
    }

    @Bean
    @ConditionalOnMissingBean
    public SimpleLogProducer simpleLogProducer(ReliableStore reliableStore) {
        SimpleLogProducer simpleLogProducer = new SimpleLogProducer();
        simpleLogProducer.init(reliableStore);
        return simpleLogProducer;
    }
}
