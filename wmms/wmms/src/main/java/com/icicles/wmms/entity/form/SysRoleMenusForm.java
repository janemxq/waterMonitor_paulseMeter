package com.icicles.wmms.entity.form;

import com.icicles.wmms.entity.po.SysRoleMenus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * <p>
 * 对外查询保存实体
 * </p>
 *
 * @author 
 * @since 2020-06-24
 */
@Data
@ApiModel(value="SysRoleMenusForm", description="对外查询保存实体")
public class SysRoleMenusForm{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "role_id", value = "角色编号||varchar(32)")
    @NotBlank(message = "role_id不能为空")
    @Length(min = 1, max = 20, message = "role_id长度在1到20个字符")
    private String roleId;

    @ApiModelProperty(name = "menus_ids", value = "菜单id,用数组形式提交||varchar(32)")
    private List<String> menusIds;
}
