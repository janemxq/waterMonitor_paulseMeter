package com.icicles.wmms.entity.form;

import com.icicles.wmms.entity.po.SysMenu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.icicles.wmms.entity.param.SysMenuQueryParam;
import lombok.*;

/**
 * <p>
 * 对外查询条件
 * </p>
 *
 * @author 
 * @since 2020-06-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="SysMenuQueryForm", description="对外查询条件")
public class SysMenuQueryForm extends BaseQueryForm<SysMenuQueryParam>{

    @ApiModelProperty(name = "name", value = "名称||varchar(200)")
    private String name;

    @ApiModelProperty(name = "parent_id", value = "直接上级||varchar(32)")
    private String parentId;

    @ApiModelProperty(name = "is_valid", value = "是否有效（0:否；1:是）||int(1)")
    private String isValid;

    @ApiModelProperty(name = "remark", value = "备注||varchar(500)")
    private String remark;

}
