package com.icicles.wmms.entity.param;

import com.icicles.wmms.entity.po.SysDictItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 字典项对内查询条件
 * </p>
 *
 * @author auto
 * @since 2020-05-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDictItemQueryParam extends BaseParam<SysDictItem> {


    private String dictId;

    private String dictCode;

    private String value;

    private String label;

    private String serial;

    private String remark;


}
