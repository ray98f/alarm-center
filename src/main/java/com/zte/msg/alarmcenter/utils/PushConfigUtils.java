package com.zte.msg.alarmcenter.utils;

import org.apache.commons.lang3.RandomUtils;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/1/11 16:36
 */
public class PushConfigUtils {

    public static String getTag() {
        return "Script" + System.currentTimeMillis() + "_" + RandomUtils.nextInt();
    }
}
