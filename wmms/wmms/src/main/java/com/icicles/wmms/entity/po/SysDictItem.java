package com.icicles.wmms.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 字典项
 * </p>
 *
 * @author auto
 * @since 2020-05-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_sys_dict_item")
public class SysDictItem extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * 字典编号
     */
    private Long dictId;

    /**
     * 字典代码
     */
    private String dictCode;

    /**
     * 值
     */
    private String value;

    /**
     * 标签项
     */
    private String label;

    /**
     * 顺序
     */
    private Integer serial;

    /**
     * 备注
     */
    private String remark;


}
