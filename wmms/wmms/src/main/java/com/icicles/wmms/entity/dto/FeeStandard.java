package com.icicles.wmms.entity.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 收费标准
 * @author lisy
 */
@Data
public class FeeStandard {
    public FeeStandard() {
    }

    public FeeStandard(BigDecimal startNum, BigDecimal endNum, BigDecimal price, Integer flight) {
        this.startNum = startNum;
        this.endNum = endNum;
        this.price = price;
        this.flight = flight;
    }

    @Override
    public String toString() {
        return "FeeStandard{" +
                "startNum=" + startNum +
                ", endNum=" + endNum +
                ", price=" + price +
                ", flight=" + flight +
                '}';
    }

    /**
     * 计价区间（始）
     */
    private BigDecimal startNum;
    /**
     * 计价区间（终）
     */
    private BigDecimal endNum;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 是否按照阶梯收费
     */
    private Integer flight;
}
