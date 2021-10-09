package com.icicles.wmms.entity.form;

import com.icicles.wmms.entity.po.SysDictItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 字典项对外查询保存实体
 * </p>
 *
 * @author auto
 * @since 2020-05-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="SysDictItemForm", description="字典项对外查询保存实体")
public class SysDictItemForm extends BaseForm<SysDictItem> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "dict_id", value = "字典编号||bigint(20) unsigned")
    private Long dictId;

    @ApiModelProperty(name = "dict_code", value = "字典代码||varchar(200)")
    @NotBlank(message = "dict_code不能为空")
    @Length(min = 2, max = 20, message = "dict_code长度在3到20个字符")
    private String dictCode;

    @ApiModelProperty(name = "value", value = "值||varchar(200)")
    @NotBlank(message = "value不能为空")
    @Length(min = 1, max = 20, message = "value长度在1到20个字符")
    private String value;

    @ApiModelProperty(name = "label", value = "标签项||varchar(200)")
    @NotBlank(message = "label不能为空")
    @Length(min = 1, max = 20, message = "label长度在1到20个字符")
    private String label;

    @ApiModelProperty(name = "serial", value = "顺序||int(11)")
    private Integer serial;

    @ApiModelProperty(name = "remark", value = "备注||varchar(500)")
    @Length(min = 0, max = 50, message = "remark长度在0到50个字符")
    private String remark;


}
