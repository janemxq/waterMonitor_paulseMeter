package com.icicles.wmms.entity.form;

import com.icicles.wmms.entity.po.SysDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 字典对外查询保存实体
 * </p>
 *
 * @author auto
 * @since 2020-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="SysDictForm", description="字典对外查询保存实体")
public class SysDictForm extends BaseForm<SysDict> {

    @ApiModelProperty(name = "code", value = "字典代码||varchar(200)")
    @NotBlank(message = "code不能为空")
    @Length(min = 3, max = 20, message = "code长度在3到20个字符")
    private String code;

    @ApiModelProperty(name = "name", value = "字典名称||varchar(200)")
    @NotBlank(message = "name不能为空")
    @Length(min = 1, max = 20, message = "name长度在1到20个字符")
    private String name;

    @ApiModelProperty(name = "is_valid", value = "是否有效（0:否；1:是）||int(1)")
    private Integer isValid;

    @ApiModelProperty(name = "serial", value = "顺序||int(10)")
    private Integer serial;

    @ApiModelProperty(name = "remark", value = "备注||varchar(500)")
    @Length(min = 0, max = 255, message = "remark长度在0到255个字符")
    private String remark;


}
