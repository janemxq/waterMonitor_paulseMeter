package com.icicles.wmms.entity.form;

import com.icicles.wmms.entity.po.WsGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 表井对外查询保存实体
 * </p>
 *
 * @author 
 * @since 2020-06-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="WsGroupForm", description="表井对外查询保存实体")
public class WsGroupForm extends BaseForm<WsGroup> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "group_name", value = "组名||varchar(50)")
    @NotBlank(message = "group_name不能为空")
    @Length(min = 2, max = 20, message = "group_name长度在3到20个字符")
    private String groupName;

    @ApiModelProperty(name = "village_name", value = "村名||varchar(255)")
    @NotBlank(message = "村名称不能为空")
    @Length(min = 2, max = 20, message = "village_name长度在3到20个字符")
    private String villageName;

    @ApiModelProperty(name = "village_sn", value = "村庄编号||varchar(255)")
    private String villageSn;

    @ApiModelProperty(name = "sn", value = "表井编号|必须|最大长度80")
    @NotBlank(message = "表井编号不能为空")
    @Length(min = 2, max = 80, message = "表井编号长度应该在2-80之间")
    private String sn;

    @ApiModelProperty(name = "address", value = "表井地址|非必须")
    private String address;
}
