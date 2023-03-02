package com.zte.msg.alarmcenter.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author frp
 */
@Slf4j
public class ObjectUtils {

    /**
     * 根据属性名获取属性值
     */
    private static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter);
            return method.invoke(o);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 获取属性名数组
     */
    private static String[] getFiledName(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }

    /**
     * 获取属性类型(type)，属性名(name)，属性值(value)的map组成的list
     */
    private static List<Map<String, Object>> getFiledsInfo(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> infoMap = null;
        for (Field field : fields) {
            infoMap = new HashMap<>(16);
            infoMap.put("type", field.getType().toString());
            infoMap.put("name", field.getName());
            infoMap.put("value", getFieldValueByName(field.getName(), o));
            list.add(infoMap);
        }
        return list;
    }

    /**
     * 获取对象的所有属性值凭借字符串
     */
    public static String getFiledValues(Object o) {
        String[] fieldNames = getFiledName(o);
        StringBuilder stringBuilder = new StringBuilder();
        for (String fieldName : fieldNames) {
            System.out.println(fieldName);
            stringBuilder.append(null == getFieldValueByName(fieldName, o) ? "" : getFieldValueByName(fieldName, o));
        }
        return stringBuilder.toString();
    }
}