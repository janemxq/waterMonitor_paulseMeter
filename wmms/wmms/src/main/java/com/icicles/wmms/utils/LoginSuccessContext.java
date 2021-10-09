package com.icicles.wmms.utils;

/**
 * 在线程中保存登录信息
 */
public class LoginSuccessContext {
    private static ThreadLocal<String> LoginSuccessHolder = new ThreadLocal<>();

    public static void setLoginUserName(String loginUser) {
        LoginSuccessHolder.set(loginUser);
    }

    public static void remove() {
        LoginSuccessHolder.remove();
    }

    public static String getLoginUserName() {
        return LoginSuccessHolder.get();
    }
}
