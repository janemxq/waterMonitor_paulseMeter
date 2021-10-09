package com.icicles.wmms.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.icicles.wmms.entity.param.SysRoleMenusQueryParam;
import lombok.*;

/**
 * <p>
 * 对外查询条件
 * </p>
 *
 * @author 
 * @since 2020-06-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="SysRoleMenusQueryForm", description="对外查询条件")
public class SysRoleMenusQueryForm extends BaseQueryForm<SysRoleMenusQueryParam> {


    @ApiModelProperty(name = "role_id", value = "角色编号||varchar(32)")
    private String roleId;

    @ApiModelProperty(name = "menus_id", value = "菜单编号||varchar(32)")
    private String menusId;
    @ApiModelProperty(value = "查询开始时间")
    private String createdTimeStart;

    @ApiModelProperty(value = "查询结束时间")
    private String createdTimeEnd;


}
