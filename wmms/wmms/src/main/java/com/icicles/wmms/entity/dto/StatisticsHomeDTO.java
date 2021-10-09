package com.icicles.wmms.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 首页综合统计类实体
 * @author lisy
 */
@ApiModel(value="StatisticsHomeDTO", description="首页综合统计类实体")
@Data
public class StatisticsHomeDTO {
    /**
     * 系统人数
     */
    @ApiModelProperty(name = "userCount", value = "系统人数")
    private Integer userCount;
    /**
     * 设备数量
     */
    @ApiModelProperty(name = "meterCount", value = "设备数量")
    private Integer meterCount;

    /**
     * 抄表次数
     */
    @ApiModelProperty(name = "meterReaderCount", value = "抄表次数")
    private Integer meterReaderCount;

    /**
     * 支付次数
     */
    @ApiModelProperty(name = "paymentCount", value = "支付次数")
    private Integer paymentCount;

    /**
     * 支付总额
     */
    @ApiModelProperty(name = "paymentSum", value = "支付总额")
    private BigDecimal paymentSum;

    /**
     * 用水总数
     */
    @ApiModelProperty(name = "waterSum", value = "用水总数")
    private BigDecimal waterSum;
}
