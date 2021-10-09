package com.icicles.wmms.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.icicles.wmms.entity.po.WsMeter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
/**
 * @author lisy
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="MeterFeeDTO", description="水表实体类")
public class MeterFeeDTO extends WsMeter {
    /**
     * 水表待付水费
     */
    @ApiModelProperty(name = "to_pay_account", value = "待付水费")
    private BigDecimal toPayAccount=BigDecimal.ZERO;
    /**
     * 水表待付用水量
     */
    @ApiModelProperty(name = "to_pay_water_volume", value = "待付用水量")
    private BigDecimal toPayWaterVolume=BigDecimal.ZERO;

    /**
     * 上期水表示数
     */
    @ApiModelProperty(name = "pre_number", value = "上期水表示数")
    private BigDecimal preNumber=BigDecimal.ZERO;
    /**
     * 最新水表示数
     */
    @ApiModelProperty(name = "last_number", value = "最新水表示数")
    private BigDecimal lastNumber=BigDecimal.ZERO;
    /**
     * 收费类型
     */
    @ApiModelProperty(name = "meterTypeName", value = "收费类型")
    private String meterTypeName="";
    /**
     * 上次缴费时间
     */
    @ApiModelProperty(name = "prePayDay", value = "上次缴费时间")
    private String prePayDay="";
}
