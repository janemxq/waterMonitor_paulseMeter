package com.icicles.wmms.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * 普通统计查询
 * @author lisy
 */
@Data
@ApiModel(value="StatisticsCommonForm", description="普通统计查询")
public class StatisticsCommonForm {
    @ApiModelProperty(name = "userSn",value = "用户标识，非必需")
    private String userSn;
    @ApiModelProperty(name = "startTime",value = "查询开始时间，非必需。如果传值，格式必须为2020-08-15")
    @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}", message = "格式必须为:2020-08-15")
    private String startTime;
    @ApiModelProperty(name = "endTime",value = "查询结束时间，非必需。如果传值，格式必须为2020-08-15")
    @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}", message = "格式必须为:2020-08-15")
    private String endTime;
}
