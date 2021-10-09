package com.icicles.wmms.entity.form;

import com.icicles.wmms.entity.po.PadMeterInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * <p>
 * pad端水表信息对外查询保存实体
 * </p>
 *
 * @author 
 * @since 2020-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="PadMeterInfoForm", description="pad端水表信息对外查询保存实体")
public class PadMeterInfoForm extends BaseForm<PadMeterInfo> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "sn", value = "水表标识||varchar(80)")
    @NotBlank(message = "水表标识不能为空")
    @Length(min = 2, max = 80, message = "表井标识长度在2到80个字符")
    private String sn;

    @ApiModelProperty(name = "pulse_sum", value = "总脉冲||int(10) unsigned")
    @Min(value = 0, message = "总脉冲最小值为0")
    private Integer pulseSum;

    @ApiModelProperty(name = "water_sum", value = "总的用水量||int(10) unsigned")
    @Min(value = 0,message = "总的用水量最小值为0")
    private Integer waterSum;

    @ApiModelProperty(name = "meter_num", value = "码盘值||int(11)")
    @Min(value = 0,message = "码盘值最小值为0")
    private Integer meterNum;
}
