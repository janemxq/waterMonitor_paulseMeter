package com.icicles.wmms.filter;

import cn.hutool.core.util.StrUtil;
import com.icicles.wmms.config.Constants;
import com.icicles.wmms.entity.po.SysUser;
import com.icicles.wmms.service.SysUserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 过滤器
 * 功能一：请求预处理，将当前登录人的id放入请求中
 * @author lisy
 */
@Component
public class Interceptor implements HandlerInterceptor {
    @Resource
    private SysUserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //当前登录人的登录名称（loginName）
        String securityLoginUserName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        //当前登录人所属的村
        String securityLoginVillage = "";
        //当前登录人标识
        String securityLoginUserSn = "";
        if(StrUtil.isNotBlank(securityLoginUserName)){
            SysUser loginUser = userService.findByAccount(securityLoginUserName);
            if(loginUser!=null){
                //超级管理员不属于任何村
                if(!loginUser.getLoginAccount().equals(Constants.SUPER_ADMIN)){
                    securityLoginVillage = loginUser.getVillageSn();
                }
                securityLoginUserSn = loginUser.getSn();
            }
        }
        //请求预处理,添加当前登录人id
        request.setAttribute(Constants.CurrentLoginUser.SECURITY_ONLINE_LOGIN_SN,securityLoginUserSn);
        //请求预处理,添加当前登录人id
        request.setAttribute(Constants.CurrentLoginUser.SECURITY_ONLINE_VILLAGE,securityLoginVillage);
        return true;
    }
}
