package com.icicles.wmms.entity.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.icicles.wmms.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 水表的收费标准子项
 * </p>
 *
 * @author 
 * @since 2020-06-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("ws_fee_standard_item")
@ApiModel(value="WsFeeStandardItem", description="收费标准子项")
public class WsFeeStandardItem extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * 表ws_fee_standard的id
     */
    @ApiModelProperty(name = "feeStandardId", value = "父级id")
    private Long feeStandardId;

    /**
     * 计费区间开始项
     */
    @ApiModelProperty(name = "startNum", value = "计费区间开始项")
    private BigDecimal startNum;

    /**
     * 收费标准
     */
    @ApiModelProperty(name = "endNum", value = "收费标准区间结束值")
    private BigDecimal endNum;

    /**
     * 是否按照阶梯计价：0不是1是
     */
    @ApiModelProperty(name = "isStep", value = "是否按照阶梯计价")
    private Integer isStep;

    /**
     * 价格
     */
    /**
     * 收费标准子项
     */
    @ApiModelProperty(name = "items", value = "收费区间价格")
    private BigDecimal price;


}
