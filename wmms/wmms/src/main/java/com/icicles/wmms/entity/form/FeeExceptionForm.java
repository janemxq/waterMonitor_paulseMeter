package com.icicles.wmms.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询缴费异常的表单类
 * @author lisy
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="FeeExceptionForm", description="查询缴费异常的表单类")
public class FeeExceptionForm extends BaseQueryForm{
    @ApiModelProperty(name = "userSn",required = true,value = "用户标识,非必需。上传用户标识即代表查询某个用户的信息，不上传代表查询整个系统")
    private String userSn;
    @ApiModelProperty(name = "month",required = true,value = "查询的月份，非必需，如果不为空格式必须为“2020-05”，默认当月")
    private String month;
    @ApiModelProperty(name = "villageSn",required = true,value = "村的标识，非必需，如果不为代表当前登录人所在村的所有记录（管理员查询所有记录）")
    private String villageSn;

//    @ApiModelProperty(name = "startTime",required = true,value = "查询开始时间，非必需，如果不为空格式必须为“2020-05-01”")
//    private String startTime;
//    @ApiModelProperty(name = "endTime",required = true,value = "查询结束时间，非必需，如果不为格式必须为“2020-05-01”")
//    private String endTime;
}
