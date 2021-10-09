package com.icicles.wmms.entity.form;

import com.icicles.wmms.entity.po.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

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
@ApiModel(value="SysUserFormPwd", description="修改密码")
public class SysUserFormPwd extends BaseForm<SysUser> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "password", value = "密码，长度我6-16")
    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 16, message = "密码长度在6到16个字符")
    private String password;
}
