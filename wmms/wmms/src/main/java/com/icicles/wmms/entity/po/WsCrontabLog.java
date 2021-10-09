package com.icicles.wmms.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.icicles.wmms.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 定时任务执行日志
 * </p>
 *
 * @author 
 * @since 2020-09-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("ws_crontab_log")
@ApiModel(value="WsCrontabLog", description="自动任务执行日志")
public class WsCrontabLog extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * 日志类型
     */
    @ApiModelProperty(name = "type", value = "日志类型：1 自动抄表缴费")
    private Integer type;

    /**
     * 执行时间，格式为YYYY-mm-dd
     */
    @ApiModelProperty(name = "runDate", value = "执行时间")
    private String runDate;

    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;
}
