package com.icicles.wmms.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.icicles.wmms.entity.param.SysUserQueryParam;
import lombok.*;

/**
 * <p>
 * 用水监测管理平台用户信息表对外查询条件
 * </p>
 *
 * @author damon
 * @since 2020-06-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="SysUserQueryForm", description="用水监测管理平台用户信息表对外查询条件")
public class SysUserQueryForm extends BaseQueryForm<SysUserQueryParam> {


    @ApiModelProperty(name = "name", value = "姓名||varchar(200)")
    private String name;

    @ApiModelProperty(name = "id_card", value = "身份证号||varchar(20)")
    private String idCard;

    @ApiModelProperty(name = "phone", value = "手机号||varchar(20)")
    private String phone;

    @ApiModelProperty(name = "login_account", value = "登录账号||varchar(200)")
    private String loginAccount;

//    @ApiModelProperty(name = "login_pass", value = "登录密码||varchar(200)")
//    private String loginPass;
//
//    @ApiModelProperty(name = "is_valid", value = "是否有效（0:否；1:是）||int(1)")
//    private String isValid;
//
//    @ApiModelProperty(name = "account_non_expired", value = "过期性  1没过期0过期||int(1)")
//    private String accountNonExpired;
//
//    @ApiModelProperty(name = "credentials_non_expired", value = "有效性  1有效0失效||int(1)")
//    private String credentialsNonExpired;
//
//    @ApiModelProperty(name = "account_non_locked", value = "锁定性 1未锁定0锁定||int(1)")
//    private String accountNonLocked;

    @ApiModelProperty(name = "type", value = "0普通用户1村级管理员2集管管理员||tinyint(4)")
    private String type;
//
//    @ApiModelProperty(name = "branch_id", value = "集管端用于记录各村用户的id||bigint(20)")
//    private String branchId;

    @ApiModelProperty(name = "village_id", value = "村id（管理员为0）||int(11)")
    private String villageId;

    @ApiModelProperty(name = "village_name", value = "村名||varchar(200)")
    private String villageName;
    @ApiModelProperty(name = "villageSn", value = "村落标识，搜索时使用这个最好||varchar(200)")
    private String villageSn;

    @ApiModelProperty(name = "remark", value = "备注||varchar(500)")
    private String remark;
    @ApiModelProperty(value = "查询开始时间")
    private String createdTimeStart;

    @ApiModelProperty(value = "查询结束时间")
    private String createdTimeEnd;


}
