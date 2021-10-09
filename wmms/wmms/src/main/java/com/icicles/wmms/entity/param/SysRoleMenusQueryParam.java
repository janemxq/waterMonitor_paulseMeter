package com.icicles.wmms.entity.param;

import com.icicles.wmms.entity.po.SysRoleMenus;
import lombok.*;

/**
 * <p>
 * 对内查询条件
 * </p>
 *
 * @author 
 * @since 2020-06-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRoleMenusQueryParam extends BaseParam<SysRoleMenus> {


    private String roleId;

    private String menusId;


}
