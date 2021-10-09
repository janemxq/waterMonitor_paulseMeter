package com.icicles.wmms.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.icicles.wmms.entity.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * pad端表井
 * </p>
 *
 * @author 
 * @since 2020-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("pad_meter_yard")
@ApiModel(value="pad端水表信息", description="pad端水表信息对应的实体类")
public class PadMeterYard extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * pad端表井名称
     */
    @ApiModelProperty(name = "name", value = "pad端表井名称")
    private String name;

    /**
     * pad端表井地址
     */
    @ApiModelProperty(name = "address", value = "pad端表井地址")
    private String address;

    /**
     * 唯一字符串标识
     */
    @ApiModelProperty(name = "sn", value = "唯一字符串标识")
    private String sn;

    /**
     * pad端表井备注
     */
    @ApiModelProperty(name = "remark", value = "pad端表井备注")
    private String remark;
}
