package com.sosmmh.demo.ha.api.reliable;

import lombok.Data;

/**
 * @author Lixh
 */
@Data
public class ReliableMessage {
    private Long id;
    private String msgId;
    private String topic;
    private String message;
    private Integer status;
    private Integer retryCount;
    private Long nextExcTime;
}
