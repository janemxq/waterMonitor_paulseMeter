package com.icicles.wmms.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lisy
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="导出用水量和缴费的统计（pdf）", description="导出用水量和缴费统计查询表单")
public class PdfFeeAndWaterForm {
    @ApiModelProperty(name = "year",value = "年份，非必须。不传参数代表查询所有年份。如果传参数，代表查询特定年份，格式必须如“2020”（YYYY）")
    private String year;
//    @ApiModelProperty(name = "villageSn",value = "村的标识,非必需。上传标识即代表查询某个村的信息，不上传代表查询当前登录人所在的村的信息（超级管理员查询所有信息）")
//    private String villageSn;
}
