package com.icicles.wmms.entity.form;

import com.icicles.wmms.entity.param.SysParamQueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统参数
 * </p>
 *
 * @author 
 * @since 2020-04-27
 */
@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel(value="SysParamQueryForm", description="系统参数")
public class SysParamQueryForm extends BaseQueryForm<SysParamQueryParam> {

    @ApiModelProperty(name = "name", value = "名称||varchar(200)")
    private String name;

    @ApiModelProperty(name = "config_key", value = "配置键||varchar(200)")
    private String configKey;

    @ApiModelProperty(name = "is_valid", value = "是否有效（0:否；1:是）")
    private String isValid;

    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    @ApiModelProperty(value = "查询开始时间")
    private String createdTimeStart;

    @ApiModelProperty(value = "查询结束时间")
    private String createdTimeEnd;

}
