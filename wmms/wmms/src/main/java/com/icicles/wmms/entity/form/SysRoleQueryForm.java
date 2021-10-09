package com.icicles.wmms.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.icicles.wmms.entity.param.SysRoleQueryParam;
import lombok.*;

/**
 * <p>
 * 角色对外查询条件
 * </p>
 *
 * @author 
 * @since 2020-06-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="SysRoleQueryForm", description="角色对外查询条件")
public class SysRoleQueryForm extends BaseQueryForm<SysRoleQueryParam> {


    @ApiModelProperty(name = "role_code", value = "角色代码||varchar(32)")
    private String roleCode;

    @ApiModelProperty(name = "role_name", value = "角色名称||varchar(200)")
    private String roleName;

    @ApiModelProperty(name = "is_valid", value = "是否有效（0:否；1:是）||int(1)")
    private String isValid;

    @ApiModelProperty(name = "remark", value = "备注||varchar(500)")
    private String remark;
    @ApiModelProperty(value = "查询开始时间")
    private String createdTimeStart;

    @ApiModelProperty(value = "查询结束时间")
    private String createdTimeEnd;


}
