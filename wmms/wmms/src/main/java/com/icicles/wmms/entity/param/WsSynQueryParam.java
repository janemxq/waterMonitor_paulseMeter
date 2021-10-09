package com.icicles.wmms.entity.param;

import com.icicles.wmms.entity.po.WsSyn;
import lombok.*;

/**
 * <p>
 * 同步记录对内查询条件
 * </p>
 *
 * @author 
 * @since 2020-07-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WsSynQueryParam extends BaseParam<WsSyn> {


    private String dataType;


}
