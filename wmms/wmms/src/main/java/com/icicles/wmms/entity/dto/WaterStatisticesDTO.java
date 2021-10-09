package com.icicles.wmms.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class WaterStatisticesDTO {
    /**
     * 日期（如月份、季度等）
     */
    private String date;
    /**
     * 对应的数据
     */
    private BigDecimal num;
    /**
     * 村名
     */
    @TableField(exist = false)
    private String villageName;

    public WaterStatisticesDTO(String date,BigDecimal num){
        this.date = date;
        this.num = num;
    }

    public WaterStatisticesDTO(){}
}
