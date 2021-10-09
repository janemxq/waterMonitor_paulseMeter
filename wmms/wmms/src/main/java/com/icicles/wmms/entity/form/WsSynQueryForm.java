package com.icicles.wmms.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.icicles.wmms.entity.param.WsSynQueryParam;
import lombok.*;

/**
 * <p>
 * 同步记录对外查询条件
 * </p>
 *
 * @author 
 * @since 2020-07-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="WsSynQueryForm", description="同步记录对外查询条件")
public class WsSynQueryForm extends BaseQueryForm<WsSynQueryParam> {


    @ApiModelProperty(name = "data_type", value = "数据类型（class name）||varchar(200)")
    private String dataType;
    @ApiModelProperty(value = "查询开始时间")
    private String createdTimeStart;

    @ApiModelProperty(value = "查询结束时间")
    private String createdTimeEnd;


}
