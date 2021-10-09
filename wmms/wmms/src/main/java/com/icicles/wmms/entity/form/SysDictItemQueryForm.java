package com.icicles.wmms.entity.form;

import com.icicles.wmms.entity.param.SysDictItemQueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 字典项对外查询条件
 * </p>
 *
 * @author auto
 * @since 2020-05-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="SysDictItemQueryForm", description="字典项对外查询条件")
public class SysDictItemQueryForm extends BaseQueryForm<SysDictItemQueryParam> {


    @ApiModelProperty(name = "dict_id", value = "字典编号||bigint(20) unsigned")
    private String dictId;

    @ApiModelProperty(name = "dict_code", value = "字典代码||varchar(200)")
    private String dictCode;

    @ApiModelProperty(name = "value", value = "值||varchar(200)")
    private String value;

    @ApiModelProperty(name = "label", value = "标签项||varchar(200)")
    private String label;

    @ApiModelProperty(name = "serial", value = "顺序||int(11)")
    private String serial;

    @ApiModelProperty(name = "remark", value = "备注||varchar(500)")
    private String remark;
    @ApiModelProperty(value = "查询开始时间")
    private String createdTimeStart;

    @ApiModelProperty(value = "查询结束时间")
    private String createdTimeEnd;


}
