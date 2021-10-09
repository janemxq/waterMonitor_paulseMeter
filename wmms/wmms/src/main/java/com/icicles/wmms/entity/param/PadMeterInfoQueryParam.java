package com.icicles.wmms.entity.param;

import com.icicles.wmms.entity.po.PadMeterInfo;
import lombok.*;

/**
 * <p>
 * pad端水表信息对内查询条件
 * </p>
 *
 * @author 
 * @since 2020-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PadMeterInfoQueryParam extends BaseParam<PadMeterInfo> {


    private String meterYardSn;

    private String meterYardId;

    private String meterMac;

    private String remark;

    private String pulseSum;

    private String waterSum;

    private String meterNum;


}
