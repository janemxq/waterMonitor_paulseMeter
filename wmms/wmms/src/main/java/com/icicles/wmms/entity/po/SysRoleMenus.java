package com.icicles.wmms.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.icicles.wmms.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2020-06-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_role_menus")
public class SysRoleMenus extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * 角色编号
     */
    private String roleId;

    /**
     * 菜单编号
     */
    private String menusId;


}
