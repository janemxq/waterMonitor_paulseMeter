package com.icicles.wmms.config;

import com.icicles.wmms.entity.po.SysRole;
import com.icicles.wmms.entity.po.SysUser;
import com.icicles.wmms.exception.ApiException;
import com.icicles.wmms.service.SysRoleService;
import com.icicles.wmms.service.SysUserService;
import com.icicles.wmms.utils.LoginSuccessContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service("myUserDetailService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleService sysRoleService;

    @Override
    public UserDetails loadUserByUsername(String uniqueId) {

        SysUser user = sysUserService.findByAccount(uniqueId);
        log.info("load user by username :{}", user.toString());
        if(user==null){
                throw new ApiException("无权登录", HttpStatus.UNAUTHORIZED);
        }
        if(user.getType()<=0){
            throw new ApiException("无权登录", HttpStatus.UNAUTHORIZED);
        }
        //将登录名称保存到线程变量中，目前用于登录成功后更新登录次数
        LoginSuccessContext.setLoginUserName(uniqueId);

        return new org.springframework.security.core.userdetails.User(
                user.getLoginAccount(),
                user.getLoginPass(),
                user.getIsValid()==0?false:true,
                user.getAccountNonExpired()==0?false:true,
                user.getCredentialsNonExpired()==0?false:true,
                user.getAccountNonLocked()==0?false:true,
                this.obtainGrantedAuthorities(user));
    }

    /**
     * 获得登录者所有角色的权限集合.
     *
     * @param user
     * @return
     */
    protected Set<GrantedAuthority> obtainGrantedAuthorities(SysUser user) {
        List<SysRole> roles =  sysRoleService.findByAccount(user.getLoginAccount());
        log.info("user:{},roles:{}", user.getLoginAccount(), roles);
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleCode())).collect(Collectors.toSet());
    }
}
