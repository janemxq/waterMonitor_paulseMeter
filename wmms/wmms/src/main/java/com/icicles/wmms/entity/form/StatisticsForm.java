package com.icicles.wmms.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * 统计查询
 * @author lisy
 */
@Data
@ApiModel(value="StatisticsForm", description="统计查询")
public class StatisticsForm {
    @ApiModelProperty(name = "year",value = "查询的年份，非必须，不为空时格式必须如：2020。默认当前年份")
    @Pattern(regexp = "\\d{4}$", message = "年的格式必须如2020这样的形式")
    private String year;
    @ApiModelProperty(name = "userSn",value = "用户标识，非必需")
    private String userSn;
    @ApiModelProperty(name = "villageSn",value = "村的标识，非必需.不填写默认本村")
    private String villageSn;
    @ApiModelProperty(name = "meterSn",value = "设备标识，非必需")
    private String meterSn;
}
