package com.icicles.wmms.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
/**
 * 统计类实体
 * @author lisy
 */
@ApiModel(value="StatisticsDTO", description="统计类实体")
@Data
public class StatisticsDTO {
    /**
     * 用水量
     */
    @ApiModelProperty(name = "waterData", value = "用水量")
    private List<WaterStatisticesDTO> waterData;
    /**
     * 缴费
     */
    @ApiModelProperty(name = "feeLogData", value = "月缴费")
    private List<WaterStatisticesDTO> feeLogData;
}
