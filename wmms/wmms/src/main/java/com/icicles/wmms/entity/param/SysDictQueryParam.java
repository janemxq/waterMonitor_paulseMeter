package com.icicles.wmms.entity.param;

import com.icicles.wmms.entity.po.SysDict;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 字典对内查询条件
 * </p>
 *
 * @author auto
 * @since 2020-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDictQueryParam extends BaseParam<SysDict> {

    private String code;

    private String name;

    private Integer isValid;

    private Integer serial;

    private String remark;


}
