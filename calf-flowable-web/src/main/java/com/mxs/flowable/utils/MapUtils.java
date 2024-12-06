package com.mxs.flowable.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MapUtils {


    /**
     * 从Map中获取指定key的String类型值
     *
     * @param map Map对象
     * @param key 指定的key
     * @return 如果key存在且value为String类型，返回对应的值；
     * 如果key不存在或value不是String类型，返回null
     */
    public static String getStringValue(Map<String, Object> map, String key) {
        if (map == null || key == null) {
            return null;
        }
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }


    /**
     * 从Map中获取指定key的String类型值
     *
     * @param map Map对象
     * @param key 指定的key
     * @return 如果key存在且value为String类型，返回对应的值；
     * 如果key不存在或value不是String类型，返回null
     */
    public static Integer getIntegerValue(Map<String, Object> map, String key) {
        if (map == null || key == null) {
            return null;
        }
        Object value = map.get(key);
        return value instanceof Integer ? (Integer) value : null;
    }

    /**
     * 将对象转化为Map
     *
     * @param object
     * @return map对象
     * @author by
     */
    public static Map<String, Object> objectToMap(Object object) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 获取对象的所有字段（包括私有字段）
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);  // 设置字段可访问
                map.put(field.getName(), field.get(object));  // 将字段名和字段值放入Map
            }
        } catch (IllegalAccessException e) {
            log.error(e.getMessage());
        }
        return map;
    }


    public static Map<String, Object> stringToMap(String json) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 将 JSON 字符串转换为 Map
            Map<String, Object> map = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
            return map;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new HashMap<>();
    }


}
