package com.icicles.wmms.entity.form;

import com.icicles.wmms.entity.po.SysParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 系统参数
 * </p>
 *
 * @author 
 * @since 2020-04-27
 */
@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel(value="SysParamForm", description="系统参数")
public class SysParamForm extends BaseForm<SysParam> {

    @ApiModelProperty(name = "name", value = "名称||varchar(200)")
    @NotBlank(message = "参数名称不能为空")
    @Length(min = 1, max = 20, message = "参数名称长度在1到20个字符")
    private String name;

    @ApiModelProperty(name = "config_key", value = "配置键||varchar(200)")
    @NotBlank(message = "配置键不能为空")
    @Length(min = 1, max = 200, message = "配置键长度在1到200个字符")
    private String configKey;

    @ApiModelProperty(name = "config_value", value = "配置值||varchar(200)")
    @NotBlank(message = "配置值不能为空")
    @Length(min = 1, max = 200, message = "配置值长度在1到200个字符.")
    private String configValue;

    @ApiModelProperty(name = "is_valid", value = "是否有效（0:否；1:是）||int(1)")
    private Integer isValid = 1;

    @ApiModelProperty(name = "serial", value = "顺序||int(5)")
    private Integer serial;

    @ApiModelProperty(name = "remark", value = "备注")
    @Length(min = 0, max = 255, message = "remark长度在0到255个字符")

    private String remark;

}
