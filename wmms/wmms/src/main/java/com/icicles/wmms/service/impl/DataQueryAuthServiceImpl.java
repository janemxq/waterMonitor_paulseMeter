package com.icicles.wmms.service.impl;

import com.icicles.wmms.config.Constants;
import com.icicles.wmms.entity.po.SysRole;
import com.icicles.wmms.entity.po.SysUser;
import com.icicles.wmms.service.AbstractDataQueryAuthService;
import com.icicles.wmms.service.SysRoleService;
import com.icicles.wmms.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lisy
 */
@Service
public class DataQueryAuthServiceImpl extends AbstractDataQueryAuthService {
    @Resource
    private SysRoleService sysRoleService;
    @Autowired
    public DataQueryAuthServiceImpl(SysUserService userService) {
        super(userService);
    }

    public boolean isSuperAdmin(SysUser u){
        if(u==null||u.getType()==null){
            return false;
        }
        SysRole role = sysRoleService.findById(String.valueOf(u.getType()));
        if(role==null){
            return false;
        }
        return role.getRoleCode().equals(Constants.SUPER_ADMIN);
    }
}
