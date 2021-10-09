package com.icicles.wmms.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.icicles.wmms.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2020-06-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_menu")
@ApiModel(value="SysMenu", description="菜单类")
public class SysMenu extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * 链接
     */
    @ApiModelProperty(name = "href", value = "链接")
    private String href;

    /**
     * 图标
     */
    @ApiModelProperty(name = "icon", value = "图标")
    private String icon;

    /**
     * 标签
     */
    @ApiModelProperty(name = "label", value = "标签")
    private String label;

    /**
     * 名称
     */
    @ApiModelProperty(name = "name", value = "名称")
    private String name;

    /**
     * 菜单编号
     */
    @ApiModelProperty(name = "no", value = "菜单编号")
    private String no;

    /**
     * 直接上级
     */
    @ApiModelProperty(name = "parentId", value = "直接上级")
    private String parentId;

    /**
     * 所有上级
     */
    @ApiModelProperty(name = "parentIds", value = "所有上级")
    private String parentIds;

    /**
     * 是否有效（0:否；1:是）
     */
    @ApiModelProperty(name = "isValid", value = "是否有效（0:否；1:是）")
    private Integer isValid;

    /**
     * 备注
     */
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

    /**
     * 排序数字，越小越靠前
     */
    @ApiModelProperty(name = "serial", value = "排序数字，越小越靠前")
    private Integer serial;

    /**
     * 子菜单
     */
    @TableField(exist = false)
    @ApiModelProperty(name = "childMenu", value = "子菜单，有的菜单可能没有这个值")
    private List<SysMenu> childMenu;

    /**
     * 是否被选中
     */
    @TableField(exist = false)
    @ApiModelProperty(name = "checkedFlag", value = "是否被选中，1被选中，0不被选中。只有在查看管理角色菜单时才生效")
    private Integer checkedFlag;

    /**
     * 是否被选中
     */
    @ApiModelProperty(name = "filePath", value = "页面文件路径")
    private String filePath;
}
