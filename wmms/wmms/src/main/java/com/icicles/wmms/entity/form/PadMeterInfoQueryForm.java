package com.icicles.wmms.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.icicles.wmms.entity.param.PadMeterInfoQueryParam;
import lombok.*;

/**
 * <p>
 * pad端水表信息对外查询条件
 * </p>
 *
 * @author 
 * @since 2020-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="PadMeterInfoQueryForm", description="pad端水表信息对外查询条件")
public class PadMeterInfoQueryForm extends BaseQueryForm<PadMeterInfoQueryParam> {


    @ApiModelProperty(name = "meter_yard_sn", value = "表井标识（pad_meter_yard表sn）||varchar(255)")
    private String meterYardSn;

    @ApiModelProperty(name = "meter_yard_id", value = "表井id（pad_meter_yard表id）||bigint(20)")
    private String meterYardId;

    @ApiModelProperty(name = "meter_mac", value = "水表通道号||varchar(20)")
    private String meterMac;

    @ApiModelProperty(name = "remark", value = "备注||varchar(500)")
    private String remark;

    @ApiModelProperty(name = "pulse_sum", value = "总脉冲||varchar(80)")
    private String pulseSum;

    @ApiModelProperty(name = "water_sum", value = "总的用水量||int(10) unsigned")
    private String waterSum;

    @ApiModelProperty(name = "meter_num", value = "码盘值||int(11)")
    private String meterNum;
    @ApiModelProperty(value = "查询开始时间")
    private String createdTimeStart;

    @ApiModelProperty(value = "查询结束时间")
    private String createdTimeEnd;


}
