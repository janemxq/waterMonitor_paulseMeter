package com.icicles.wmms.entity.param;

import com.icicles.wmms.entity.po.WsFeeStandard;
import lombok.*;

/**
 * <p>
 * 水表的收费标准对内查询条件
 * </p>
 *
 * @author 
 * @since 2020-06-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WsFeeStandardQueryParam extends BaseParam<WsFeeStandard> {


    private String displayName;

    private String sortId;

    private String feeStandard;


}
