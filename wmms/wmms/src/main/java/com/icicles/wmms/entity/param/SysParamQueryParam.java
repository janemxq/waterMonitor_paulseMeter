package com.icicles.wmms.entity.param;

import com.icicles.wmms.entity.po.SysParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统参数
 * </p>
 *
 * @author 
 * @since 2020-04-27
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class SysParamQueryParam extends BaseParam<SysParam> {

    private String name;

    private String configKey;

    private String configValue;

    private String isValid;

    private Integer serial;

    private String remark;
}
