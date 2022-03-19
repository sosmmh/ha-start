package com.sosmmh.demo.ha.api.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author Lixh
 */
@Builder
@Data
public class HaMessage {
    private String refId;
    private String message;
    private SendArgs sendArgs;
}
