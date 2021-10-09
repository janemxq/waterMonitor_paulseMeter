package com.icicles.wmms.entity.param;

import com.icicles.wmms.entity.form.BaseQueryForm;
import com.icicles.wmms.entity.po.SysMenu;
import lombok.*;

/**
 * <p>
 * 对内查询条件
 * </p>
 *
 * @author 
 * @since 2020-06-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenuQueryParam extends BaseParam<SysMenu> {

    private String label;

    private String name;

    private String parentId;

    private String remark;

}
