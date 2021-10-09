package com.icicles.wmms.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.icicles.wmms.entity.param.WsMeterReaderQueryParam;
import lombok.*;

/**
 * <p>
 * 水表读数对外查询条件
 * </p>
 *
 * @author 
 * @since 2020-06-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="WsMeterReaderQueryForm", description="水表读数对外查询条件")
public class WsMeterReaderQueryForm extends BaseQueryForm<WsMeterReaderQueryParam> {


    @ApiModelProperty(name = "user_id", value = "用户id||bigint(20)")
    private String userId;
    @ApiModelProperty(name = "user_sn", value = "用户标识")
    private String userSn;

    @ApiModelProperty(name = "user_name", value = "用户名||varchar(50)")
    private String userName;

    @ApiModelProperty(name = "meter_id", value = "设备id||bigint(20)")
    private String meterId;

    @ApiModelProperty(name = "meter_sn", value = "设备标识||varchar(80)")
    private String meterSn;

    @ApiModelProperty(name = "village_id", value = "村id||bigint(20)")
    private String villageId;

    @ApiModelProperty(name = "village_name", value = "村名||varchar(50)")
    private String villageName;

    @ApiModelProperty(name = "group_id", value = "表井id")
    private String groupId;
    @ApiModelProperty(name = "groupName", value = "表井名称")
    private String groupName;

    @ApiModelProperty(value = "查询开始时间")
    private String createdTimeStart;

    @ApiModelProperty(value = "查询结束时间")
    private String createdTimeEnd;


}
