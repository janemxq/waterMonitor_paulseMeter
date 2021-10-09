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
@ApiModel(value="一定时间内没有缴费的用户查询表单", description="一定时间内没有缴费的用户查询表单")
public class NopayUsersForm extends BaseQueryForm{
    @ApiModelProperty(name = "userSn",value = "用户标识,非必需。上传用户标识即代表查询某个用户的信息，不上传代表查询整个系统")
    private String userSn;
    @ApiModelProperty(name = "villageSn",value = "村的标识,非必需。上传标识即代表查询某个村的信息，不上传代表查询当前登录人所在的村的信息（超级管理员查询所有信息）")
    private String villageSn;
    @ApiModelProperty(name = "用水量",value = "用水量，非必需。该值为40时，即查询用没有缴费的那部分用水量大于40立方米的用户记录。默认使用后台配置的值")
    private String volume;
    @ApiModelProperty(name = "多久没有缴费了（天）",value = "多久没有缴费了（天），非必需。该值为31时，即查询用没有缴费天数超过31的用户记录。默认使用后台配置的值")
    private Integer howLong;
}
