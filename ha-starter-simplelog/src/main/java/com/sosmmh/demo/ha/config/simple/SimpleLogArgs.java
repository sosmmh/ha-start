package com.sosmmh.demo.ha.config.simple;

import com.sosmmh.demo.ha.api.model.SendArgs;
import lombok.Data;

/**
 * @author Lixh
 */
@Data
public class SimpleLogArgs extends SendArgs {
    private String logName;
}
