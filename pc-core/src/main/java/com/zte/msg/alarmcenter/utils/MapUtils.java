package com.zte.msg.alarmcenter.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/1/5 12:24
 */
@Slf4j
public class MapUtils {

    public static Map<String, Object> objectToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>(16);
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(obj));
            } catch (IllegalAccessException e) {
                log.error("IllegalAccessException : {}", e.getMessage());
            }
        }
        return map;
    }
}
