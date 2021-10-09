package com.icicles.wmms.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.icicles.wmms.entity.param.WsChargeQueryParam;
import lombok.*;

/**
 * <p>
 * 用户充值记录对外查询条件
 * </p>
 *
 * @author 
 * @since 2020-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="WsChargeQueryForm", description="用户充值记录对外查询条件")
public class WsChargeQueryForm extends BaseQueryForm<WsChargeQueryParam> {


    @ApiModelProperty(name = "user_sn", value = "用户标识||varchar(32)")
    private String userSn;

    @ApiModelProperty(name = "user_name", value = "用户名称||varchar(80)")
    private String userName;

    @ApiModelProperty(name = "phone", value = "手机号||varchar(20)")
    private String phone;

    @ApiModelProperty(name = "account_num", value = "收款金额||decimal(15,2)")
    private String accountNum;

    @ApiModelProperty(name = "balance", value = "余额||decimal(14,2)")
    private String balance;
    @ApiModelProperty(value = "查询开始时间")
    private String createdTimeStart;

    @ApiModelProperty(value = "查询结束时间")
    private String createdTimeEnd;


}
