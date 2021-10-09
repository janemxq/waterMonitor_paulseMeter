package com.icicles.wmms.entity.po;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 缴费记录
 * </p>
 *
 * @author 
 * @since 2020-06-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("ws_fee_log")
@ApiModel(value="WsFeeLog", description="缴费记录实体")
public class WsFeeLog extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @ApiModelProperty(name = "userId", value = "用户id")
    private Long userId;

    /**
     * 设备id
     */
    @ApiModelProperty(name = "meterId", value = "设备id")
    private Long meterId;

    /**
     * 是否缴费：0未缴费1已缴费-1已取消
     */
    @ApiModelProperty(name = "payStatus", value = "是否缴费：0未缴费1已缴费-1已取消")
    private Integer payStatus;

    /**
     * 上期水表示数
     */
    @ApiModelProperty(name = "startNum", value = "上期水表示数")
    private BigDecimal startNum;

    /**
     * 本期水表示数
     */
    @ApiModelProperty(name = "endNum", value = "本期水表示数")
    private BigDecimal endNum;

    /**
     * 本期账单开始时间
     */
    @ApiModelProperty(name = "feeStartTime", value = "本期账单开始时间")
    private String feeStartTime;

    /**
     * 本期账单结束时间
     */
    @ApiModelProperty(name = "feeEndTime", value = "本期账单结束时间")
    private String feeEndTime;

    /**
     * 缴费基准
     */
    @JsonIgnore
    @ApiModelProperty(name = "fee_standard", value = "缴费基准||前端不需要显示")
    private String feeStandard;

    /**
     * 用水量
     */
    @ApiModelProperty(name = "volume", value = "用水量")
    private BigDecimal volume;

    /**
     * 缴费金额
     */
    @ApiModelProperty(name = "account", value = "缴费金额")
    private BigDecimal account;
    /**
     * 缴费时间
     */
    @ApiModelProperty(name = "payTime", value = "缴费时间")
    private String payTime;

    /**
     * 用户名
     */
    @ApiModelProperty(name = "userName", value = "用户名")
    private String userName;

    /**
     * 用户手机号
     */
    @ApiModelProperty(name = "phone", value = "用户手机号")
    private String phone;

    /**
     * 设备编码
     */
    @ApiModelProperty(name = "meterSn", value = "设备编码")
    private String meterSn;

    /**
     * 累计用水量
     */
    @ApiModelProperty(name = "waterSum", value = "累计用水量")
    private BigDecimal waterSum;

    /**
     * 用户标识
     */
    @ApiModelProperty(name = "userSn", value = "用户标识")
    private String userSn;

    /**
     * 支付方式
     */
    @ApiModelProperty(name = "payment", value = "支付方式")
    private String payment;

    /**
     *  村标识
     */
    @ApiModelProperty(name = "villageSn", value = "村标识")
    private String villageSn;

    /**
     *  收款金额
     */
    @ApiModelProperty(name = "realPay", value = "收款金额")
    @Builder.Default
    private BigDecimal realPay = BigDecimal.ZERO;

    /**
     *  使用的余额的数量 balance+realPay>=account
     */
    @ApiModelProperty(name = "balance", value = "使用的余额的数量")
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;

    /**
     *  缴费时总的脉冲值
     */
    @ApiModelProperty(name = "pulseSum", value = "缴费时总的脉冲值")
    private BigDecimal pulseSum;

    /**
     * 表井名称
     */
    @ApiModelProperty(name = "groupName", value = "表井名称")
    @TableField(exist = false)
    private String groupName;
}
