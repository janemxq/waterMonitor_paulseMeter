package com.icicles.wmms.entity.form;

import com.icicles.wmms.entity.po.WsCrontabLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 定时任务执行日志对外查询保存实体
 * </p>
 *
 * @author 
 * @since 2020-09-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="WsCrontabLogForm", description="定时任务执行日志对外查询保存实体")
public class WsCrontabLogForm extends BaseForm<WsCrontabLog> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "type", value = "1自动抄表||tinyint(4)")
    @NotBlank(message = "type不能为空")
    @Length(min = 2, max = 20, message = "type长度在3到20个字符")
    private Integer type;

    @ApiModelProperty(name = "run_date", value = "执行时间，格式为YYYY-mm-dd||varchar(80)")
    @NotBlank(message = "run_date不能为空")
    @Length(min = 2, max = 20, message = "run_date长度在3到20个字符")
    private String runDate;

    @ApiModelProperty(name = "remark", value = "备注||varchar(500)")
    @NotBlank(message = "remark不能为空")
    @Length(min = 2, max = 20, message = "remark长度在3到20个字符")
    private String remark;


}
