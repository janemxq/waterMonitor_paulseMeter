package com.icicles.wmms.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 水表的收费标准
 * </p>
 *
 * @author 
 * @since 2020-06-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("ws_fee_standard")
@ApiModel(value="WsFeeStandard", description="收费标准实体说明")
public class WsFeeStandard extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * 收费名称
     */
    @ApiModelProperty(name = "display_name", value = "收费名称")
    private String displayName;

    /**
     * 用于排序的字段
     */
    @ApiModelProperty(name = "sort_id", value = "用于排序的字段，值越小于越靠前")
    private Integer sortId;

    /**
     * 收费标准，形式如0-50,2&51-100,3(区间开始和区间结束用"-"分隔，区间和价格用","分隔，区间直接用"&"分隔)
     */
    @ApiModelProperty(name = "fee_standard", value = "收费标准(有特定的格式,前端需要验证，待补充)")
    private String feeStandard;

    /**
     * 否按照阶梯计价
     */
    @ApiModelProperty(name = "is_step", value = "是否按照阶梯计价：0不是1是。默认1")
    private Integer isStep;

    /**
     * 唯一标识
     */
    @ApiModelProperty(name = "sn", value = "唯一标识")
    private String sn;
}
