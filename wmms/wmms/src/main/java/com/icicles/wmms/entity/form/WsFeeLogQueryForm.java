package com.icicles.wmms.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.icicles.wmms.entity.param.WsFeeLogQueryParam;
import lombok.*;

/**
 * <p>
 * 缴费记录对外查询条件
 * </p>
 *
 * @author 
 * @since 2020-06-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="WsFeeLogQueryForm", description="缴费记录对外查询条件")
public class WsFeeLogQueryForm extends BaseQueryForm<WsFeeLogQueryParam> {


    @ApiModelProperty(name = "userName",value = "用户名||非必须")
    private String userName;

    @ApiModelProperty(name = "userId",value = "用户id||非必须")
    private String userId;

    @ApiModelProperty(name = "userSn",value = "用户标识")
    private String userSn;

    @ApiModelProperty(name = "meterSn", value = "设备编号||非必须")
    private String meterSn;

    @ApiModelProperty(value = "查询开始时间||非必须,格式为YYYY-mm-dd HH:ii:ss")
    private String createdTimeStart;

    @ApiModelProperty(value = "查询结束时间||非必须,格式为YYYY-mm-dd HH:ii:ss")
    private String createdTimeEnd;

}
