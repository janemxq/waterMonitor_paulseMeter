package com.icicles.wmms.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.icicles.wmms.entity.param.WsMeterQueryParam;
import lombok.*;

/**
 * <p>
 * 水表设备对外查询条件
 * </p>
 *
 * @author 
 * @since 2020-06-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="WsMeterQueryForm", description="水表设备对外查询条件")
public class WsMeterQueryForm extends BaseQueryForm<WsMeterQueryParam> {


    @ApiModelProperty(name = "sn", value = "编号||varchar(50)")
    private String sn;


    @ApiModelProperty(name = "user_name", value = "用户名||varchar(50)")
    private String userName;

    @ApiModelProperty(name = "village_id", value = "村id||int(11)")
    private String villageId;
//
    @ApiModelProperty(name = "group_id", value = "表井id||int(11)")
    private String groupId;

    @ApiModelProperty(name = "online", value = "是否在线||tinyint(4)")
    private String online;
//
    @ApiModelProperty(name = "mac_sn", value = "唯一标识||varchar(80)")
    private String macSn;

    @ApiModelProperty(value = "查询开始时间")
    private String createdTimeStart;

    @ApiModelProperty(value = "查询结束时间")
    private String createdTimeEnd;

    @ApiModelProperty(name = "village_sn", value = "村的唯一标识")
    private String villageSn;
    @ApiModelProperty(name = "group_sn", value = "表井的唯一标识")
    private String groupSn;
    @ApiModelProperty(name = "user_sn", value = "用户的唯一标识||varchar(32)")
    private String userSn;
    @ApiModelProperty(name = "meter_type_sn", value = "收费标准唯一标识||varchar(32)")
    private String meterTypeSn;

}
