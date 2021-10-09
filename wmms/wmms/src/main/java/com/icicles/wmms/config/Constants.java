package com.icicles.wmms.config;

/**
 * @author lisy
 */
public class Constants {
    /**
     * 运行在村委端的标识
     */
    public static final String SCHEMA_SINGLE = "schema_single";
    /**
     * 运行在集管端的标识
     */
    public static final String SCHEMA_CENTER = "schema_center";
    /**
     * key
     */
    public static final String URL_KEY = "http://www.baidu.com";

    /**
     * 用水量异常的key
     */
    public static final String WATER_EXCEPTION = "water_exception";

    /**
     * 用水量异常最小阈值的key
     */
    public static final String WATER_EXCEPTION_MIN = "water_exception_min";

    /**
     * 用水量异常最大阈值的key
     */
    public static final String WATER_EXCEPTION_MAX = "water_exception_max";

    /**
     * 缴费异常的key
     */
    public static final String PAYMENT_EXCEPTION = "payment_exception";

    /**
     * 缴费异常最小阈值的key
     */
    public static final String PAY_EXCEPTION_MIN = "pay_exception_min";

    /**
     * 缴费异常最大阈值的key
     */
    public static final String PAY_EXCEPTION_MAX = "pay_exception_max";

    /**
     * 未交费异常的key
     */
    public static final String NO_PAY_EXCEPTION = "no_pay_exception";
    /**
     * 未交费用水量下限key
     */
    public static final String NO_PAY_EXCEPTION_VOLUME = "no_pay_volume";
    /**
     * 未交费时间下限（天）key
     */
    public static final String NO_PAY_EXCEPTION_VOLUME_LONG = "no_pay_long";

    /**
     * 在请求中预处理当前登录人的信息
     */
    public static final class CurrentLoginUser{
        //当前登录人所属的村
        public static final String SECURITY_ONLINE_VILLAGE = "securityLoginVillage";
        //当前登录人的唯一标识
        public static final String SECURITY_ONLINE_LOGIN_SN = "securityLoginUserSn";
    }

    /**
     * 执行抄表是日期
     */
    public static final String READ_METER_TIME = "read_meter_time";

    /**
     * 菜单的父级id
     */
    public static final String MENU_PID = "-1";

    /**
     * 超级管理员对应的角色名称
     */
    public static final String SUPER_ADMIN = "superAdmin";

    /**
     * 参数表中默认密码对应的key
     */
    public static final String RESET_PASSWORD = "resetPassword";

    /**
     * 计算用水量
     */
    public static final class WaterBase{
        /**
         *默认多少脉冲对应1方水的配置表的键
         */
        public static final String DEFAULT_PLUSE = "default_pluse";
        /**
         *默认采用什么计量用水量的配置表的键
         */
        public static final String WATER_VOLUME_BASE = "water_volume_base";
        /**
         * 默认采用什么计量用水量:脉冲
         */
        public static final String WATER_VOLUME_BASE_PLUSE = "pluse";
        /**
         * 默认采用什么计量用水量:用水量
         */
        public static final String WATER_VOLUME_BASE_VOLUME = "volume";
        /**
         * 默认采用什么计量用水量:用水量
         */
        public static final String FREE_WATER = "free_water";
    }



    /**
     * 默认的集管端url
     */
    public static final String CENTER_SERVER_URL = "http://127.0.0.1:19999";
    /**
     * 默认集管端的url的key
     */
    public static final String CENTER_SERVER_URL_KEY = "CENTER_SERVER_URL";

    /**
     * 同步数据时的默认签名密钥
     */
    public static final String API_SECRET = "WBLiNG99Success";
    /**
     * 同步数据时的默认签名密钥的key
     */
    public static final String API_KEY = "API_KEY";
    /**
     * 系统运行的模式
     */
    public static final String APP_SCHEMA = "APP_SCHEMA";
}
