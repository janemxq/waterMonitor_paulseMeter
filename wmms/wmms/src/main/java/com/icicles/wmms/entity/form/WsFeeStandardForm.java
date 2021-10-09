package com.icicles.wmms.entity.form;

import com.icicles.wmms.entity.po.WsFeeStandard;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 水表的收费标准对外查询保存实体
 * </p>
 *
 * @author 
 * @since 2020-06-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="WsFeeStandardForm", description="水表的收费标准对外查询保存实体")
public class WsFeeStandardForm extends BaseForm<WsFeeStandard> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "display_name", value = "收费名称||必须||varchar(200)")
    @NotBlank(message = "收费标准名称不能为空")
    @Length(min = 2, max = 200, message = "收费标准长度应该在2-200之间")
    private String displayName;

    @ApiModelProperty(name = "sort_id", value = "用于排序的字段||非必须，默认99||int(11)")
    private Integer sortId=99;

    @ApiModelProperty(name = "fee_standard", value = "收费标准||必须，格式为“0-50,5&50-100,10”||varchar(500)")
    @NotBlank(message = "收费标准不能为空")
    @Length(min = 2, max = 500, message = "收费标准的长度在2-500之间")
    private String feeStandard;

    @ApiModelProperty(name = "is_step", value = "是否按照阶梯计价，1是0不是，默认1||非必须")
    @Min(value = 0,message = "阶梯计价格式不正确")
    private Integer isStep;

}
