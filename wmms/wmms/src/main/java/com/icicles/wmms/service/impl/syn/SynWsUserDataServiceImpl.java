package com.icicles.wmms.service.impl.syn;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icicles.wmms.config.Constants;
import com.icicles.wmms.entity.po.SysRole;
import com.icicles.wmms.entity.po.SysUser;
import com.icicles.wmms.service.AbstractSynData;
import com.icicles.wmms.service.SysRoleService;
import com.icicles.wmms.service.WsSynService;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lisy
 */
public class SynWsUserDataServiceImpl extends AbstractSynData<SysUser> {

    private SysRoleService sysRoleService;
    private Map<String,SysRole> roleMap = new HashMap<>();
    /**
     * 构造函数，对必要的参数赋值
     *
     * @param service      数据服务类
     * @param wsSynService 同步日志服务类
     */
    private SynWsUserDataServiceImpl(IService<SysUser> service, WsSynService wsSynService) {
        super(service, wsSynService);
    }

    public SynWsUserDataServiceImpl(IService<SysUser> service, WsSynService wsSynService,SysRoleService sysRoleService) {
        this(service,wsSynService);
        this.sysRoleService = sysRoleService;
    }

    @Override
    protected void synFun(String dataType, List<SysUser> data) {
    }

    /**
     * 不同的保存记录的方法
     * @param data     需要同步的数据
     */
    @Override
    public void saveData(List<SysUser> data){
        for (SysUser temp:
        data) {
            //不同步超级管理员(如果村委端的用户角色标识和集管端超级管理员标识相同，不同步了)
            if(this.isSuperAdmin(temp)){
                continue;
            }
            QueryWrapper<SysUser> queryWrap = new QueryWrapper<> ();
            queryWrap.lambda().eq(SysUser::getSn,temp.getSn()).last("limit 1");
            SysUser user = super.getService().getOne(queryWrap);
            if (user == null){
                super.getService().save(temp);
            }
        }
    }

    private boolean isSuperAdmin(SysUser u){
        if(u.getType()==null){
            return false;
        }
        String roleType = String.valueOf(u.getType());
        if(StringUtils.isBlank(roleType)){
            return false;
        }
        SysRole role;
        role = roleMap.get(roleType);
        if(role==null){
            role = sysRoleService.findById(roleType);
            if(role==null){
                return false;
            }else{
                //把角色缓存下来
                roleMap.put(roleType,role);
            }
        }
        return role.getRoleCode().equals(Constants.SUPER_ADMIN);
    }
}
