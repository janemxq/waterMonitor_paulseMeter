package com.icicles.wmms.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用水监测管理平台用户信息表
 * </p>
 *
 * @author damon
 * @since 2020-06-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_sys_user")
@ApiModel(value="SysUser", description="用户实体类")
public class SysUser extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * 姓名
     */
    @ApiModelProperty(name = "name", value = "姓名")
    private String name;

    /**
     * 身份证号
     */
    @ApiModelProperty(name = "idCard", value = "身份证号")
    private String idCard;

    /**
     * 手机号
     */
    @ApiModelProperty(name = "phone", value = "手机号")
    private String phone;

    /**
     * 登录账号
     */
    private String loginAccount;

    /**
     * 登录密码
     */
    @ApiModelProperty(name = "loginPass", value = "登录密码")
    private String loginPass;

    /**
     * 是否有效（0:否；1:是）
     */
    private Integer isValid;

    /**
     * 过期性  1没过期0过期
     */
    private Integer accountNonExpired;

    /**
     * 有效性  1有效0失效
     */
    private Integer credentialsNonExpired;

    /**
     * 锁定性 1未锁定0锁定
     */
    private Integer accountNonLocked;

    /**
     * 0普通用户1村级管理员2集管管理员
     */
    @ApiModelProperty(name = "type", value = "0普通用户1村级管理员2集管管理员")
    private Integer type;

    /**
     * 0普通用户1村级管理员2集管管理员
     */
    @TableField(exist = false)
    private String typeName;

    /**
     * 集管端用于记录各村用户的id
     */
    private Long branchId;

    /**
     * 村id（管理员为0）
     */
    private Integer villageId;

    /**
     * 村名
     */
    private String villageName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 唯一编号
     */
    @ApiModelProperty(name = "sn", value = "唯一编号")
    private String sn;

    /**
     * 村的标识
     */
    @ApiModelProperty(name = "villageSn", value = "村的标识")
    private String villageSn;

    /**
     * 登录次数
     */
    @ApiModelProperty(name = "loginNumber", value = "登录次数")
    private Long loginNumber;
}
