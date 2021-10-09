package com.icicles.wmms.entity.dto;

import com.icicles.wmms.entity.po.WsMeter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lisy
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="WsMeterDTO", description="水表信息拓展类")
public class WsMeterDTO extends WsMeter {
    /**
     * 用水类型名称
     */
    @ApiModelProperty(name = "meter_type_name", value = "用水类型名称（显示时直接使用）")
    private String meterTypeName="";
}
