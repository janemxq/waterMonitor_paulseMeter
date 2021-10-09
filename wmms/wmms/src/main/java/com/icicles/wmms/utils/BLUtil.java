package com.icicles.wmms.utils;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class BLUtil {


    /**
     * 获得UUID值
     *
     * @param b 如果b = true，去掉横线，如果b=false,保留横线
     * @return UUID值
     */
    public static String getUUID(boolean b) {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        if (b) {
            return str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);
        } else {
            return str;
        }
    }

    /**
     * 将对象的大写转换为下划线加小写，例如：userName-->user_name
     *
     * @param object
     * @return
     * @throws JsonProcessingException
     */
    public static String toUnderlineJSONString(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String reqJson = mapper.writeValueAsString(object);
        return reqJson;
    }

    /**
     * 将下划线转换为驼峰的形式，例如：user_name-->userName
     *
     * @param json
     * @param clazz
     * @return
     * @throws IOException
     */
    public static <T> T toSnakeObject(String json, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        // mapper的configure方法可以设置多种配置（例如：多字段 少字段的处理）
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        T reqJson =  mapper.readValue(json, clazz);
        return reqJson;
    }


    public static void main(String[] args) {
           /* Room room =  new Room();
            room.setRoom(1111111L);
            MvRoom mvRoom =  new MvRoom();
            classToClass(room,mvRoom);
            System.out.println(room.getRoom());
            System.out.println(mvRoom.getRoom());*/
         SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            System.out.println(df.parse("2020-04-15 00:00:00").before(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}
