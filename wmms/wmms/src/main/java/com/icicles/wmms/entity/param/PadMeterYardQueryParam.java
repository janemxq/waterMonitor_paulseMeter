package com.icicles.wmms.entity.param;

import com.icicles.wmms.entity.po.PadMeterYard;
import lombok.*;

/**
 * <p>
 * pad端表井对内查询条件
 * </p>
 *
 * @author 
 * @since 2020-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PadMeterYardQueryParam extends BaseParam<PadMeterYard> {


    private String name;

    private String sn;

    private String remark;


}
