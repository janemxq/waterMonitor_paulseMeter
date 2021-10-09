package com.icicles.wmms.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 字典
 * </p>
 *
 * @author auto
 * @since 2020-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_sys_dict")
public class SysDict extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * 字典代码
     */
    private String code;

    /**
     * 字典名称
     */
    private String name;

    /**
     * 是否有效（0:否；1:是）
     */
    private Integer isValid;

    /**
     * 顺序
     */
    private Integer serial;

    /**
     * 备注
     */
    private String remark;

    @ApiModelProperty(name = "sys_dict_item_list", value = "字典项列表")
    @TableField(exist = false)
    private List<SysDictItem> sysDictItemList;


}
