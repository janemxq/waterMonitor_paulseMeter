package com.icicles.wmms.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.icicles.wmms.entity.param.PadMeterYardQueryParam;
import lombok.*;

/**
 * <p>
 * pad端表井对外查询条件
 * </p>
 *
 * @author 
 * @since 2020-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="PadMeterYardQueryForm", description="pad端表井对外查询条件")
public class PadMeterYardQueryForm extends BaseQueryForm<PadMeterYardQueryParam> {


    @ApiModelProperty(name = "name", value = "pad端表井名称||varchar(500)")
    private String name;
}
