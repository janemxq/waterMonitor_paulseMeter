package com.icicles.wmms.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * <p>
 * 导出缴费记录excel
 * </p>
 *
 * @author 
 * @since 2020-06-29
 */
@Data
@ApiModel(value="WsFeeLogExcelForm", description="导出缴费记录excel")
public class WsFeeLogExcelForm{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "meterSn", value = "设备标识")
    private String meterSn;
    @ApiModelProperty(name = "userSn", value = "用户标识")
    private String userSn;

    @ApiModelProperty(name = "createdTimeEnd", value = "查询结束时间")
    private String createdTimeEnd;
    @ApiModelProperty(name = "createdTimeStart", value = "查询开始时间")
    private String createdTimeStart;
}
