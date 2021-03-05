package com.zte.msg.alarmcenter.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author frp
 */
public class JSONUtils {

    /**
     * 数据不合法
     */
    public static final String MSG_E1004 = "E1004";

    private static boolean isPretty = false;
    private static ObjectMapper mapper;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static boolean isPretty() {
        return isPretty;
    }

    /**
     * 获取ObjectMapper实例
     *
     * @param createNew 方式：true，新实例；false,存在的mapper实例
     * @return
     */
    public static synchronized ObjectMapper getMapperInstance(boolean createNew) {
        if (createNew) {
            return new ObjectMapper();
        } else if (mapper == null) {
            mapper = new ObjectMapper();
        }
        return mapper;
    }

    public static String toJson(Object object) {
        try {
            return beanToJsonString(object, true, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将java对象转换成json字符串
     */
    public static String beanToJsonString(Object object) {
        return beanToJsonString(object, true, false);
    }

    /**
     * 将java对象转换成json字符串
     *
     * @param object
     * @param isNotNull
     * @return
     */
    public static String beanToJsonString(Object object, boolean isNotNull) {
        return beanToJsonString(object, isNotNull, false);
    }

    /**
     * 将java对象转换成json字符串
     *
     * @param object
     * @param isNotNull
     * @param createNew
     * @return
     */
    public static String beanToJsonString(Object object, boolean isNotNull, boolean createNew) {
        String jsonString = null;
        try {
            ObjectMapper objectMapper = getMapperInstance(createNew);
            if (isNotNull) {
                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            }
            if (isPretty) {
                jsonString = objectMapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(object);
            } else {
                jsonString = objectMapper.writeValueAsString(object);
            }
        } catch (
                JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonString;
    }

    public static Map transToMAP(Map parameterMap){
        // 返回值Map
        Map returnMap = new HashMap(16);
        Iterator entries = parameterMap.entrySet().iterator();
        Map.Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if(null == valueObj){
                value = "";
            }else if(valueObj instanceof String[]){
                String[] values = (String[])valueObj;
                for (String s : values) {
                    value = s + ",";
                }
                value = value.substring(0, value.length()-1);
            }else{
                value = valueObj.toString();
            }
            returnMap.put(name, value);
        }
        return returnMap;
    }

    /**
     * 将json字符串转换成java对象
     */
    public static Object jsonStringToBean(String json, Class<?> cls) throws JsonProcessingException {
        return jsonStringToBean(json, cls, false);
    }

    /**
     * 将json字符串转换成java对象
     *
     * @param json
     * @param field
     * @param cls
     * @return
     */
    public static Object jsonArrayToBean(String json, String field, Class<?> cls) throws JsonProcessingException {
        String vjson = "{\"" + field + "\":" + json + "}";
        return jsonStringToBean(vjson, cls, false);
    }

    /**
     * 将json字符串转换成java对象
     *
     * @param json
     * @param cls
     * @param createNew
     * @return
     */
    public static Object jsonStringToBean(String json, Class<?> cls, Boolean createNew) throws JsonProcessingException {
        ObjectMapper objectMapper = getMapperInstance(createNew);
        return objectMapper.readValue(json, cls);
    }


    /**
     * 将json字符串转换成java List对象
     *
     * @param json
     * @param cls
     * @param createNew
     * @return
     * @throws JsonProcessingException
     */
    public static Object jsonStringToBeanList(String json, Class<?> cls, Boolean createNew) throws JsonProcessingException {
        ObjectMapper objectMapper = getMapperInstance(createNew);
        //专门创建bean的集合类类型供json转换
        JavaType javaType = getCollectionType(objectMapper, ArrayList.class, cls);
        return objectMapper.readValue(json, javaType);
    }

    /**
     * 获取泛型的Collection Type
     *
     * @param objectMapper    ObjectMapper
     * @param collectionClass 泛型的Collection
     * @param elementClasses  元素类
     * @return JavaType       Java类型
     */
    public static JavaType getCollectionType(ObjectMapper objectMapper,
                                             Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(
                collectionClass, elementClasses);
    }

    /**
     * 将json字符串转换成java List对象
     *
     * @param json
     * @param cls
     * @return
     * @throws JsonProcessingException
     */
    public static Object jsonStringToBeanList(String json, Class<?> cls) throws JsonProcessingException {
        return jsonStringToBeanList(json, cls, false);
    }

    public static <T> T toObject(String json, Class<T> clazz) throws Exception {
        try {
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
