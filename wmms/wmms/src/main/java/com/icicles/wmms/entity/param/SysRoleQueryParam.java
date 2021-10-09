package com.icicles.wmms.entity.param;

import com.icicles.wmms.entity.po.SysRole;
import lombok.*;

/**
 * <p>
 * 角色对内查询条件
 * </p>
 *
 * @author 
 * @since 2020-06-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRoleQueryParam extends BaseParam<SysRole> {


    private String roleCode;

    private String roleName;

    private String isValid;

    private String remark;


}
