package com.icicles.wmms.entity.form;

import com.icicles.wmms.entity.po.SysRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 角色对外查询保存实体
 * </p>
 *
 * @author 
 * @since 2020-06-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="SysRoleForm", description="角色对外查询保存实体")
public class SysRoleForm extends BaseForm<SysRole> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "role_code", value = "角色代码||varchar(32)")
    @NotBlank(message = "role_code不能为空")
    @Length(min = 2, max = 20, message = "role_code长度在3到20个字符")
    private String roleCode;

    @ApiModelProperty(name = "role_name", value = "角色名称||varchar(200)")
    @NotBlank(message = "role_name不能为空")
    @Length(min = 2, max = 20, message = "role_name长度在3到20个字符")
    private String roleName;

    @ApiModelProperty(name = "is_valid", value = "是否有效（0:否；1:是）||int(1)")
    @NotNull(message = "is_valid不能为空")
    @Range(min = 0,max = 1,message = "是否生效的有效值只能为0或者1")
    private Integer isValid;

    @ApiModelProperty(name = "remark", value = "备注||varchar(500)")
    @Length(max = 500, message = "长度最大为500")
    private String remark;
}
