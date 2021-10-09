package com.icicles.wmms.entity.form;

import com.icicles.wmms.entity.po.SysMenu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 对外查询保存实体
 * </p>
 *
 * @author 
 * @since 2020-06-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="SysMenuForm", description="对外查询保存实体")
public class SysMenuForm extends BaseForm<SysMenu> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "href",required = true,value = "链接||varchar(200)")
    @NotBlank(message = "菜单连接不能为空")
    @Length(min = 1, max = 200, message = "菜单连接长度在1到200个字符")
    private String href;

    @ApiModelProperty(name = "icon",value = "图标||varchar(200)")
    @Length(min = 2, max = 200, message = "图标长度在2到200个字符")
    private String icon;

    @ApiModelProperty(name = "label", value = "标签||varchar(200)")
    @NotBlank(message = "标签不能为空")
    @Length(min = 2, max = 20, message = "标签长度在2到200个字符")
    private String label;

    @ApiModelProperty(name = "name", value = "名称||varchar(200)")
    @NotBlank(message = "name不能为空")
    @Length(min = 2, max = 200, message = "name长度在2到20个字符")
    private String name;

    @ApiModelProperty(name = "no", value = "菜单编号||varchar(50)")
    @NotBlank(message = "菜单编号不能为空")
    @Length(min = 2, max = 50, message = "菜单编号长度在2到50个字符")
    private String no;

    @ApiModelProperty(name = "parent_id", value = "直接上级||没有上级时传-1，varchar(32)")
    @Length(min = 1, max = 20, message = "直接上级长度在1到20个字符")
    private String parentId;

    @ApiModelProperty(name = "is_valid",required = true,value = "是否有效（0:否；1:是）||int(1)")
    @NotNull(message = "是否有效标识取值为0或者1!")
    @Range(min = 0, max = 1, message = "是否有效标识取值为0或者1")
    private Integer isValid;

    @ApiModelProperty(name = "remark", value = "备注||varchar(500)")
    @Length(min = 1, max = 500, message = "remark长度在1到500个字符")
    private String remark;

    @ApiModelProperty(name = "serial", value = "排序数字，越小越靠前||varchar(500)")
    private Integer serial;

    @ApiModelProperty(name = "filePath", required = true, value = "页面文件路径||varchar(500)")
    @NotBlank(message = "页面文件路径不能为空")
    @Length(min = 2, max = 500, message = "页面文件路径字符应该在2到500之间")
    private String filePath;
}
