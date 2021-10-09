package com.icicles.wmms.entity.param;

import com.icicles.wmms.entity.po.SysUser;
import lombok.*;

/**
 * <p>
 * 用水监测管理平台用户信息表对内查询条件
 * </p>
 *
 * @author damon
 * @since 2020-06-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserQueryParam extends BaseParam<SysUser> {


    private String name;

    private String idCard;

    private String phone;

    private String loginAccount;

    private String loginPass;

    private String isValid;

    private String accountNonExpired;

    private String credentialsNonExpired;

    private String accountNonLocked;

    private String type;

    private String branchId;

    private String villageId;

    private String villageName;

    private String villageSn;

    private String remark;


}
