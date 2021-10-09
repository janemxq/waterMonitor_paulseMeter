package com.icicles.wmms.entity.form;

import com.icicles.wmms.entity.po.WsCharge;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * <p>
 * 用户充值记录对外查询保存实体
 * </p>
 *
 * @author 
 * @since 2020-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="WsChargeForm", description="用户充值记录对外查询保存实体")
public class WsChargeForm extends BaseForm<WsCharge> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "user_sn", value = "用户标识||varchar(32)")
    @NotBlank(message = "user_sn不能为空")
    @Length(min = 2, max = 20, message = "user_sn长度在3到20个字符")
    private String userSn;

    @ApiModelProperty(name = "user_name", value = "用户名称||varchar(80)")
    @NotBlank(message = "user_name不能为空")
    @Length(min = 2, max = 20, message = "user_name长度在3到20个字符")
    private String userName;

    @ApiModelProperty(name = "phone", value = "手机号||varchar(20)")
    @NotBlank(message = "phone不能为空")
    @Length(min = 2, max = 20, message = "phone长度在3到20个字符")
    private String phone;

    @ApiModelProperty(name = "account_num", value = "收款金额||decimal(15,2)")
    @NotBlank(message = "account_num不能为空")
    @Length(min = 2, max = 20, message = "account_num长度在3到20个字符")
    private BigDecimal accountNum;

    @ApiModelProperty(name = "balance", value = "余额||decimal(14,2)")
    @NotBlank(message = "balance不能为空")
    @Length(min = 2, max = 20, message = "balance长度在3到20个字符")
    private BigDecimal balance;


}
