package com.icicles.wmms.entity.param;

import com.icicles.wmms.entity.po.WsCrontabLog;
import lombok.*;

/**
 * <p>
 * 定时任务执行日志对内查询条件
 * </p>
 *
 * @author 
 * @since 2020-09-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WsCrontabLogQueryParam extends BaseParam<WsCrontabLog> {


    private String type;

    private String runDate;

    private String remark;


}
