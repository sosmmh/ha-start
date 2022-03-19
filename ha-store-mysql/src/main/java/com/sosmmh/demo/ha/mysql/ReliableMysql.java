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

    String INSERT_SQL = "INSERT INTO `ha_message` (`ref_id`, `msg_id`, `send_args`, `message`, `status`, `next_exc_time`, `retry_count`, `type`) " +
            "VALUES('%s', '%s', '%s', '%s', %s, %s, %s, '%s')";

    String UPDATE_SQL = "UPDATE `ha_message` set next_exc_time = %s, retry_count = retry_count + 1, error = '%s' WHERE ref_id = '%s' AND msg_id = '%s'";

    String DELETE_SQL = "DELETE FROM `ha_message` WHERE ref_id = '%s' AND msg_id = '%s'";

    String SELECT_SQL = "SELECT `id`, `ref_id` AS refId, `msg_id` AS msgId, `send_args` AS sendArgs, `message` " +
            "FROM `ha_message` WHERE status = ? AND next_exc_time <= ? AND retry_count < ?";

    private JdbcTemplate jdbcTemplate;

    public ReliableMysql(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void preSend(ReliableMessage reliableMessage) {
        jdbcTemplate.execute(String.format(INSERT_SQL,
                reliableMessage.getRefId(),
                reliableMessage.getMsgId(),
                reliableMessage.getSendArgs(),
                reliableMessage.getMessage(),
                reliableMessage.getStatus(),
                reliableMessage.getNextExcTime(),
                1, reliableMessage.getType()));
    }

    @Override
    public void postSend(ReliableMessage reliableMessage) {
        jdbcTemplate.execute(String.format(DELETE_SQL, reliableMessage.getRefId(), reliableMessage.getMsgId()));
    }

    @Override
    public List<ReliableMessage> fetchTimeOutMessage4Retry(Integer maxRetryCount) {
        List<ReliableMessage> messages = jdbcTemplate.query(SELECT_SQL,
                new Object[]{MessageStatus.SENDING, HaUtil.now(), maxRetryCount},
                new BeanPropertyRowMapper<>(ReliableMessage.class));

        return messages;
    }
}
