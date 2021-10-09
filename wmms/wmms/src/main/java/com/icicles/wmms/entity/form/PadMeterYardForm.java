package com.icicles.wmms.entity.form;

import com.icicles.wmms.entity.po.PadMeterYard;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

/**
 * <p>
 * pad端表井对外查询保存实体
 * </p>
 *
 * @author 
 * @since 2020-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="PadMeterYardForm", description="pad端表井对外查询保存实体")
public class PadMeterYardForm extends BaseForm<PadMeterYard> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "name", value = "pad端表井名称||varchar(500)")
    @NotBlank(message = "表井名称不能为空")
    @Length(min = 2, max = 200, message = "表井名称长度在2到200个字符")
    private String name;

    @ApiModelProperty(name = "address", value = "pad端表井地址||varchar(32)")
    @NotBlank(message = "表井地址不能为空")
    @Length(min = 2, max = 200, message = "表井表井地址长度在2到200个字符")
    private String address;

    @ApiModelProperty(name = "remark", value = "备注||varchar(500)")
    @Length(max = 500, message = "备注最大长度为500个字符")
    private String remark;
}
