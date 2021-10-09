package com.icicles.wmms.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 是否在使用默认密码
 * @author lisy
 */
@Data
@ApiModel(value="是否在使用默认密码的结果", description="是否在使用默认密码的结果类")
public class UseDefaultPwdResultDTO {
    /**
     * 登录账号
     */
    @ApiModelProperty(name = "loginAccount", value = "登录账号")
    private String loginAccount;
    /**
     * 是否在使用默认密码，1在使用0未使用
     */
    @ApiModelProperty(name = "isUserDefaultPwd", value = "是否在使用默认密码，1在使用0未使用")
    private Integer isUserDefaultPwd;
}
