package com.icicles.wmms.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.icicles.wmms.entity.param.WsCrontabLogQueryParam;
import io.swagger.models.auth.In;
import lombok.*;

/**
 * <p>
 * 定时任务执行日志对外查询条件
 * </p>
 *
 * @author 
 * @since 2020-09-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="WsCrontabLogQueryForm", description="定时任务执行日志对外查询条件")
public class WsCrontabLogQueryForm extends BaseQueryForm<WsCrontabLogQueryParam> {

//
//    @ApiModelProperty(name = "type", value = "日志类型：1自动抄表||tinyint(4)")
//    private Integer type;

//    @ApiModelProperty(name = "run_date", value = "执行时间，格式为YYYY-mm-dd||varchar(80)")
//    private String runDate;
//
//    @ApiModelProperty(name = "remark", value = "备注||varchar(500)")
//    private String remark;
//    @ApiModelProperty(value = "查询开始时间")
//    private String createdTimeStart;

    @ApiModelProperty(value = "查询结束时间")
    private String createdTimeEnd;


}
