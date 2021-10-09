package com.icicles.wmms.service;

import com.icicles.wmms.entity.po.WsFeeStandard;
import com.icicles.wmms.entity.po.WsFeeStandardItem;

import java.math.BigDecimal;
import java.util.List;

/**
 * 计算水费服务接口
 * @author lisy
 */
public interface WaterFeeService {

    /**
     * 根据用水量和收费标准，计算水费
     * @param volume        总的用水量
     * @param feeStandard   收费标准（表ws_fee_standard对应的实体）
     * @param startTime     缴费周期的开始时间
     * @param endTime       缴费周期的结束时间
     * @return 总的水费
     */
    BigDecimal getWaterFee(BigDecimal volume, WsFeeStandard feeStandard, String startTime, String endTime);

    /**
     * 获取水费的收费标准项的集合
     * @param feeStandard  收费标准
     * @return 水费的收费标准项的集合
     */
    List<WsFeeStandardItem> getStandards(WsFeeStandard feeStandard);
}
