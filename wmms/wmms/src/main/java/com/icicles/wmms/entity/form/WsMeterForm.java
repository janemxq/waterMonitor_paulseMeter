package com.icicles.wmms.entity.form;

import com.icicles.wmms.entity.po.WsMeter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * <p>
 * 水表设备对外查询保存实体
 * </p>
 *
 * @author 
 * @since 2020-06-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="WsMeterForm", description="水表设备对外查询保存实体")
public class WsMeterForm extends BaseForm<WsMeter> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "sn",required = true,value = "编号||必须||varchar(80)")
    @NotBlank(message = "sn不能为空")
    @Length(min = 3, max = 80, message = "sn长度在3到80个字符")
    private String sn;

    @ApiModelProperty(name = "pulseInit", value = "初始码盘值||非必须，默认0||decimal(0,0)")
    @DecimalMin(value = "0", message = "初始码盘值应该大于等于0")
    private Integer pulseInit;

    @ApiModelProperty(name = "pulse", value = "脉冲（多少脉冲对应 1 方水）||varchar(50)||非必须")
    @Length(max = 50, message = "脉冲最大长度为50个字符")
    private String pulse;

    @ApiModelProperty(name = "mac_sn",value = "唯一标识||varchar(1)||非必须")
    @Length(min = 1, max = 1, message = "mac_sn长度在为1位16进制字符")
    private String macSn;

    @ApiModelProperty(name = "activationTime",value = "设备启用时间||格式必须为2020-05-01||非必须")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "设备启用时间格式不正确")
    private String activationTime;

    @ApiModelProperty(name = "village_sn",value = "村的标识")
    private String villageSn;
    @ApiModelProperty(name = "group_sn",value = "表井标识")
    @NotBlank(message = "表井标识不能为空，请选择设备对应的表井")
    private String groupSn;
    @ApiModelProperty(name = "user_sn",value = "用户标识")
    private String userSn;
    @ApiModelProperty(name = "meter_type_sn",value = "收费标准的标识")
    @NotBlank(message = "用水类型不能为空，请选择设备的用水类型")
    private String meterTypeSn;

    @ApiModelProperty(name = "remark",value = "备注||长度最大为200||非必须")
    @Length(max = 200, message = "备注长度最大为200")
    private String remark;
}
