package com.icicles.wmms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.icicles.wmms.entity.po.SysUser;

import java.security.Principal;

/**
 * 根据权限查询数据
 * @author lisy
 */
public abstract class AbstractDataQueryAuthService {
    private SysUserService userService;
    public AbstractDataQueryAuthService(SysUserService userService){
        this.userService = userService;
    }

    public <T> QueryWrapper<T> setQuery(QueryWrapper<T> queryWrapper, Principal principal){
        SysUser u = this.getUser(principal);
        if(u!=null){
            if(u.getType()<=3){
                queryWrapper.eq("village_sn",u.getVillageSn());
            }
        }
        return queryWrapper;
    }
    public int getUserType(Principal principal){
        SysUser u = this.getUser(principal);
        if(u==null){
            return -1;
        }else{
            return u.getType();
        }
    }

    public SysUser getUser(Principal principal){
        if(principal==null){
            return null;
        }
        QueryWrapper<SysUser> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.lambda().eq(SysUser::getLoginAccount,principal.getName());
        return userService.getOne(userQueryWrapper);
    }
}
