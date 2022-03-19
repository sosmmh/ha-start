package com.sosmmh.demo.ha.api.reliable;

import lombok.Data;

/**
 * @author Lixh
 */
@Data
public class ReliableMessage {
    private Long id;
    private String refId;
    private String msgId;
    private String message;
    private Integer status;
    private Integer retryCount;
    private Long nextExcTime;
    private String type;
    private String sendArgs;
}
