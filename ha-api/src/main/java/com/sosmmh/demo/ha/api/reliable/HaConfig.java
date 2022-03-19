package com.sosmmh.demo.ha.api.reliable;

import lombok.Data;

/**
 * @author Lixh
 */
@Data
public class HaConfig {
    /**
     * 提交ack 失败最大重试次数
     */
    private Integer maxRetries = 3;

    /**
     * 调度周期，默认30秒
     */
    private Long retryPeriod = 30000L;

    /**
     * 使用什么mq；rabbitmq，kafka
     */
    private String type = "simple";

    /**
     * 发送超时时间
     */
    private Long timeout = 5000L;

}
