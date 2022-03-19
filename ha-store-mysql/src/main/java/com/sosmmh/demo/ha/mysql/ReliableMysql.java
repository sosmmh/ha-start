package com.sosmmh.demo.ha.mysql;

import com.sosmmh.demo.ha.api.enums.MessageStatus;
import com.sosmmh.demo.ha.api.reliable.ReliableMessage;
import com.sosmmh.demo.ha.api.reliable.ReliableStore;
import com.sosmmh.demo.ha.api.utils.HaUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * @author Lixh
 */
public class ReliableMysql implements ReliableStore {

    String INSERT_SQL = "INSERT INTO `ha_message` (`msg_id`, `topic`, `message`, `status`, `next_exc_time`, retry_count) " +
            "VALUES('%s', '%s', '%s', %s, %s, 1)";

    String UPDATE_SQL = "UPDATE `ha_message` set status = %s, next_exc_time = %s, retry_count = retry_count + 1 WHERE msg_id = '%s'";

    String DELETE_SQL = "DELETE FROM `ha_message` WHERE msg_id = '%s'";

    String SELECT_SQL = "SELECT `id`, `msg_id` AS msgId, `topic`, `message` " +
            "FROM `ha_message` WHERE status = ? AND next_exc_time <= ? AND retry_count < 3";

    private JdbcTemplate jdbcTemplate;

    public ReliableMysql(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void preSend(ReliableMessage reliableMessage) {
        jdbcTemplate.execute(String.format(INSERT_SQL,
                reliableMessage.getMsgId(),
                reliableMessage.getTopic(),
                reliableMessage.getMessage(),
                reliableMessage.getStatus(),
                reliableMessage.getNextExcTime()));
    }

    @Override
    public void postSend(String messageId) {
        jdbcTemplate.execute(String.format(DELETE_SQL, messageId));
    }

    @Override
    public List<ReliableMessage> fetchTimeOutMessage4Retry() {
        List<ReliableMessage> messages = jdbcTemplate.query(SELECT_SQL,
                new Object[]{MessageStatus.SENDING, HaUtil.now()},
                new BeanPropertyRowMapper<>(ReliableMessage.class));

        return messages;
    }
}
