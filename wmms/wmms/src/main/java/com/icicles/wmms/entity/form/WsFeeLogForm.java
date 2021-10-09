package com.icicles.wmms.entity.form;

import com.icicles.wmms.entity.po.WsFeeLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <p>
 * 缴费记录对外查询保存实体
 * </p>
 *
 * @author 
 * @since 2020-06-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="WsFeeLogForm", description="缴费记录对外查询保存实体")
public class WsFeeLogForm extends BaseForm<WsFeeLog> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "meter_id", value = "设备id||必须||bigint(20)")
    @NotNull(message = "设备id不能为空")
    @Min(value = 1,message = "设备id不正确")
    private Long meterId;

    @ApiModelProperty(name = "payment", value = "支付方式")
    @NotBlank(message = "支付方式不能为空")
    private String payment;

    @ApiModelProperty(name = "realPay", value = "收款金额")
    @Min(value = 0,message = "收款金额不能小于0")
    private BigDecimal realPay;
}
