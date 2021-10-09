package com.icicles.wmms.entity.form;

import com.icicles.wmms.entity.po.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 用水监测管理平台用户信息表对外查询保存实体
 * </p>
 *
 * @author damon
 * @since 2020-06-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="SysUserForm", description="用水监测管理平台用户信息表对外查询保存实体")
public class SysUserForm extends BaseForm<SysUser> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "name", value = "姓名||varchar(200)")
    @NotBlank(message = "name不能为空")
    @Length(min = 2, max = 20, message = "name长度在3到20个字符")
    private String name;

    @ApiModelProperty(name = "id_card", value = "身份证号||varchar(20)")
    @Length(min = 2, max = 20, message = "id_card长度在3到20个字符")
    private String idCard;

    @ApiModelProperty(name = "phone", value = "手机号||varchar(20)")
    @Length(max = 20, message = "phone长度在3到20个字符")
    private String phone;

    @ApiModelProperty(name = "login_account", value = "登录账号||varchar(200)")
    @NotBlank(message = "login_account不能为空")
    @Length(min = 1, max = 20, message = "login_account长度在3到20个字符")
    private String loginAccount;

    @ApiModelProperty(name = "login_pass", value = "登录密码||varchar(200)")
    @Length(max = 20, message = "login_pass长度在0到20个字符")
    private String loginPass;

    @ApiModelProperty(name = "is_valid", value = "是否有效（0:否；1:是）||int(1)")
    private Integer isValid;

    @ApiModelProperty(name = "account_non_expired", value = "过期性  1没过期0过期||int(1)")
    private Integer accountNonExpired;

    @ApiModelProperty(name = "credentials_non_expired", value = "有效性  1有效0失效||int(1)")
    private Integer credentialsNonExpired;

    @ApiModelProperty(name = "account_non_locked", value = "锁定性 1未锁定0锁定||int(1)")
    private Integer accountNonLocked;

    @ApiModelProperty(name = "type", value = "0普通用户1村级管理员2集管管理员||tinyint(4)")
    private Integer type;

    @ApiModelProperty(name = "branch_id", value = "集管端用于记录各村用户的id||bigint(20)")
    private Long branchId;

    @ApiModelProperty(name = "village_id", value = "村id（管理员为0）||int(11)")
    private Integer villageId;

    @ApiModelProperty(name = "village_name", value = "村名||varchar(200)")
//    @Length(min = 1, max = 50, message = "village_name长度在1到50个字符")
    private String villageName;

    @ApiModelProperty(name = "remark", value = "备注||varchar(500)")
    @Length(max = 20, message = "remark长度在3到20个字符")
    private String remark;


}
