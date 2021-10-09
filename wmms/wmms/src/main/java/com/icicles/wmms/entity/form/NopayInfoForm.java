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
@ApiModel(value="欠费的用户查询表单", description="欠费的用户查询表单")
public class NopayInfoForm{
    @ApiModelProperty(name = "userSn",value = "用户标识,非必需。上传用户标识即代表查询某个用户的信息，不上传代表查询整个系统")
    private String userSn;
    @ApiModelProperty(name = "villageSn",value = "村的标识,非必需。上传标识即代表查询某个村的信息，不上传代表查询当前登录人所在的村的信息（超级管理员查询所有信息）")
    private String villageSn;
}
