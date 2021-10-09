package com.icicles.wmms.entity.po;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.icicles.wmms.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户充值记录
 * </p>
 *
 * @author 
 * @since 2020-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("ws_charge")
@ApiModel(value="WsCharge", description="充值记录实体类")
public class WsCharge extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * 用户标识
     */
    @ApiModelProperty(name = "userSn", value = "用户标识")
    private String userSn;

    /**
     * 用户名称
     */
    @ApiModelProperty(name = "userName", value = "用户名称")
    private String userName;

    /**
     * 手机号
     */
    @ApiModelProperty(name = "phone", value = "手机号")
    private String phone;

    /**
     * 收款金额
     */
    @ApiModelProperty(name = "accountNum", value = "收款金额")
    private BigDecimal accountNum;

    /**
     * 余额
     */
    @ApiModelProperty(name = "balance", value = "余额")
    private BigDecimal balance;
    /**
     * 水表标识
     */
    @ApiModelProperty(name = "meterSn", value = "水表标识")
    private String meterSn;

    /**
     * 水费
     */
    @ApiModelProperty(name = "cost", value = "水费")
    private BigDecimal cost;

    /**
     * 村标识
     */
    @ApiModelProperty(name = "villageSn", value = "村标识")
    private String villageSn;

    /**
     * 支付方式
     */
    @ApiModelProperty(name = "payment", value = "支付方式")
    private String payment;

    /**
     * 村名称
     */
    @ApiModelProperty(name = "villageName", value = "村名称")
    @TableField(exist = false)
    private String villageName;
}
