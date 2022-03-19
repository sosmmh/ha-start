package com.sosmmh.demo.ha.util;

import java.util.UUID;

/**
 * @author Lixh
 */
public class HaUtil {
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取periodTime后的时间戳
     * @param periodTime 毫秒
     * @return
     */
    public static Long nextRetryTime(Long periodTime) {
        return System.currentTimeMillis() + periodTime;
    }

    /**
     * 获取当前时间戳
     * @return
     */
    public static Long now() {
        return System.currentTimeMillis();
    }
}
