package com.icicles.wmms.utils;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import com.icicles.wmms.config.AppRunSchema;
import com.icicles.wmms.config.Constants;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * 获取网络时间（获取失败时，使用本地时间）
 * @author lisy
 */
@Component
public class WebDateUtils {
    /**
     * 默认时间格式
     */
    private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 日期格式
     */
    private static final String DAY_FORMAT = "yyyy-MM-dd";

    /**
     * 系统运行的模式：schema_single 为村委端，schema_center 为集管端
     */
    private static AppRunSchema appRunSchema;
    @Autowired
    public void setSchema(AppRunSchema appRunSchema) {
        WebDateUtils.appRunSchema = appRunSchema;
    }


    /**
     * 根据运行模式返回时间
     * 如果是村委端，返回从网络上获取的时间，如果是集管端返回本地时间
     * @return 字符型时间
     */
    public static String getWebDateBySchema(){
       return getWebDateBySchema(DEFAULT_FORMAT);
    }

    /**
     * 获取网络时间
     * @return 时间字符串，yyyy-MM-dd HH:mm:ss
     */
    public static String getWebDateString(){
        return getWebDateString(DEFAULT_FORMAT);
    }

    /**
     * 获取网络时间
     * @param format 时间格式
     * @return 时间字符串
     */
    public static String getWebDateString(String format){
        try{
            //访问百度获取时间
            String baidu = getWebsiteDatetime(DateConsist.WEB_URL2, format);
            //访问淘宝获取时间
            String taobao = getWebsiteDatetime(DateConsist.WEB_URL3, format);
            if(format.equals(DEFAULT_FORMAT)){
                //转成时间戳方便计算
                long baiduNum = convertTimeToLong(baidu);
                long taobaoNum = convertTimeToLong(taobao);
                //如果两个时间相差5s，说明可能有问题（比如网络特别慢、或者有不能访问的）
                if(Math.abs(baiduNum-taobaoNum)<=5){
                    return baidu;
                }else{
                    throw new Exception("错误了");
                }
            }else if(DAY_FORMAT.equals(format)){
                //如果两个日期不相等，说明可能有问题
                if(baidu.equals(taobao)){
                    return baidu;
                }else{
                    throw new Exception("错误了");
                }
            }else{
                return baidu;
            }
        }catch (Exception e){
            //如果有问题了，使用本地时间
            return DateFormatUtils.format(new Date(), format);
        }
    }

    /**
     * 根据运行模式返回时间
     * 如果是村委端，返回从网络上获取的时间，如果是集管端返回本地时间
     * @param format 时间格式
     * @return 字符型时间
     */
    public static String getWebDateBySchema(String format){
        if(Constants.SCHEMA_SINGLE.equals(appRunSchema.getSchema())){
            //村委端，使用网络时间
            return getWebDateString(format);
        }else{
            return DateFormatUtils.format(new Date(), format);
        }
    }


    /**
     *
     * @param webUrl 网络URL
     * @param format 格式
     * @return 时间
     * @throws Exception
     */
    private static String getWebsiteDatetime(String webUrl, String format) throws Exception{
        try {
            // 判断当前是否传入URL
            if (!StringUtils.isEmpty(webUrl)) {
                // 获取url对象
                URL url = new URL(webUrl);
                // 获取生成连接对象
                URLConnection uc = url.openConnection();
                // 发出连接请求
                uc.connect();
                // 读取网站日期时间
                long ld = uc.getDate();
                // 转化为时间对象
                Date date = new Date(ld);
                // 输出北京时间
                SimpleDateFormat sdf = new SimpleDateFormat(
                        format != null ? format : DEFAULT_FORMAT, Locale.CHINA);
                return sdf.format(date);
            } else {
                throw new Exception("URL Error!!!");
            }
        } catch (Exception e) {
            throw new Exception("URL Error!!!");
        }
    }

    /**
     * 返回时间戳（秒）
     * @param time 时间，格式必须为：yyyy-MM-dd HH:mm:ss
     * @return 时间戳（秒）
     */
    private static Long convertTimeToLong(String time) {
        if(StringUtils.isEmpty(time)){
            return 0L;
        }
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern(DEFAULT_FORMAT);
        LocalDateTime parse = LocalDateTime.parse(time, ftf);
        return LocalDateTime.from(parse).atZone(ZoneOffset.of("+8")).toInstant().toEpochMilli()/1000;
    }

    private class DateConsist{
        private static final String WEB_URL1 = "http://www.bjtime.cn";// bjTime
        private static final String WEB_URL2 = "http://www.baidu.com";// 百度
        private static final String WEB_URL3 = "http://www.taobao.com";// 淘宝
        private static final String WEB_URL4 = "http://www.ntsc.ac.cn";// 中国科学院国家授时中心
        private static final String WEB_URL5 = "http://www.360.cn";// 360
        private static final String WEB_URL6 = "http://www.beijing-time.org";// beijing-time
        private static final String WEB_URL7 = "http://www.jd.com/";// 京东
    }
}
