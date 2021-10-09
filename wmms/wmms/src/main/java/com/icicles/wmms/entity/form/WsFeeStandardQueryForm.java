package com.icicles.wmms.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.icicles.wmms.entity.param.WsFeeStandardQueryParam;
import lombok.*;

/**
 * <p>
 * 水表的收费标准对外查询条件
 * </p>
 *
 * @author 
 * @since 2020-06-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="WsFeeStandardQueryForm", description="水表的收费标准对外查询条件")
public class WsFeeStandardQueryForm extends BaseQueryForm<WsFeeStandardQueryParam> {
    @ApiModelProperty(name = "display_name", value = "收费名称||varchar(200)")
    private String displayName;
}
