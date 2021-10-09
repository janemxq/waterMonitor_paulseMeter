package com.icicles.wmms.entity.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 缴费相关的统计
 * @author lisy
 */
@Data
public class PaymentStatisticsDTO {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 金额
     */
    private BigDecimal account;
    /**
     * 时间（年）
     */
    private String year;
    /**
     * 总用水量
     */
    private BigDecimal waterSum;
}
