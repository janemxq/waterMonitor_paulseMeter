package com.icicles.wmms.entity.form;

import com.icicles.wmms.entity.po.WsVillage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 村庄对外查询保存实体
 * </p>
 *
 * @author 
 * @since 2020-06-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="WsVillageForm", description="村庄对外查询保存实体")
public class WsVillageForm extends BaseForm<WsVillage> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "village_name",required = true, value = "名称||varchar(200)")
    @NotBlank(message = "village_name不能为空")
    @Length(min = 2, max = 20, message = "village_name长度在3到20个字符")
    private String villageName;

    @ApiModelProperty(name = "address",required = true, value = "地址||varchar(200)")
    @NotBlank(message = "address不能为空")
    @Length(min = 2, max = 20, message = "address长度在3到20个字符")
    private String address;

    @ApiModelProperty(name = "sort_id", value = "排序字段,越小越靠前||int(11)")
    private Integer sortId=99;

    @ApiModelProperty(name = "sn",required = true,value = "村落编号||必须6-32")
    @NotBlank(message = "编号不能为空")
    @Length(min = 6, max =32, message = "编号长度在6到32个字符")
    private String sn;
}
